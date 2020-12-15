package com.mydrawn.lib_base.LogUtilsUtils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Process
import android.widget.Toast
import com.mydrawn.lib_base.Log.LogUtils
import com.mydrawn.lib_network.NetWorkManager
import com.permissionx.guolindev.PermissionX
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Author:drawn
 * Description: 负责异常收集和存储
 * date:2020/12/14
 */
class CrashHandler : Thread.UncaughtExceptionHandler {

    companion object {
        val instance: CrashHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CrashHandler()
        }

        private const val TAG = "CrashHandler"

        //异常日志文件存储路径
        private val PATH = Environment.getExternalStorageDirectory()
            .path + File.separator.toString() + "crash"
        private const val FILE_NAME = "crash_"
        private const val FILE_NAME_SUFFIX = ".trace"
        private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler? = null

    }

    private var mContext: Context? = null
    fun init(context: Context) {
        mDefaultCrashHandler =
            Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        mContext = context.applicationContext
    }

    override fun uncaughtException(
        thread: Thread,
        ex: Throwable
    ) {
        try {
            //将文件写入sd卡
            writeToSDCard(ex)
            //写入后在这里可以进行上传操作
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.e(TAG,"exception writeToSDCard error")
        }
        //如果系统提供了默认异常处理就交给系统进行处理，否则自己进行处理。
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler?.uncaughtException(thread, ex)
        } else {
            ex.printStackTrace()
            Process.killProcess(Process.myPid())
        }
    }

    //将异常写入文件
    @Throws(IOException::class, PackageManager.NameNotFoundException::class)
    private fun writeToSDCard(ex: Throwable) {
        //如果没有SD卡，直接返回
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return
        }
        val fileDir = File(PATH)
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        val curTime = System.currentTimeMillis()
        val time: String =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(curTime))
        val exFile =
            File(PATH + File.separator.toString() + FILE_NAME + time + FILE_NAME_SUFFIX)
        val pw = PrintWriter(BufferedWriter(FileWriter(exFile)))
        LogUtils.e("Crash:" + exFile.absolutePath)
        pw.println(time)
        val pm = mContext!!.packageManager
        val pi =
            pm.getPackageInfo(mContext!!.packageName, PackageManager.GET_ACTIVITIES)
        //当前版本号
        pw.println("App Version:" + pi.versionName + "_" + pi.versionCode)
        //当前系统
        pw.println("OS version:" + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT)
        //制造商
        pw.println("Vendor:" + Build.MANUFACTURER)
        //手机型号
        pw.println("Model:" + Build.MODEL)
        //CPU架构
        pw.println("CPU ABI:" + Build.CPU_ABI)
        ex.printStackTrace(pw)
        pw.close()
    }
}
