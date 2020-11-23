package com.mydrawn.lib_network.interceptor

import android.util.Log
import com.mydrawn.lib_network.NetLogUtil
import com.mydrawn.lib_network.NetWorkConfigs
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.lang.Exception
import java.lang.String

/**
 * Author:drawn
 * Description: 打印请求信息方便调试 设置公共的请求头，
 * Date:2020/11/23
 */
class HeadUrlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        NetLogUtil.d(
            request.method + " url = " + request.url
        )
        //打印请求提日志
        try {
            request.body?.let {
                val buffer = Buffer()
                it.writeTo(buffer)
                buffer.readByteString().utf8().let {
                    if (!it.isNullOrEmpty()) {
                        NetLogUtil.d("request body = $it")
                    }
                }
            }
        } catch (e: Exception) {
            NetLogUtil.e("printf request.body error")
        }

        var bulider: Request.Builder = request.newBuilder()
            .addHeader("Content-Type", NetWorkConfigs.Content_Type)
            .addHeader("Vary", NetWorkConfigs.Vary)
            .addHeader("Server", NetWorkConfigs.Server)
            .addHeader("Pragma", NetWorkConfigs.Pragma)
        if (!NetWorkConfigs.Authorization.isNullOrEmpty()) {
            bulider.addHeader("Authorization", NetWorkConfigs.Authorization) //添加请求token
        }
        bulider.build()

        val startMs = System.currentTimeMillis()
        //执行请求
        val response = chain.proceed(request)
        //打印响应时间日志
        NetLogUtil.d("spendMs = ${System.currentTimeMillis() - startMs}ms")
        return response
    }
}