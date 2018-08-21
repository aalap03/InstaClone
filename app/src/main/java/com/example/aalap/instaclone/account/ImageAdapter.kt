package com.example.aalap.instaclone.account

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.example.aalap.instaclone.Models.UserPost
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import kotlin.coroutines.experimental.coroutineContext

class ImageAdapter(private val mContext: Context, var list: MutableList<Any>, var requestManager: RequestManager, var callBack: CallBack)
    : BaseAdapter(), AnkoLogger {

    override fun getCount(): Int = list.size

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

        val any = list[position]
        if (any is UserPost) {
            info { "adapter: userPost:" }
            requestManager.load(any.postImage)
                    .into(imageView)
        } else {
            info { "adapter: strings:" }
            requestManager.load(any)
                    .into(imageView)
        }

        if (convertView != null)
            convertView.setOnClickListener { callBack.deliverImage(list[position]) }
        else
            Toast.makeText(mContext, "Null bro..!!", Toast.LENGTH_SHORT)
                    .show()

        return imageView
    }

    interface CallBack {
        fun deliverImage(imagePath: Any)
    }
}