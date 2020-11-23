package com.mydrawn.lib_network.bean

/**
 * Author:drawn
 * Description: Response 响应封装父类对象，需要和后台约定格式，这是是通用格式，实际依据业务需求
 * date:2020/11/20
 */
data class BaseResponse<T>(
    var code: String, //请求响应码
    var msg: String, // 响应码描述
    var `data`: T // 请求Json 实体bean对象
) {
    override fun toString(): String {
        return "LoginInfo(code='$code', `data`=${`data`.toString()}, msg='$msg')"
    }
}