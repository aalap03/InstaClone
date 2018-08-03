package com.example.aalap.instaclone.home

import android.os.Bundle
import android.util.Log
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R

class HomeActivity: BaseActivity() {

    override fun getScreenNum(): Int {
        return Constants.HOME
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_home
    }

}