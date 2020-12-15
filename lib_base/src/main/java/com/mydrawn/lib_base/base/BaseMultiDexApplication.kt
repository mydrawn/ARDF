package com.mydrawn.lib_base.base

import androidx.multidex.MultiDexApplication

/**
 * Author:drawn
 * Description: MultiDex Application基类
 * date:2020/12/14
 */
class BaseMultiDexApplication : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
    }


}