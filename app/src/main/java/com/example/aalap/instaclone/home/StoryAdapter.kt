package com.example.aalap.instaclone.home

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aalap.instaclone.R
import com.example.aalap.instaclone.models.UserStory
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

val STORY_URI = "Story_uri"

class StoryAdapter(var context: Context, var posts: MutableList<UserStory>) : RecyclerView.Adapter<StoryAdapter.PostHolder>(), AnkoLogger {

    var requestManager = Glide.with(context).applyDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ic_launcher_round))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        return PostHolder(LayoutInflater.from(context).inflate(R.layout.simple_circle_image_item, parent, false))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        var userStory = posts[position]
        holder.bindItems(userStory)
        holder.itemView.setOnClickListener { holder.bindClick(userStory, position) }
    }

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(userPost: UserStory) {

            val userImage = itemView.findViewById<ImageView>(R.id.user_circle_image)
            info { userPost }
            requestManager
                    .load(userPost.user.profilePic)
                    .into(userImage)
        }

        fun bindClick(userStory: UserStory, position: Int) {
            var intent = Intent(context, StoryScreen::class.java)
            intent.putExtra(STORY_URI, userStory.storyUri)
            context.startActivity(intent)
        }


    }
}