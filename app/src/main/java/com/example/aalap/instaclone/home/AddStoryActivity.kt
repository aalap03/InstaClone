package com.example.aalap.instaclone.home

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.example.aalap.instaclone.BasePostActivity
import com.example.aalap.instaclone.models.UserPost
import com.example.aalap.instaclone.models.UserStory
import kotlinx.android.synthetic.main.layout_post_screen.*
import org.jetbrains.anko.AnkoLogger
import java.util.*

class AddStoryActivity : BasePostActivity(), AnkoLogger {

    lateinit var videoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        post_story.visibility = View.VISIBLE
        post_image.visibility = View.GONE

        if (intent != null) {
            videoUri = intent.data
            post_story.setVideoURI(videoUri)
            post_story.start()
        }
    }

    override fun getUserPost(resultImageUri: String): UserPost? {
        return null
    }

    override fun getUserStory(resultVideoUri: String): UserStory? {
        return UserStory(fbReference.push().key!!,
                caption.text.toString(),
                resultVideoUri,
                Date().toString(),
                getCurretUser())
    }

    override fun getToolbarTitle(): String {
        return "Add Story"
    }

    override fun getStorageReference(): String {
        return "user_stories"
    }

    override fun getStorageReferenceChild(): String {
        return "user_story_video"
    }

    override fun getFBReferenceChild(): String {
        return "user_stories"
    }

    override fun getPostUri(): String {
        return videoUri.toString()
    }

}
