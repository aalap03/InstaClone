package com.example.aalap.instaclone

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.aalap.instaclone.models.User
import com.example.aalap.instaclone.models.UserPost
import com.example.aalap.instaclone.models.UserStory
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.include_layout_toolbar.*
import kotlinx.android.synthetic.main.layout_post_screen.*
import kotlinx.android.synthetic.main.layout_progress.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.File
import java.util.*


abstract class BasePostActivity : AppCompatActivity(), AnkoLogger {

    abstract fun getToolbarTitle(): String

    abstract fun getStorageReference(): String

    abstract fun getStorageReferenceChild(): String

    abstract fun getFBReferenceChild(): String

    abstract fun getPostUri(): String

    abstract fun getUserPost(resultImageUri: String): UserPost?

    abstract fun getUserStory(resultVideoUri: String): UserStory?

    lateinit var firebaseDb: FirebaseDatabase
    lateinit var fbReference: DatabaseReference
    lateinit var storageReference: StorageReference
    lateinit var caption: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_post_screen)
        toolbar.title = getToolbarTitle()
        setSupportActionBar(toolbar)
        post_progress.visibility = View.GONE

        post_caption_layout.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Add Caption"
        caption = post_caption_layout.findViewById(R.id.input_edit_text)

        firebaseDb = FirebaseDatabase.getInstance()
        fbReference = firebaseDb.reference
        storageReference = FirebaseStorage.getInstance().getReference(getStorageReference())

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

                info { "fbRefCH: ${getFBReferenceChild()}" }
                info { "postURI: ${getPostUri()}" }
                info { "stoRef: ${getStorageReference()}" }
                info { "stoRefCH: ${getStorageReferenceChild()}" }

                info { "preparing to post...!!!" }
                if (caption.text.toString().isEmpty()) {
                    Toast.makeText(this, "Please add caption", Toast.LENGTH_SHORT)
                            .show()
                } else {

                    post_progress.visibility = View.VISIBLE

                    val imageUri = Uri.fromFile(File(getPostUri()))
                    var imageStoreReference = storageReference.child(getStorageReferenceChild())

                    imageStoreReference.putFile(imageUri).continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { snapShot ->
                        if (snapShot.isSuccessful) {
                            info { "returning ${imageStoreReference.downloadUrl}" }
                            return@Continuation imageStoreReference.downloadUrl
                        }

                        throw snapShot.exception!!
                    }).addOnCompleteListener { task ->
                        val postId = fbReference.push().key

                        var userPost:Any? = getUserPost(task.result.toString()) as UserPost

                        if( userPost == null) {
                            userPost = getUserStory(task.result.toString()) as UserStory
                        }

                        info { "setting value.. $userPost" }
                        fbReference.child(getFBReferenceChild()).child(postId!!).setValue(userPost)
                        info { "value set..!! $userPost" }
                        post_progress.visibility = View.GONE
                        finish()
                    }.addOnFailureListener { exception ->
                        exception.printStackTrace()
                        info { "shoot error: ${exception.message}" }
                        Toast.makeText(this@BasePostActivity, "Post error: ${exception.message}", Toast.LENGTH_SHORT)
                                .show()
                        post_progress.visibility = View.GONE
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getCurretUser() :User{
        val preference = Preference(applicationContext)

        return User(preference.getUserName(), preference.getUserEmail()
                , preference.getUserId(), preference.getProfilePic())

    }


}