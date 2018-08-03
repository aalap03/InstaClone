package com.example.aalap.instaclone.daggers

import com.example.aalap.instaclone.App
import com.example.aalap.instaclone.BaseActivity
import dagger.Component

@Component(modules = [(AppModule::class)])
interface DaggerComponent{

    fun inject(baseActivity: BaseActivity)
    fun inject(app: App)
}