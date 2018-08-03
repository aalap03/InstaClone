package com.example.aalap.instaclone.account

import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R
import org.jetbrains.anko.info

class AccountActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.layout_account
    }

    override fun getScreenNum(): Int {
        info { "Account:" }
        return Constants.ACCOUNT
    }
}
