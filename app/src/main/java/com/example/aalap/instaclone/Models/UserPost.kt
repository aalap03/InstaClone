package com.example.aalap.instaclone.Models

data class UserPost(var postId: String,
                    var caption:String,
                    var postImage: String,
                    var user: User)

{
    constructor() : this("", "", "", User())

}