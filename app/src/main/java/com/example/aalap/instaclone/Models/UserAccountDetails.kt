package com.example.aalap.instaclone.Models

data class UserAccountDetails(var descr: String,
                              var username: String,
                              var displayName: String,
                              var phone: String,
                              var profilePic: String,
                              var website: String,
                              var followers: Int,
                              var following: Int,
                              var posts: Int)
{

    constructor():
            this("",
                    "",
                    "",
                    "",
                    "",
                    "" ,
                    -1,
                    -1,
                    -1)
}
