package com.mydrawn.lib_base.eventBus

/**
 * Author:drawn
 * Description:发送的消息类型定义
 * Date:2020/12/15
 */
enum class RxEventType {
    TEST, //测试
    BLE,
    BLE_DATA,
    WEB_SOCKET_MSG_REV, //接收到服务器消息
    WEB_SOCKET_MSG_SEND, //发送消息到服务器
    NETWORK_ERROR, //网络连异常
    REFRESH_WEB_VIEW, //刷新webView
    NFC_DATA//debug 跳转
}