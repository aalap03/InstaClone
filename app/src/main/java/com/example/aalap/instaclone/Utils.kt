package com.example.aalap.instaclone

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        fun formatDate(postTime: String): String {
            var simpleDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
            val parse = simpleDateFormat.parse(postTime)

            return DateUtils.getRelativeTimeSpanString(parse.getTime(), Date().time, DateUtils.SECOND_IN_MILLIS).toString();
        }
    }

}