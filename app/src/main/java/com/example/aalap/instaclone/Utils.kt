package com.example.aalap.instaclone

import android.content.Context
import android.os.Environment
import android.text.format.DateUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        fun formatDate(postTime: String): String {
            var simpleDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
            val parse = simpleDateFormat.parse(postTime)

            return DateUtils.getRelativeTimeSpanString(parse.getTime(), Date().time, DateUtils.SECOND_IN_MILLIS).toString();
        }

         fun createImageFile(context:Context, type:String): File {
            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "File_" + timeStamp + "_"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(
                    imageFileName, /* prefix */
                    type, /* suffix */
                    storageDir      /* directory */
            )
            return image
        }
    }

}