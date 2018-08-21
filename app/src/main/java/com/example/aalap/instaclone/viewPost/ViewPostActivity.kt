package com.example.aalap.instaclone.viewPost

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.R
import com.example.aalap.instaclone.Utils
import com.example.aalap.instaclone.account.POST_CAPTION
import com.example.aalap.instaclone.account.POST_IMAGE
import com.example.aalap.instaclone.account.POST_TIME
import kotlinx.android.synthetic.main.post_item.*

class ViewPostActivity : AppCompatActivity() {

    lateinit var preference: Preference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_item)
        preference = Preference(applicationContext)

        Glide.with(this)
                .load(preference.getProfilePic())
                .into(post_user_image)

        Glide.with (this)
                .load(intent.getStringExtra(POST_IMAGE))
                .into(post_image)

        post_user_name.text = preference.getUserName()
        post_caption.text = intent.getStringExtra(POST_CAPTION)
        post_time.text = Utils.formatDate(intent.getStringExtra(POST_TIME))

    }

}