package com.mydrawn.lib_base.base

import com.mydrawn.lib_base.eventBus.RxEvent


/**
 * Author:drawn
 * Description: activity公共方法,
 * date:2020/12/15
 */
interface IActivity {

    /**
     * 释放资源
     */
    fun release()

    /**
     * 发送事件
     */
    fun postRxEvent(rxEvent: RxEvent<*>)

}