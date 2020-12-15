package com.mydrawn.lib_base.base

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.mydrawn.lib_base.Log.LogUtils
import com.mydrawn.lib_base.LogUtilsUtils.CrashHandler

/**
 * Author:drawn
 * Description: Application 公共方法类,实际业务实现
 * date:2020/12/14
 */
object ApplicationProxy {

    fun onCreate(application: Application) {
        LogUtils.d("onCreate")
        //初始化异常处理
        CrashHandler.instance.init(application)

        if (LogUtils.isDebug) {// 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化
    }

    fun onTrimMemory(application: Application, level: Int) {
        when (level) {
            //并且该进程在后台进程列表最后一个,马上就要被清理
            MultiDexApplication.TRIM_MEMORY_COMPLETE -> LogUtils.d("TRIM_MEMORY_COMPLETE")

            //并且该进程在后台进程列表的中部。
            MultiDexApplication.TRIM_MEMORY_MODERATE -> LogUtils.d("TRIM_MEMORY_COMPLETE")

            //并且该进程是后台进程。
            MultiDexApplication.TRIM_MEMORY_BACKGROUND -> LogUtils.d("TRIM_MEMORY_COMPLETE")

            //并且该进程的UI已经不可见了。
            MultiDexApplication.TRIM_MEMORY_UI_HIDDEN -> LogUtils.d("TRIM_MEMORY_COMPLETE")

            //后台进程不足3个，并且该进程优先级比较高，需要清理内存
            MultiDexApplication.TRIM_MEMORY_RUNNING_CRITICAL -> LogUtils.d("TRIM_MEMORY_COMPLETE")

            //后台进程不足5个，并且该进程优先级比较高，需要清理内存
            MultiDexApplication.TRIM_MEMORY_RUNNING_LOW -> LogUtils.d("TRIM_MEMORY_COMPLETE")

            //后台进程不足5个，并且该进程优先级比较高，需要清理内存
            MultiDexApplication.TRIM_MEMORY_RUNNING_MODERATE -> LogUtils.d("TRIM_MEMORY_COMPLETE")
        }
    }

    fun attachBaseContext(baseApplication: Application) {
        LogUtils.d("attachBaseContext")
    }
}