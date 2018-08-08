package com.example.aalap.instaclone.account

import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.View
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.layout_account.*
import org.jetbrains.anko.info

class AccountActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.layout_account
    }

    override fun getScreenNum(): Int {
        return Constants.ACCOUNT
    }

    fun editProfile(view: View) {
        startActivity(Intent(this, AccountEditActivity::class.java))
    }


}
