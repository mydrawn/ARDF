package com.mydrawn.lib_network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Author:drawn
 * Description: 设置请求请求的公共参数，需要与后台协商需要哪些值，下面只是试例
 * Date:2020/11/20
 */
class CommonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val authorizedUrlBuilder = oldRequest.url
            .newBuilder()
            .scheme(oldRequest.url.scheme)
            .host(oldRequest.url.host)
//            .addQueryParameter("deviceType", "")
//            .addQueryParameter("bundleId", "")
//            .addQueryParameter("deviceModel", "")
//            .addQueryParameter("deviceId", "")
//            .addQueryParameter("osType", "1")
//            .addQueryParameter("osVersion", "")
//            .addQueryParameter("timestamp", "" + System.currentTimeMillis())
//            .addQueryParameter("appVersion", "")
        val newRequest = oldRequest.newBuilder()
            .method(oldRequest.method, oldRequest.body)
            .url(authorizedUrlBuilder.build())
            .build()
        return chain.proceed(newRequest)
    }
}