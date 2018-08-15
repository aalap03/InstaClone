package com.example.aalap.instaclone.likes

import android.os.Bundle
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.include_layout_bottombar.*
import kotlinx.android.synthetic.main.layout_add.*
import org.jetbrains.anko.info

class LikesActivity : BaseActivity() {
    override fun getToolbarTitle(): String {
        return "Likes bro..!!"
    }

    override fun getScreenNum(): Int {
        return Constants.LIKES
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_likes
    }
}
