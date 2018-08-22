package com.example.aalap.instaclone.account

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.models.UserPost
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.R
import com.example.aalap.instaclone.viewPost.ViewPostActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.layout_account.*
import org.jetbrains.anko.info

val POST_IMAGE = "post_image"
val POST_CAPTION = "post_caption"
val POST_TIME = "post_TIME"

class AccountActivity : BaseActivity(), ImageCallback {

    lateinit var preference : Preference
    var userPosts = mutableListOf<UserPost>()

    override fun deliverImage(post: Any) {
        val userPost = post as UserPost
        var intent = Intent(this, ViewPostActivity::class.java)
        intent.putExtra(POST_IMAGE, userPost.postImage)
        intent.putExtra(POST_CAPTION, userPost.caption)
        intent.putExtra(POST_TIME, userPost.postTime)
        startActivity(intent)
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

        var requestManager = Glide.with(this)
                .applyDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ic_launcher_round))

        FirebaseDatabase.getInstance().reference.child("user_posts").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                info { "Error: ${error.message}" }
            }

            override fun onDataChange(snapShot: DataSnapshot) {

                info{
                    snapShot.childrenCount
                }
                if(snapShot.childrenCount > 0) {
                    info { "going for snapshot" }
                    var int = 0
                    for(snapshott in snapShot.children) {
                        info { "childre: ${int++}" }
                        val value = snapshott.getValue(UserPost::class.java)
                        if(value?.user?.id.equals(preference.getUserId())) {
                            userPosts.add(value!!)
                            userPosts.add(value!!)
                            userPosts.add(value!!)
                            userPosts.add(value!!)
                            userPosts.add(value!!)
                            userPosts.add(value!!)
                            userPosts.add(value!!)
                            info { "addingItem" }
                        }
                    }
                    info { "trying to notify" }
                    account_recycler_view.layoutManager = GridLayoutManager(this@AccountActivity, 4)

                    account_recycler_view.adapter = ImageAdapter(this@AccountActivity, userPosts,
                            requestManager, this@AccountActivity)
                }
            }
        })


    }

    fun editProfile(view: View) {
        startActivity(Intent(this, AccountEditActivity::class.java))
    }
}
