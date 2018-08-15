package com.example.aalap.instaclone.add

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.layout_camera_fragment.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import android.support.v4.content.FileProvider
import com.bumptech.glide.Glide
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


val CAM_PERM = 1
val OPEN_CAM = 2

class CameraFragment : Fragment(), AnkoLogger {

    var mCurrentPhotoPath : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.layout_camera_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        open_camera.setOnClickListener { requestCamPermission() }
        setHasOptionsMenu(true)
    }

    private fun requestCamPermission() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAM_PERM)
        } else
            openCam()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAM_PERM) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCam()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        info { "result" }
        if(requestCode == OPEN_CAM) {

            if(resultCode == RESULT_OK){
                Glide.with(this).load(mCurrentPhotoPath).into(cam_image)
            }
        }
    }

    private fun openCam() {
        info { "openingCam" }
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile = createImageFile()

        val uriForFile = FileProvider.getUriForFile(requireContext(), "com.example.aalap.instaclone.fileprovider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
        startActivityForResult(intent, OPEN_CAM)
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

}