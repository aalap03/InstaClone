package com.example.aalap.instaclone.search

import android.os.Bundle
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.include_layout_bottombar.*
import kotlinx.android.synthetic.main.layout_search.*
import org.jetbrains.anko.info

class SearchActivity : BaseActivity() {

    override fun getScreenNum(): Int {
        return Constants.SEARCH
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_search
    }
}
