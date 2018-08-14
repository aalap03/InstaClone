package com.example.aalap.instaclone.home

import android.os.Bundle
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.include_layout_home_viewpager.*

class HomeActivity: BaseActivity() {

    override fun getScreenNum(): Int {
        return Constants.HOME
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var pagerAdapter = PagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(HomeCenterFragment())

        home_viewpager.adapter = pagerAdapter

        home_tablayout.setupWithViewPager(home_viewpager)
    }
}