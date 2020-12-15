package com.mydrawn.lib_base.base

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.mydrawn.lib_base.Log.LogUtils
import com.mydrawn.lib_base.eventBus.RxBus
import com.mydrawn.lib_base.eventBus.RxEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Author:drawn
 * Description: activity 基类
 *      1.基于RxJava的实践总线封装
 *      2.资源释放封装
 *      3.屏幕常量封装
 * Date:2020/12/15
 */
abstract class BaseActivity : AppCompatActivity(), IActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtils.d(this.javaClass.simpleName + ": onCreate")
        super.onCreate(savedInstanceState)
        if (keepScreenOn()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
    }

    override fun onStart() {
        LogUtils.d(this.javaClass.simpleName + ": onStart")
        super.onStart()
    }

    override fun onRestart() {
        LogUtils.d(this.javaClass.simpleName + ": onRestart")
        super.onRestart()
    }

    override fun onResume() {
        LogUtils.d(this.javaClass.simpleName + ": onResume")
        super.onResume()
    }

    override fun onPause() {
        LogUtils.d(this.javaClass.simpleName + ": onPause")
        super.onPause()
    }

    override fun onStop() {
        LogUtils.d(this.javaClass.simpleName + ": onStop")
        super.onStop()
        if (isFinishing) {
            release()
        }
    }

    override fun onDestroy() {
        LogUtils.d(this.javaClass.simpleName + ": onDestroy")
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
}