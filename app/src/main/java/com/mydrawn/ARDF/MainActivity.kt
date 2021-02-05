package com.mydrawn.ARDF

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.mydrawn.lib_base.Log.ArdfLog
import com.mydrawn.lib_base.arouter.ARouterPath
import com.mydrawn.lib_base.base.BaseActivity
import com.mydrawn.lib_base.eventBus.RxEvent
import com.mydrawn.lib_base.eventBus.RxEventType
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = ARouterPath.MainActivity)
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startCheckPermission()
    }

    /**
     * 检查权限
     */
    private fun startCheckPermission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
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
                    ARouter.getInstance().build(ARouterPath.AppConfigActivity)
                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                    finish()
                } else {
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT)
                }
            }
    }
    fun btnClick1(view: View) {
//        var intent = Intent(this, BpBleReceiverService::class.java)
//        intent.putExtra(BpBleReceiverService.TAG, BpBleReceiverService.TAG_START_SCAN)
//        startService(intent)
//        textView.text = "发现设备："
//        var wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
//        ArdfLog.d( "open wifi ${wifiManager.setWifiEnabled(true)}")
    }


    fun btnClick2(view: View) {
//        var intent = Intent(this, BpBleReceiverService::class.java)
//        intent.putExtra(BpBleReceiverService.TAG, BpBleReceiverService.TAG_AUTO_DATA)
//        startService(intent)\
    }

    fun btnClick3(view: View) {
//        var intent = Intent(this, BpBleReceiverService::class.java)
//        intent.putExtra(BpBleReceiverService.TAG, BpBleReceiverService.TAG_CONNECT)
//        startService(intent)
    }

    fun btnClick4(view: View) {
//        var intent = Intent(this, BpBleReceiverService::class.java)
//        intent.putExtra(BpBleReceiverService.TAG, BpBleReceiverService.TAG_EXIT)
//        startService(intent)
//
//        textView.text = ""\
    }

    override fun onRxEventHandle(rxEvent: RxEvent<*>) {
        when (rxEvent.eventType) {
            RxEventType.BLE -> {
                textView.text = textView.text.toString() + "\n    " + rxEvent.arg.toString()
            }
            RxEventType.BLE_DATA -> {
                textView1.text = rxEvent.arg.toString()
            }
        }
    }

    override fun releaseResources() {

    }
}