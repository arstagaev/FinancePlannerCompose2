package com.example.android

import android.app.Application
import com.example.common.PreferenceStorage

class App: Application() {


    override fun onCreate() {
        super.onCreate()
        PreferenceStorage.init(this)
    }
}