package com.example.aalap.instaclone

import android.content.Context
import android.content.SharedPreferences

val USERNAME = "username"
val PASSWORD = "password"
val SAVE_CREDS = "save_creds"
val PROFILE_PIC = "profile_pic"
val USER_ID = "user_id"

class Preference(appContext: Context) {

    val preference : SharedPreferences = appContext.getSharedPreferences("FirebaseApp", Context.MODE_PRIVATE)

    fun setUserName(userName: String) {
        preference.edit().putString(USERNAME, userName).apply()
    }

    fun getUserName() : String{
        return preference.getString(USERNAME, "")
    }

    fun setProfilePic(imageUrl: String){
        preference.edit().putString(PROFILE_PIC, imageUrl).apply()
    }

    fun getProfilePic():String{
        return preference.getString(PROFILE_PIC, "")
    }

    fun setPassword(password: String) {
        preference.edit().putString(PASSWORD, password).apply()
    }

    fun getPassword(): String {
        return preference.getString(PASSWORD, "")
    }

    fun saveCredentials(isSave : Boolean) {
        preference.edit().putBoolean(SAVE_CREDS, true).apply()
    }

    fun isSaveCreds(): Boolean {
        return preference.getBoolean(SAVE_CREDS, false)
    }

    fun setUserId(userId: String) {
        preference.edit().putString(USER_ID, userId).apply()
    }

    fun getUserId():String{
        return preference.getString(USER_ID, "")
    }
}