package com.mydrawn.lib_network.selfRxjava

import android.net.ParseException
import com.google.gson.JsonSyntaxException
import com.mydrawn.lib_network.NetLogUtil
import com.mydrawn.lib_network.ResponseApiException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


/**
 * Author:drawn
 * Description: 自定义的 rxjava Observer
 *  在开启网络请求前，做一些网络检查工作
 *  接收到数据后，统一处理一些异常
 * date:2020/11/20
 */
open class NetWorkObserver<T> : Observer<T?> {

    override fun onNext(value: T) {}

    override fun onError(e: Throwable) {
        //ResponseApiException 是主动抛出的响应code异常
        if (e is ResponseApiException) {
            //针对 ApiException 的特殊处理 部分请求失败可能需要Toast提示用户
            NetLogUtil.e(
                "NetRequest error, ErrorCode = " + e.getErrorCode() + ";  msg = " + e.message
            )
        } else {
            NetLogUtil.e("NetRequest error, ErrorCode =" + getThrowableMsg(e))
        }
    }

    override fun onSubscribe(d: Disposable) {
        //请求开始之前，做一些准备工作，网络检查、wifi连接检查等
        //无法确定当前代码运行在什么线程的时候，不要放UI的相关操作
    }

    override fun onComplete() {}

    private fun getThrowableMsg(t: Throwable): String {
        if (t is UnknownHostException) { //网络异常
            return "当前无网络，请检查你的网络设置"
        } else {
            return if (t is SocketTimeoutException  //okhttp全局设置超时
                || t is TimeoutException     //rxjava中的timeout方法超时
//                || t is TimeoutCancellationException  //协程超时
            ) {
                "连接超时,请稍后再试"
            } else if (t is ConnectException) {
                "网络不给力，请稍候重试！"
            } else if (t is JsonSyntaxException) {
                "Json语法 数据解析失败,请检查数据是否正确"
            } else if (t is ParseException) {
                "请求成功，数据不正确"
            } else {
                "请求失败，请稍后再试"
            }
        }
    }
}
