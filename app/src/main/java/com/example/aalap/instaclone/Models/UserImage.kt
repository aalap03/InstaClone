package com.example.aalap.instaclone.Models

import android.net.Uri

data class UserImage(var userId:String,
                     var imageUrl: String)

{
    constructor() : this("", "")
}