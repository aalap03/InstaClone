package com.example.aalap.instaclone.add

import android.os.Bundle
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.include_layout_bottombar.*
import kotlinx.android.synthetic.main.layout_add.*

class AddActivity : BaseActivity() {
    override fun getScreenNum(): Int {
        return Constants.ADD
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_add
    }
}
