package com.example.aalap.instaclone.add

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.include_layout_bottombar.*
import kotlinx.android.synthetic.main.layout_add.*
import org.jetbrains.anko.info
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

val SELECTED_IMAGE = "selected_image"

class AddActivity : BaseActivity() {

    lateinit var pagerAdapter: AddScreenPagerAdapter

    override fun getToolbarTitle(): String {
        return "Add bro..."
    }

    override fun getScreenNum(): Int {
        return Constants.ADD
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_add
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pagerAdapter = AddScreenPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(GalleryFragment())
        pagerAdapter.addFragment(CameraFragment())

        add_screen_viewpager.adapter = pagerAdapter
        add_screen_tablayout.setupWithViewPager(add_screen_viewpager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_post_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        var selectedImage = ""

        when(item?.itemId) {

            R.id.menu_add_post_next -> {

                when(add_screen_tablayout.selectedTabPosition) {

                    0-> {
                        selectedImage = (pagerAdapter.getItem(0) as GalleryFragment).selectedImagePath
                    }
                    1-> {
                        selectedImage = (pagerAdapter.getItem(1) as CameraFragment).mCurrentPhotoPath
                    }
                }

                var intent = Intent(this, PostActivity::class.java)
                intent.putExtra(SELECTED_IMAGE, selectedImage)
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }
}