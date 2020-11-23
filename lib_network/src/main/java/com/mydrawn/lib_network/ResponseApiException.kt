package com.mydrawn.lib_network

/**
 * Author:drawn
 * Description: 响应code异常 主要是服务器响应非成功的响应code
 * date:2020/11/23
 */
class ResponseApiException(
    private val errorCode: Int, //服务器返回的错误code
    private val msg: String? //服务器返回的错误信息
) :
    RuntimeException(msg) {

    fun getErrorCode(): Int {
        return errorCode
    }

    fun getErrorMsg(): String? {
        return msg
    }
}