package com.example.aalap.instaclone

import android.app.Application
import com.example.aalap.instaclone.daggers.AppModule
import com.example.aalap.instaclone.daggers.DaggerComponent
import com.example.aalap.instaclone.daggers.DaggerDaggerComponent

class App: Application() {

    companion object Factory {
        fun create(): App = App()
    }

    val component: DaggerComponent by lazy {
        DaggerDaggerComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}