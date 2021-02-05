package com.mydrawn.lib_base.base

import android.app.Application
import com.mydrawn.lib_base.eventBus.RxEvent


/**
 * Author:drawn
 * Description: activity公共方法,
 * date:2020/12/15
 */
interface IModuleApplication {

    fun onCreate(application: Application)

    fun onTrimMemory(application: Application, level: Int)

    fun attachBaseContext(baseApplication: Application)
}