package com.example.aalap.instaclone.search

import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R

class SearchActivity : BaseActivity() {

    override fun getScreenNum(): Int {
        return Constants.SEARCH
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_search
    }
}
