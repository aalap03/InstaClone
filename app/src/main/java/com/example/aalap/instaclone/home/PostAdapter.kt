package com.example.aalap.instaclone.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aalap.instaclone.Models.UserPost
import com.example.aalap.instaclone.R
import com.example.aalap.instaclone.Utils
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.info
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(var context: Context, var posts: MutableList<UserPost>) : RecyclerView.Adapter<PostAdapter.PostHolder>(), AnkoLogger {


    var requestManager = Glide.with(context).applyDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ic_launcher_round))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        return PostHolder(LayoutInflater.from(context).inflate(R.layout.post_item, parent, false))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bindItems(posts[position], position)
    }

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(userPost: UserPost, position: Int) {
            val postImage = itemView.findViewById<ImageView>(R.id.post_image)
            val userImage = itemView.findViewById<ImageView>(R.id.post_user_image)
            val userName = itemView.findViewById<TextView>(R.id.post_user_name)
            val postCaption = itemView.findViewById<TextView>(R.id.post_caption)
            val postTime= itemView.findViewById<TextView>(R.id.post_time)


            postImage.layoutParams.width = context.displayMetrics.widthPixels
            postImage.layoutParams.height = context.displayMetrics.heightPixels/2

            info { userPost }
            requestManager
                    .load(userPost.postImage)
                    .into(postImage)

            requestManager
                    .load(userPost.user.profilePic)
                    .into(userImage)

            userName.text = userPost.user.name
            postCaption.text = userPost.caption
            postTime.text = Utils.formatDate(userPost.postTime)
        }



    }
}