package com.example.aalap.instaclone.account

import android.content.Context
import android.content.SharedPreferences

val USERNAME = "username"
val PASSWORD = "password"
val SAVE_CREDS = "save_creds"

class Preference(appContext: Context) {

    val preference : SharedPreferences = appContext.getSharedPreferences("FirebaseApp", Context.MODE_PRIVATE)

    fun setUserName(userName: String) {
        preference.edit().putString(USERNAME, userName).apply()
    }

    fun getUserName() : String{
        return preference.getString(USERNAME, "")
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
}