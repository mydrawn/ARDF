package com.mydrawn.lib_base.eventBus

/**
 * Author:drawn
 * Description:RX bus 进行组件间的通信实体类
 * date:2020/12/15
 */
class RxEvent<T>(var eventType: RxEventType, var arg: T?) {}