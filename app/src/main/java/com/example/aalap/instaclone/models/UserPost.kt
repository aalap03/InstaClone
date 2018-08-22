package com.example.aalap.instaclone.models

data class UserPost(var postId: String,
                    var caption:String,
                    var postImage: String,
                    var postTime: String,
                    var user: User)

{
    constructor() : this("", "","", "", User())

}