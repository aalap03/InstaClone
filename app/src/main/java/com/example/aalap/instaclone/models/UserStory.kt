package com.example.aalap.instaclone.models

data class UserStory(var postId: String,
                     var caption:String,
                     var storyUri: String,
                     var postTime: String,
                     var user: User)

{
    constructor() : this("", "","", "", User())

}