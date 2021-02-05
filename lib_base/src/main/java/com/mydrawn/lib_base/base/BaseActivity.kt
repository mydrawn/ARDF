package com.mydrawn.lib_base.base

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.SizeUtils
import com.mydrawn.lib_base.Log.ArdfLog
import com.mydrawn.lib_base.eventBus.RxBus
import com.mydrawn.lib_base.eventBus.RxEvent
import com.permissionx.guolindev.PermissionX
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.jessyan.autosize.internal.CustomAdapt

/**
 * Author:drawn
 * Description: activity 基类
 *      1.基于RxJava的实践总线封装
 *      2.资源释放封装
 *      3.屏幕常量封装
 * Date:2020/12/15
 */
abstract class BaseActivity : AppCompatActivity(), IActivity, CustomAdapt {

    override fun onCreate(savedInstanceState: Bundle?) {
        ArdfLog.d(this.javaClass.simpleName + ": onCreate")
        super.onCreate(savedInstanceState)
        if (keepScreenOn()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
    }

    override fun onStart() {
        ArdfLog.d(this.javaClass.simpleName + ": onStart")
        super.onStart()
    }

    override fun onRestart() {
        ArdfLog.d(this.javaClass.simpleName + ": onRestart")
        super.onRestart()
    }

    override fun onResume() {
        ArdfLog.d(this.javaClass.simpleName + ": onResume")
        super.onResume()
    }

    override fun onPause() {
        ArdfLog.d(this.javaClass.simpleName + ": onPause")
        super.onPause()
    }

    override fun onStop() {
        ArdfLog.d(this.javaClass.simpleName + ": onStop")
        super.onStop()
        if (isFinishing) {
            release()
        }
    }

    override fun onDestroy() {
        ArdfLog.d(this.javaClass.simpleName + ": onDestroy")
        super.onDestroy()
        release()
    }

    /**
     * ***********************事件总线封装****************************
     */
    private var mCompositeDisposable: CompositeDisposable? = null

    /**
     * 注册事件总线
     */
    fun registerRxEvent() {
        if (null == mCompositeDisposable) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(
            RxBus.toFlowable(RxEvent::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { rxEvent: RxEvent<*> ->
                    onRxEventHandle(rxEvent)
                })
    }

    /**
     * 反注册事件总线
     */
    fun unregisterRxEvent() {
        if (null != mCompositeDisposable) {
            mCompositeDisposable!!.clear()
        }
    }


    /**
     * 发送通信事件
     */
    override fun postRxEvent(rxEvent: RxEvent<*>) {
        RxBus.post(rxEvent)
    }

    /**
     * 接收通信事件
     * 注意要注册事件，取消注册
     */
    open fun onRxEventHandle(rxEvent: RxEvent<*>) {}
    //***********************事件总线封装 end**************************


    /**
     * 是否屏幕常亮
     */
    open fun keepScreenOn(): Boolean {
        return false
    }

    /**
     * ***********************资源释放封装****************************
     */
    private var mIsRelease = false
    override fun release() {
        if (!mIsRelease) {
            unregisterRxEvent()
            releaseResources()
            mIsRelease = true
        }
    }

    /**
     * 释放资源
     */
    abstract fun releaseResources()
    //***********************资源释放封装****************************

    /**
     * ***********************屏幕适配封装****************************
     */
    override fun isBaseOnWidth(): Boolean {
        //默认依据宽度百分比适配
        return true
    }

    //默认依据当前屏幕尺寸，实际需要根据UI设计尺寸填写
    override fun getSizeInDp(): Float {
        return SizeUtils.px2dp(ApplicationProxy.mScreenWidth.toFloat()).toFloat()
    }
    //***********************资源释放封装****************************
}