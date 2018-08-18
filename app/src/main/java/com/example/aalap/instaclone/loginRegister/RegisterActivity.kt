package com.example.aalap.instaclone.loginRegister

import android.Manifest
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.View
import android.widget.*
import com.example.aalap.instaclone.R
import com.example.aalap.instaclone.Models.User
import com.example.aalap.instaclone.Models.UserAccountDetails
import com.example.aalap.instaclone.account.CODE_MEDIA_PERMISSION
import com.example.aalap.instaclone.account.CODE_PICK_IMAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.register_screen.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import android.webkit.MimeTypeMap
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.home.HomeActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity(), AnkoLogger {

    var imageUri: Uri? = null
    lateinit var email: TextInputEditText
    lateinit var name: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var confirmPassword: TextInputEditText
    lateinit var authInstance: FirebaseAuth
    lateinit var firebaseDB: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var storageReference: StorageReference
    lateinit var pref: Preference

    private val fabWidth: Int
        get() = resources.getDimension(R.dimen.fab_width).toInt()

    private val buttonWidth: Int
        get() = resources.getDimension(R.dimen.button_width).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_screen)

        info{formatDate(Date().toString())}

        firebaseDB = FirebaseDatabase.getInstance()
        databaseReference = firebaseDB.reference
        storageReference = FirebaseStorage.getInstance().getReference("user_images")
        pref = Preference(applicationContext)

        register_profile_image.setOnClickListener { pickImage() }
        register_button.setOnClickListener { validateAndRegister() }
        setupEditText()

        login.setOnClickListener{loginScreen()}
    }

    private fun loginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun validateAndRegister() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select profile picture", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(email.text.toString())) {
            Toast.makeText(this, "Please Enter email", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(name.text.toString())) {
            Toast.makeText(this, "Please Enter name", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(password.text.toString())) {
            Toast.makeText(this, "Please Enter Passwrod", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(confirmPassword.text.toString())) {
            Toast.makeText(this, "Please Confirm password", Toast.LENGTH_SHORT).show()
        } else if (!password.text.toString().equals(confirmPassword.text.toString())) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
        } else {
            registerUser(email.text.toString(), name.text.toString(), password.text.toString())
        }
    }

    private fun registerUser(email: String, name: String, password: String) {

        animateButtonWidth(true)

        authInstance = FirebaseAuth.getInstance()
        var imageStorageRef: StorageReference = storageReference.child("${System.currentTimeMillis()}.${getFileExtension(imageUri!!)}")

        var user: User
        var userAccount: UserAccountDetails

        authInstance.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->

                    Toast.makeText(this@RegisterActivity, "Registered", Toast.LENGTH_SHORT).show()
                    if (authInstance.currentUser != null) {

                        imageStorageRef.putFile(imageUri!!).continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                            override fun then(task: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                                if (task.isSuccessful) {
                                    return imageStorageRef.downloadUrl
                                }
                                return throw task.exception!!
                            }
                        }).addOnCompleteListener{ task ->

                            val userId = authResult.user.uid
                            user = User(name, email, userId!!, task.result.toString())
                            databaseReference.child("users").child(userId).setValue(user)

                            userAccount = UserAccountDetails("", name, name, "", task.result.toString(), "", 0, 0, 0)
                            databaseReference.child("users_account_setting").child(userId).setValue(userAccount)

                            pref.setUserId(userId)
                            pref.setProfilePic(task.result.toString())
                            pref.setUserName(name)
                            pref.setUserEmail(email)

                            var intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()

                        }.addOnFailureListener { exception -> Toast.makeText(this@RegisterActivity, "Image Loading failed", Toast.LENGTH_SHORT).show()}

                    }

                }.addOnFailureListener { e ->
                    e.printStackTrace()
                    //set button with text again
                    info { "inAuthinstance failure" }
                    Handler().postDelayed({ animateButtonWidth(false) }, 500)
                }
    }

    private fun animateButtonWidth(isShrink: Boolean) {

        val anim: ValueAnimator
        if (isShrink)
            anim = ValueAnimator.ofInt(register_button.measuredWidth, fabWidth)
        else
            anim = ValueAnimator.ofInt(fabWidth, buttonWidth)

        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = register_button.layoutParams
            layoutParams.width = `val`
            register_button.layoutParams = layoutParams

            progress_bar.visibility = if (isShrink) View.VISIBLE else View.INVISIBLE
            text.visibility = if (isShrink) View.INVISIBLE else View.VISIBLE
        }
        anim.duration = 500
        anim.start()
    }

    private fun setupEditText() {

        register_email.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Email"
        register_name.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Full Name"
        register_password.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Password"
        register_confirm_password.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Confirm Password"
        register_password.findViewById<TextInputLayout>(R.id.input_edit_text_layout).isPasswordVisibilityToggleEnabled = true
        register_confirm_password.findViewById<TextInputLayout>(R.id.input_edit_text_layout).isPasswordVisibilityToggleEnabled = true


        email = register_email.findViewById(R.id.input_edit_text)
        name = register_name.findViewById(R.id.input_edit_text)
        password = register_password.findViewById(R.id.input_edit_text)
        confirmPassword = register_confirm_password.findViewById(R.id.input_edit_text)
        password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        confirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

    }

    private fun pickImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    , CODE_MEDIA_PERMISSION)
        } else {
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, CODE_PICK_IMAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CODE_MEDIA_PERMISSION) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted yooohoo...
                var intent = Intent(Intent.ACTION_PICK)
                intent.setType("image/*")
                startActivityForResult(intent, CODE_PICK_IMAGE)


            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "Please allow application to use image for profile", Toast.LENGTH_SHORT)
                        .show()
                pickImage()
            } else {
                Toast.makeText(this, "FOOK OFF...", Toast.LENGTH_SHORT)
                        .show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            register_profile_image.setImageURI(imageUri)
        }
    }

    private fun formatDate(postTime: String): String {
        var formattedTime = ""
        //Fri Aug 17 19:59:01 EDT 2018
        var simpleDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
        val parse = simpleDateFormat.parse(postTime)

        DateUtils.getRelativeTimeSpanString(parse.getTime(), Date().time, DateUtils.DAY_IN_MILLIS);
        return formattedTime
    }
}
