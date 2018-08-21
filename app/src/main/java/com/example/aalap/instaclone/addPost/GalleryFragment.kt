package com.example.aalap.instaclone.addPost

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.aalap.instaclone.R
import org.jetbrains.anko.AnkoLogger
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.support.v4.app.ActivityCompat
import com.bumptech.glide.request.RequestOptions
import com.example.aalap.instaclone.account.ImageAdapter
import kotlinx.android.synthetic.main.layout_camera_fragment.*
import kotlinx.android.synthetic.main.layout_gallery_fragment.*

val PERM_CODE = 1
class GalleryFragment : Fragment(), AnkoLogger, ImageAdapter.CallBack {

    var selectedImagePath = ""
    override fun deliverImage(imagePath: Any) {

        Glide.with(this)
                .load(imagePath as String)
                .into(gallery_selected_image)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.layout_gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    , PERM_CODE)
        else
            requestImages()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERM_CODE) {

            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                requestImages()
            }else
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissions[0])
        }
    }

    fun requestImages() {
        val requestManager = Glide
                .with(this)
                .applyDefaultRequestOptions(RequestOptions()
                        .placeholder(R.mipmap.ic_launcher_round)
                )

        val allImages = getAllShownImagesPath(requireActivity())
        val imageAdapter = ImageAdapter(requireContext(), allImages.toMutableList(), requestManager, this@GalleryFragment)
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