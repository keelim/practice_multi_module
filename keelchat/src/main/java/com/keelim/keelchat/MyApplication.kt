package com.keelim.keelchat

import android.app.Application
import com.keelim.commonlibrary.AppManager

class MyApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        AppManager.appType = AppManager.AppType.keelchat
    }
}