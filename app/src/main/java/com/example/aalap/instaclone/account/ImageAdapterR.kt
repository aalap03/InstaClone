package com.example.aalap.instaclone.account

import android.content.Context
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.image_items.view.*
import kotlin.coroutines.experimental.coroutineContext

class ImageAdapterR(private val mContext: Context, var mThumbIds: List<String>, var requestManager: RequestManager) : RecyclerView.Adapter<ImageAdapterR.ImageHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(LayoutInflater.from(mContext).inflate(R.layout.image_items, parent,false))
    }

    override fun getItemCount(): Int {
        return mThumbIds.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        requestManager
                .load(mThumbIds.get(position))
                .into(holder.imageView)
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.image_item)
    }


}