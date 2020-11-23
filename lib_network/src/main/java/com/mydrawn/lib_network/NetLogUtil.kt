package com.mydrawn.lib_network

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Author:drawn
 * Description: 管理网络请求日志打印
 *      打印格式化的Response json字符串log，方便查看日志调试
 * date:2020/11/20
 */
object NetLogUtil {

    const val DEBUG = NetWorkConfigs.DEBUG
    const val LOG_TAG = NetWorkConfigs.LOG_TAG
    val LINE_SEPARATOR = System.getProperty("line.separator")

    /**
     * tag:日志tag
     * jsonString:json字符串
     * headString：
     */
    fun printJson(
        jsonString: String,
        headString: String
    ) {
        if (!DEBUG) {
            return
        }
        var message: String
        message = try {
            when {
                jsonString.startsWith("{") -> {
                    val jsonObject = JSONObject(jsonString)
                    jsonObject.toString(4) //最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
                }
                jsonString.startsWith("[") -> {
                    val jsonArray = JSONArray(jsonString)
                    jsonArray.toString(4)
                }
                else -> {
                    jsonString
                }
            }
        } catch (e: JSONException) {
            jsonString
        }
        printLine(true)
        message = headString + LINE_SEPARATOR.toString() + message
        val lines: Array<String> = message.split(LINE_SEPARATOR).toTypedArray()
        for (line in lines) {
            d("║ $line")
        }
        printLine(false)
    }

    private fun printLine(isTop: Boolean) {
        if (isTop) {
            d(
                "╔═══════════════════════════════════════════════════════════════════════════════════════"
            )
        } else {
            d(
                "╚═══════════════════════════════════════════════════════════════════════════════════════"
            )
        }
    }


    fun d(msg: String) {
        if (DEBUG) {
            Log.d(LOG_TAG, msg)
        }
    }

    fun e(msg: String) {
        if (DEBUG) {
            Log.e(LOG_TAG, msg)
        }
    }
}