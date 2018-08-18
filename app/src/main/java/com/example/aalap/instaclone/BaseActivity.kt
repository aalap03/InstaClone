package com.example.aalap.instaclone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.aalap.instaclone.Constants.Companion.ACCOUNT
import com.example.aalap.instaclone.Constants.Companion.ADD
import com.example.aalap.instaclone.Constants.Companion.HOME
import com.example.aalap.instaclone.Constants.Companion.LIKES
import com.example.aalap.instaclone.Constants.Companion.SEARCH
import com.example.aalap.instaclone.account.AccountActivity
import com.example.aalap.instaclone.addPost.AddActivity
import com.example.aalap.instaclone.home.HomeActivity
import com.example.aalap.instaclone.likes.LikesActivity
import com.example.aalap.instaclone.search.SearchActivity
import kotlinx.android.synthetic.main.include_layout_bottombar.*
import kotlinx.android.synthetic.main.include_layout_toolbar.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.lang.Exception
import javax.inject.Inject


val TAG = "BaseActivity:"

abstract class BaseActivity : AppCompatActivity(), AnkoLogger {

    abstract fun getScreenNum(): Int

    abstract fun getLayoutId(): Int

    abstract fun getToolbarTitle():String

    @Inject
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        if (toolbar != null) {
            toolbar.title = getToolbarTitle()
            setSupportActionBar(toolbar)
        }
        info { "Here I am..." }
        (App.create()).component.inject(this)

        if (bottom_navigation != null)
            setupBottomNavigation(bottom_navigation, this, getScreenNum())
    }

    public override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    fun setupBottomNavigation(bottomNav: AHBottomNavigation, context: Context, screenNum: Int) {

        info { "setting bottom nav...(" }

        info { "Num:$screenNum" }

        val item1 = AHBottomNavigationItem("Home", R.drawable.bottom_home_outline)
        val item2 = AHBottomNavigationItem("Search", R.drawable.bottom_search)
        val item3 = AHBottomNavigationItem("Add", R.drawable.bottom_add_outlined)
        val item4 = AHBottomNavigationItem("Likes", R.drawable.bottom_like_outline)
        val item5 = AHBottomNavigationItem("Account", R.drawable.bottom_account_outlined)

        bottomNav.addItem(item1)
        bottomNav.addItem(item2)
        bottomNav.addItem(item3)
        bottomNav.addItem(item4)
        bottomNav.addItem(item5)

        bottomNav.currentItem = screenNum

        bottomNav.getItem(screenNum).setDrawable(getFilledIcon(screenNum))


        info { "currentItem: ${bottomNav.currentItem}" }

        bottomNav.setOnTabSelectedListener { position, wasSelected ->

            bottomNav.postDelayed({
                try {

                    if (position == HOME) {
                        if (!wasSelected) {
                            startActivity(Intent(context, HomeActivity::class.java))
                        }
                    } else if (position == SEARCH) {
                        if (!wasSelected) {
                            startActivity(Intent(context, SearchActivity::class.java))
                            finish()

                        }
                    } else if (position == ADD) {
                        if (!wasSelected) {
                            startActivity(Intent(context, AddActivity::class.java))
                        }
                    } else if (position == ACCOUNT) {
                        if (!wasSelected) {
                            startActivity(Intent(context, AccountActivity::class.java))
                            finish()
                        }
                    } else if (position == LIKES) {
                        if (!wasSelected) {
                            startActivity(Intent(context, LikesActivity::class.java))
                            finish()
                        }
                    } else {
                        startActivity(Intent(context, HomeActivity::class.java))
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 100)

            true
        }

    }

    fun getFilledIcon(screenNum: Int): Int {

        when (screenNum) {
            HOME -> return R.drawable.bottom_home_filled
            ADD -> return R.drawable.bottom_add_filled
            SEARCH -> return R.drawable.bottom_search
            ACCOUNT -> return R.drawable.bottom_account_filled
            LIKES -> return R.drawable.bottom_like_filled
        }
        return R.drawable.bottom_home_filled
    }


}