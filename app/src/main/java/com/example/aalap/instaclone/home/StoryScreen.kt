package com.example.aalap.instaclone.home

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.story_screen.*

class StoryScreen:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.story_screen)

        story_video.setVideoURI(Uri.parse(intent.getStringExtra(STORY_URI)))
        story_video.start()
    }
}
