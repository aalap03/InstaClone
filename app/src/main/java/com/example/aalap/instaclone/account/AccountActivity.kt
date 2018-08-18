package com.example.aalap.instaclone.account

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.Models.UserPost
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.include_layout_bottombar.*
import kotlinx.android.synthetic.main.layout_account.*
import org.jetbrains.anko.info

class AccountActivity : BaseActivity(), ImageAdapter.CallBack {

    lateinit var preference : Preference
    var userPosts = mutableListOf<String>()

    override fun deliverImage(imagePath: String) {
        info { "Clicked imagePath: $imagePath" }
    }

    override fun getToolbarTitle(): String {
        return "Account bro...!!!"
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_account
    }

    override fun getScreenNum(): Int {
        return Constants.ACCOUNT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preference = Preference(applicationContext)
        //account_recycler_view.layoutManager = GridLayoutManager(this, 4)
        var requestManager = Glide.with(this)
                .applyDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ic_launcher_round))
        var imagesAdapter = ImageAdapter(this, userPosts, requestManager, this@AccountActivity)
        //account_recycler_view.adapter = imagesAdapter

        FirebaseDatabase.getInstance().reference.child("user_posts").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                info { "Error: ${error.message}" }
            }

            override fun onDataChange(snapShot: DataSnapshot) {

                if(snapShot.childrenCount > 0) {
                    for(snapshott in snapShot.children) {
                        val value = snapshott.getValue(UserPost::class.java)
                        if(value?.user?.id.equals(preference.getUserId())) {
                            userPosts.add(value?.postImage!!)
                        }
                    }
                    imagesAdapter.notifyDataSetChanged()
                }
            }
        })
        gridView.numColumns = 4
        gridView.adapter = imagesAdapter

    }

    fun editProfile(view: View) {
        startActivity(Intent(this, AccountEditActivity::class.java))
    }
}
