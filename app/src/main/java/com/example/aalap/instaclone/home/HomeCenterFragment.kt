package com.example.aalap.instaclone.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.aalap.instaclone.Models.UserPost
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.layout_center_home_fragment.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class HomeCenterFragment : Fragment(), AnkoLogger {

    lateinit var pref: Preference
    lateinit var postAdapter: PostAdapter
    var posts = mutableListOf<UserPost>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.layout_center_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = Preference(requireContext().applicationContext)
        home_user_name.text = pref.getUserName()
        info { pref.getUserId() }

        postAdapter = PostAdapter(requireContext(), posts)
        home_feeds_recycler.layoutManager = LinearLayoutManager(requireContext())
        home_feeds_recycler.adapter = postAdapter

        FirebaseDatabase.getInstance().reference.child("user_posts").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                info { "fuss.. ${error.message}" }
            }

            override fun onDataChange(snapShot: DataSnapshot) {
                for (snapshotObj in snapShot.children) {
                    val userPost = snapshotObj.getValue(UserPost::class.java)
                    posts.add(userPost!!)
                }
                postAdapter.notifyDataSetChanged()
            }
        })

        Glide.with(this)
                .load(pref.getProfilePic())
                .into(home_profile_pic)
    }
}