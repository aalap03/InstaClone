package com.example.aalap.instaclone.daggers

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(var context: Context) {

    @Provides fun context(): Context{
        return context
    }
}