package com.mydrawn.lib_base.base

import android.app.Application
import android.content.Context

/**
 * Author:drawn
 * Description: Application基类
 * date:2020/12/14
 */
open class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        ApplicationProxy.onCreate(this)
    }


    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        ApplicationProxy.onTrimMemory(this, level)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        ApplicationProxy.attachBaseContext(this)
    }

}