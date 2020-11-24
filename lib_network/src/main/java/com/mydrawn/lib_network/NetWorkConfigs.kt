package com.mydrawn.lib_network

import android.content.SharedPreferences
import com.mydrawn.lib_network.interceptor.HeadUrlInterceptor

/**
 * Author:drawn
 * Description: 放置请求地址
 * date:2020/11/20
 */
object NetWorkConfigs {

    const val SP_KEY= "NetWork_SP" //SharedPreferences key名
    const val SP_KEY_TOKEN= "SP_KEY_TOKEN" //储存token 的key值

    /**
     * log 配置
     * @see NetLogUtil.DEBUG
     */
    const val DEBUG: Boolean = true //是否打印网络请求日志
    const val LOG_TAG = "ARDF_NetWork" //日志标签

    /**
     * okHttp 连接设置
     * @see NetWorkManager.initOkHttpClient
     */
    const val connectTimeout = 30L //连接超时时间 单位s
    const val readTimeout = 30L //读取超时时间 单位s
    const val writeTimeout = 30L //写超时时间 单位 s
    const val retryOnConnectionFailure = true //是否失败重连

    /**
     * retrofit设置
     * @see NetWorkManager.initRetrofit
     */
    const val BASE_DEV_URL: String = "http://47.100.245.128/"//开发环境地址
    const val baseTestUrl: String = "XXXXX"//测试环境地址
    const val basePreUrl: String = "XXXXX"//预发布环境地址
    const val baseProductUrl: String = "XXXXX"//生产环境地址

    //服务器根地址
    var baseUrl: String = BASE_DEV_URL //建议依据打包方式不同，使用build config值来赋值给url

    /**
     * 请求头设置
     * @see HeadUrlInterceptor
     */
    const val Content_Type = "application/json; charset=UTF-8"
    const val Vary = "Accept-Encoding"
    const val Server = "Apache"
    const val Pragma =
        "no-cache" //HTTP/1.0 caches协议没有实现Cache-Control（在http1.1实现）等同于http1.1 Cache-Control
    var Authorization = "" //token值，在登录获取了token后动态设置，然后会被添加到请求头中
}