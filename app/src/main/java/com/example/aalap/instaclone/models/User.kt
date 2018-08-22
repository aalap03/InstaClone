package com.example.aalap.instaclone.models

data class User(var name:String,
                var email: String,
                var id: String,
                var profilePic: String)

{
    constructor() : this("", "", "", "")
}