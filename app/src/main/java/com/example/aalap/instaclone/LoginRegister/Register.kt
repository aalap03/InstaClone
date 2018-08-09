package com.example.aalap.instaclone.LoginRegister

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
import android.util.Log
import android.view.View
import android.widget.*
import com.example.aalap.instaclone.R
import com.example.aalap.instaclone.account.CODE_MEDIA_PERMISSION
import com.example.aalap.instaclone.account.CODE_PICK_IMAGE
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.account_edit_profile.*
import kotlinx.android.synthetic.main.register_screen.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor

class Register : AppCompatActivity(), AnkoLogger {

    var imageUri: Uri? = null
    lateinit var email: TextInputEditText
    lateinit var name: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var confirmPassword: TextInputEditText
    lateinit var authInstance: FirebaseAuth

    private val fabWidth: Int
        get() = resources.getDimension(R.dimen.fab_width).toInt()

    private val buttonWidth: Int
        get() = resources.getDimension(R.dimen.button_width).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_screen)

        register_profile_image.setOnClickListener { pickImage() }
        register_button.setOnClickListener { validateAndRegister() }
        setupEditText()
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

        }
    }

    private fun registerUser(userName: String, password: String) {
        authInstance = FirebaseAuth.getInstance()

        authInstance.createUserWithEmailAndPassword(userName, password).addOnSuccessListener { authResult ->
            //circular effect with for new screen
            enterApp(authResult)
            Toast.makeText(this@Register, "Registered", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            e.printStackTrace()
            //set button with text again

            Handler().postDelayed({ animateButtonWidth(false) }, 500)

        }
    }

    private fun enterApp(authResult: AuthResult?) {

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
                pickImage()
            } else {
                Toast.makeText(this, "FOOK OFF...", Toast.LENGTH_SHORT)
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
}
