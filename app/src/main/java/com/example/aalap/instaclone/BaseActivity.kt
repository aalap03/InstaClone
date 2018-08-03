package com.example.aalap.instaclone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.aalap.instaclone.Constants.Companion.ACCOUNT
import com.example.aalap.instaclone.Constants.Companion.ADD
import com.example.aalap.instaclone.Constants.Companion.HOME
import com.example.aalap.instaclone.Constants.Companion.LIKES
import com.example.aalap.instaclone.Constants.Companion.SEARCH
import com.example.aalap.instaclone.account.AccountActivity
import com.example.aalap.instaclone.add.AddActivity
import com.example.aalap.instaclone.home.HomeActivity
import com.example.aalap.instaclone.likes.LikesActivity
import com.example.aalap.instaclone.search.SearchActivity
import kotlinx.android.synthetic.main.layout_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import javax.inject.Inject


val TAG = "BaseActivity:"

abstract class  BaseActivity : AppCompatActivity(), AnkoLogger {

    abstract fun getScreenNum(): Int
    abstract fun getLayoutId(): Int

    @Inject
    lateinit var context: Context

    lateinit var bottomNav: AHBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        info { "Here I am..." }

        (App.create()).component.inject(this)
        bottomNav = findViewById(R.id.bottom_navigation)
        setupBottomNavigation(bottomNav)
        dummy_text.text = getScreenNum().toString()
    }

    fun setupBottomNavigation(bottomNav: AHBottomNavigation) {

        info { "setting bottom nav...(" }

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

        if(bottomNav.currentItem == HOME) {
            bottomNav.getItem(HOME).setDrawable(R.drawable.bottom_home_filled)
        }else
            bottomNav.getItem(HOME).setDrawable(R.drawable.bottom_home_outline)

        bottomNav.setOnTabSelectedListener { position, wasSelected ->

            try{
                var intent = Intent()

                if (position == HOME) {
                    if (!wasSelected) {
                        intent = Intent(context, HomeActivity::class.java)
                        bottomNav.getItem(HOME).setDrawable(R.drawable.bottom_home_filled)
                        startActivity(intent)
                    }
                } else if (position == SEARCH) {
                    if (!wasSelected) {
                        intent = Intent(context, SearchActivity::class.java)
                        bottomNav.getItem(SEARCH).setDrawable(R.drawable.bottom_search)
                        startActivity(intent)
                    }
                } else if (position == ADD) {
                    if (!wasSelected) {
                        intent = Intent(context, AddActivity::class.java)
                        bottomNav.getItem(ADD).setDrawable(R.drawable.bottom_add_filled)
                        startActivity(intent)
                    }
                } else if (position == ACCOUNT) {
                    if (!wasSelected) {
                        intent = Intent(context, AccountActivity::class.java)
                        bottomNav.getItem(ACCOUNT).setDrawable(R.drawable.bottom_account_filled)
                        startActivity(intent)
                    }
                } else if (position == LIKES) {
                    if (!wasSelected) {
                        intent = Intent(context, LikesActivity::class.java)
                        bottomNav.getItem(LIKES).setDrawable(R.drawable.bottom_like_filled)
                        startActivity(intent)
                    }
                } else {
                    intent = Intent(context, HomeActivity::class.java)
                    Toast.makeText(context, "Invalid tab selection", Toast.LENGTH_SHORT)
                            .show()
                    startActivity(intent)
                }


            } catch(e: Exception) {
                e.printStackTrace()
            }

            true
        }

    }


}