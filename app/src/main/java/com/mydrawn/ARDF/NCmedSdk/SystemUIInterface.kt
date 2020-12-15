package com.mydrawn.ARDF.NCmedSdk

import android.content.Context
import android.webkit.JavascriptInterface
import com.just.agentweb.AgentWeb

/**
 * Author:drawn
 * Description:系统UI相关Js接口
 * date:2020/12/14
 */
class SystemUIInterface constructor(var mAgent: AgentWeb, var mContext: Context) {

    @JavascriptInterface
    fun postMessage() {
    }
}