package com.mydrawn.ARDF

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.just.agentweb.AgentWeb
import com.mydrawn.ARDF.BluetoothExample.BpBleReceiverService
import com.mydrawn.lib_base.Log.LogUtils
import com.mydrawn.lib_base.base.BaseActivity
import com.mydrawn.lib_base.eventBus.RxBus
import com.mydrawn.lib_base.eventBus.RxEvent
import com.mydrawn.lib_base.eventBus.RxEventType
import com.permissionx.guolindev.PermissionX


class MainActivity : BaseActivity() {

    private var mAgentWeb: AgentWeb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //注册事件总线
        registerRxEvent()
        //权限检查
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
                scope.showRequestReasonDialog(deniedList, "为了保证程序正常工作，请您同意以下权限申请", "我已明白")

            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                } else {
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT)
                }
            }

    }

    override fun releaseResources() {

    }


    override fun onResume() {
        super.onResume()
    }

    /**
     * 扫描设备
     */
    fun scan(view: View) {
//        var intent = Intent(this, BpBleReceiverService::class.java)
//        intent.putExtra(BpBleReceiverService.TAG, BpBleReceiverService.TAG_START_SCAN)
//        startService(intent)
        ARouter.getInstance().build("/test/GuideActivity").navigation()
    }


    /**
     *
     */
    fun getData(view: View) {
//        var intent = Intent(this, BpBleReceiverService::class.java)
//        intent.putExtra(BpBleReceiverService.TAG, BpBleReceiverService.TAG_AUTO_DATA)
//        startService(intent)
        postRxEvent(RxEvent(RxEventType.TEST,"onResume"))
    }


    /**
     * 连接设备
     */
    fun connect(view: View) {
        var intent = Intent(this, BpBleReceiverService::class.java)
        intent.putExtra(BpBleReceiverService.TAG, BpBleReceiverService.TAG_CONNECT)
        startService(intent)
    }


    /**
     * 断开设备连接
     */
    fun disConnect(view: View) {
        var intent = Intent(this, BpBleReceiverService::class.java)
        intent.putExtra(BpBleReceiverService.TAG, BpBleReceiverService.TAG_EXIT)
        startService(intent)
    }


    override fun onRxEventHandle(rxEvent: RxEvent<*>) {
        when (rxEvent.eventType) {
            RxEventType.TEST -> {
                LogUtils.d("onRxEventHandle")
            }
        }
    }
}