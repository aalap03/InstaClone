package com.example.aalap.instaclone.add

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.layout_center_home_fragment.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.aalap.instaclone.account.ImageAdapter
import kotlinx.android.synthetic.main.layout_account.*
import kotlinx.android.synthetic.main.layout_gallery_fragment.*


class GalleryFragment : Fragment(), AnkoLogger, ImageAdapter.CallBack {

    var selectedImagePath = ""
    override fun deliverImage(imagePath: String) {
        selectedImagePath = imagePath

        Glide.with(this)
                .load(selectedImagePath)
                .into(gallery_selected_image)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.layout_gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var requestManager = Glide
                .with(this)
                .applyDefaultRequestOptions(RequestOptions()
                        .placeholder(R.mipmap.ic_launcher_round)
                )

        var allImages = getAllShownImagesPath(requireActivity())
        var imageAdapter = ImageAdapter(requireContext(), allImages, requestManager, this@GalleryFragment)
        gallery_recycler.adapter = imageAdapter
        deliverImage(allImages[0])
    }

    private fun getAllShownImagesPath(activity: Activity): MutableList<String> {
        val uri: Uri
        var cursor: Cursor? = null
        val column_index_data: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null
        try{

            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val projection = arrayOf(MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            cursor = activity.contentResolver.query(uri, projection, null, null, null)

            column_index_data = cursor!!.getColumnIndexOrThrow(MediaColumns.DATA)
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data)

                listOfAllImages.add(absolutePathOfImage)
            }

        } catch(e:Exception) {
            e.printStackTrace()
        }finally {
            cursor?.close()
        }

        return listOfAllImages.asReversed()
    }

}