package com.example.aalap.instaclone.addPost

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.aalap.instaclone.BasePostActivity
import com.example.aalap.instaclone.models.User
import com.example.aalap.instaclone.models.UserPost
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.R
import com.example.aalap.instaclone.models.UserStory
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
import kotlinx.android.synthetic.main.layout_progress.*
import org.jetbrains.anko.*
import java.io.File
import java.lang.Exception
import java.util.*

class PostActivity : BasePostActivity(), AnkoLogger {

    var selectedImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        post_story.visibility = View.GONE
        post_image.visibility = View.VISIBLE
        if (intent != null) {
            selectedImage = intent.getStringExtra(SELECTED_IMAGE)
            Glide.with(this)
                    .load(selectedImage)
                    .into(post_image)
        }
    }

    override fun getToolbarTitle(): String {
        return "Add Post"
    }

    override fun getStorageReference(): String {
        return "user_posts"
    }

    override fun getStorageReferenceChild(): String {
        return "user_post_images"
    }

    override fun getFBReferenceChild(): String {
        return "user_posts"
    }

    override fun getPostUri(): String {
        return selectedImage
    }

    override fun getUserPost(resultImageUri: String): UserPost? {

        return UserPost(fbReference.push().key!!,
                caption.text.toString(),
                resultImageUri,
                Date().toString(),
                getCurretUser())
    }

    override fun getUserStory(resultVideoUri: String): UserStory? {
        return null
    }
}