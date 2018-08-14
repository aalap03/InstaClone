package com.example.aalap.instaclone.Models

data class User(var name:String,
                var email: String,
                var id: String)

{
    constructor() : this("", "", "")
}