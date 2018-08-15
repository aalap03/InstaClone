package com.example.aalap.instaclone.add

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.aalap.instaclone.Models.User
import com.example.aalap.instaclone.Models.UserPost
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.include_layout_toolbar.*
import kotlinx.android.synthetic.main.layout_post_screen.*
import org.jetbrains.anko.*
import java.io.File
import java.lang.Exception

class PostActivity : AppCompatActivity(), AnkoLogger {

    var selectedImage = ""
    lateinit var caption: EditText
    lateinit var firebaseDb: FirebaseDatabase
    lateinit var fbReference: DatabaseReference
    lateinit var storageReference: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_post_screen)

        toolbar.title = "Post screen"
        setSupportActionBar(toolbar)

        firebaseDb = FirebaseDatabase.getInstance()
        fbReference = firebaseDb.reference
        storageReference = FirebaseStorage.getInstance().getReference("user_posts")

        post_caption_layout.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Add Caption"
        caption = post_caption_layout.findViewById(R.id.input_edit_text)

        val layoutParams = post_image.layoutParams

        var displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        post_image.layoutParams.width = (displayMetrics.widthPixels * 0.7).toInt()
        post_image.layoutParams.height = (displayMetrics.heightPixels * 0.7).toInt()

        if (intent != null) {
            selectedImage = intent.getStringExtra(SELECTED_IMAGE)
            Glide.with(this)
                    .load(selectedImage)
                    .into(post_image)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_post_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(0)?.title = "Post"
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.menu_add_post_next -> {

                info { "preparing to post...!!!" }
                if (caption.text.toString().isEmpty()) {
                    Toast.makeText(this, "Please add caption", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    val imageUri = Uri.fromFile(File(selectedImage))
                    var imageStoreReference = storageReference.child("user_post_images")

                    imageStoreReference.putFile(imageUri).continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                        override fun then(snapShot: Task<UploadTask.TaskSnapshot>): Task<Uri> {

                            if (snapShot.isSuccessful) {
                                info { "returning ${imageStoreReference.downloadUrl}" }
                                return imageStoreReference.downloadUrl
                            }

                            return throw snapShot.exception!!
                        }
                    }).addOnCompleteListener(object : OnCompleteListener<Uri?> {
                        override fun onComplete(task: Task<Uri?>) {
                            var postId = fbReference.push().key

                            var preference = Preference(applicationContext)

                            var user = User(preference.getUserName(), preference.getUserEmail()
                                    , preference.getUserId(), preference.getProfilePic())

                            var userPost = UserPost(postId!!,
                                    caption.text.toString(),
                                    task.result.toString(),
                                    user)
                            info { "setting value.. $userPost" }
                            fbReference.child("user_posts").child(postId).setValue(userPost)
                            info { "value set..!! $userPost" }
                        }
                    }).addOnFailureListener(object : OnFailureListener {
                        override fun onFailure(exception: Exception) {
                            exception.printStackTrace()
                            info { "shoot error: ${exception.message}" }
                        }
                    })
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


}