package com.mydrawn.lib_base.base

import android.app.Application
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
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

        getScreenWH(application)
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


    /**
     * 获取屏幕宽高，autoSize 屏幕适配
     */
    var mScreenWidth = 0
    var mScreenHeight = 0
    fun getScreenWH(application: Application) {
        var windowManager = (application.getSystemService(Context.WINDOW_SERVICE)) as WindowManager
        val outPoint = Point()
        when {
            Build.VERSION.SDK_INT < 19 -> {
                // 不可能有虚拟按键
                windowManager.defaultDisplay.getSize(outPoint)
                mScreenWidth = outPoint.x
                mScreenHeight = outPoint.y
            }
            Build.VERSION.SDK_INT < 30 -> {
                // 可能有虚拟按键的情况
                windowManager.defaultDisplay.getRealSize(outPoint)
                mScreenWidth = outPoint.x
                mScreenHeight = outPoint.y
            }
            else -> {
                mScreenWidth = windowManager.currentWindowMetrics.bounds.width()
                mScreenHeight = windowManager.currentWindowMetrics.bounds.height()
            }
        }
        LogUtils.d("width = $mScreenWidth height = $mScreenHeight")
        if (mScreenWidth == 0 || mScreenHeight == 0) {
            mScreenWidth = 320
            mScreenHeight = 640
            LogUtils.e("屏幕宽高获取异常，设置默认值 width = $mScreenWidth height = $mScreenHeight")
        }
    }
}