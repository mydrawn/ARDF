package com.mydrawn.lib_base.eventBus

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


/**
 * Author:drawn
 * Description:基于Rxjava 事件总线
 * date:2020/12/15
 */
object RxBus {

    // 支持背压且线程安全的，保证线程安全需要调用 toSerialized() 方法
    fun toFlowable() = mBus
    fun hasSubscribers() = mBus.hasSubscribers()
    private val mBus: FlowableProcessor<Any> by lazy {
        PublishProcessor.create<Any>().toSerialized()
    }
    //发送事件
    fun post(obj: RxEvent<*>) {
        mBus.onNext(obj)
    }
    //订阅事件
    fun <T> toFlowable(tClass: Class<T>) = mBus.ofType(tClass)


    //不支持背压且线程安全的，保证线程安全需要调用 toSerialized() 方法
    fun toObservable(): Observable<Any> = mBusNB
    fun hasObservers() = mBusNB.hasObservers()
    private val mBusNB: Subject<Any>
            by lazy {
                PublishSubject.create<Any>().toSerialized()
            }
    //发送事件
    fun postNB(obj: Any) {
        mBusNB.onNext(obj)
    }
    //订阅事件
    fun <T> toObservable(tClass: Class<T>): Observable<T> = mBusNB.ofType(tClass)


    /**
     * 注册事件总线 背压且线程安全
     */
    fun registerRxEvent(rxEventHandle: RxEventHandle): CompositeDisposable {
        var compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            RxBus.toFlowable(RxEvent::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { rxEvent: RxEvent<*> ->
                    rxEventHandle.onRxEventHandle(rxEvent)
                })
        return compositeDisposable
    }

    interface RxEventHandle {
        fun onRxEventHandle(rxEvent: RxEvent<*>)
    }

    /**
     * 反注册事件总线
     */
    fun unregisterRxEvent(compositeDisposable: CompositeDisposable) {
        compositeDisposable?.clear()
    }
}
