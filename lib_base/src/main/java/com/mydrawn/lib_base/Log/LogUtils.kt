package com.mydrawn.lib_base.Log

import android.util.Log
import com.mydrawn.lib_base.BuildConfig
import com.mydrawn.lib_network.NetLogUtil

/**
 * Author:drawn
 * Description: 负责日志的打印
 * date:2020/12/14
 */
class LogUtils {
    companion object {
        const val LOG_TAG = "ARDF_"
        const val LOG_TAG_NETWORK = NetLogUtil.LOG_TAG //网络日志的log
        var isDebug = BuildConfig.BUILD_TYPE == "debug"


        fun w(info: String?) {
            if (isDebug) Log.w(LOG_TAG, getLogInfo() + "$info")
        }

        fun w(tag: String, info: String?) {
            if (isDebug) Log.w(LOG_TAG, getLogInfo() + "$tag : $info")
        }

        fun d(info: String?) {
            if (isDebug) Log.d(LOG_TAG, getLogInfo() + "$info")
        }

        fun d(tag: String, info: String?) {
            if (isDebug) Log.d(LOG_TAG, getLogInfo() + "$tag : $info")
        }

        fun i(info: String?) {
            if (isDebug) Log.i(LOG_TAG, getLogInfo() + "$info")
        }

        fun i(tag: String, info: String?) {
            if (isDebug) Log.i(LOG_TAG, getLogInfo() + "$tag : $info")
        }

        fun e(info: String?) {
            if (isDebug) Log.e(LOG_TAG, getLogInfo() + "$info")
        }

        fun e(tag: String, info: String?) {
            if (isDebug) Log.e(LOG_TAG, getLogInfo() + "$tag : $info")
        }


        /**
         * 获取行号和类名
         */
        private fun getLogInfo(): String {
            var el = Throwable().stackTrace[2]
            var lineNumber = el?.lineNumber ?: ""
            var className = el?.className ?: ""
            if (className.contains(".")) {
                var strs = className.split('.')
                className = strs[strs.size - 1]
            }
            return "$className:line-${lineNumber}<->: "
        }
    }
}