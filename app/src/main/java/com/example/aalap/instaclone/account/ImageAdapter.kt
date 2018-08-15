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

class ImageAdapter(private val mContext: Context, var mThumbIds: List<String>, var requestManager: RequestManager, var callBack: CallBack) : BaseAdapter() {

    override fun getCount(): Int = mThumbIds.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(mContext)
            imageView.layoutParams = ViewGroup.LayoutParams(mContext.resources.displayMetrics.widthPixels / 4,
                    mContext.resources.displayMetrics.widthPixels / 4)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            imageView = convertView as ImageView
        }

        requestManager.load(mThumbIds.get(position))
                .into(imageView)

        convertView?.setOnClickListener{callBack.deliverImage(mThumbIds[position])}

        return imageView
    }

    interface CallBack{
        fun deliverImage(imagePath: String)
    }
}