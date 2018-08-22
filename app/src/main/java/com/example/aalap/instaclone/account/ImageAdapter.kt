package com.example.aalap.instaclone.account

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.example.aalap.instaclone.models.UserPost
import com.example.aalap.instaclone.R
import org.jetbrains.anko.displayMetrics

class ImageAdapter(private val mContext: Context, var mThumbIds: List<Any>, var requestManager: RequestManager, var callBack: ImageCallback) : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(LayoutInflater.from(mContext).inflate(R.layout.image_items, parent, false))
    }

    override fun getItemCount(): Int {
        return mThumbIds.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {

        val any = mThumbIds[position]

        if (any is UserPost)
            requestManager
                    .load(any.postImage)
                    .into(holder.imageView)
        else
            requestManager
                    .load(any)
                    .into(holder.imageView)

        holder.itemView.setOnClickListener { holder.bindClick(any) }

        holder.imageView.layoutParams.width = mContext.displayMetrics.widthPixels / 4
        holder.imageView.layoutParams.height = mContext.displayMetrics.widthPixels / 4
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image_item)

        fun bindClick(userPost: Any) {
            callBack.deliverImage(userPost)
        }
    }


}