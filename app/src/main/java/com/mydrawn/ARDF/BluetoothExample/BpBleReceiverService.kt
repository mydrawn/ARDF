package com.mydrawn.ARDF.BluetoothExample

import android.app.Service
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mydrawn.lib_base.Log.LogUtils
import com.mydrawn.lib_base.eventBus.RxBus
import com.mydrawn.lib_base.eventBus.RxEvent
import com.mydrawn.lib_base.eventBus.RxEventType
import java.io.IOException
import java.util.*
import kotlin.collections.HashSet


/**
 * Author:drawn
 * Description:A&D UA-651BLE 血压测量设备 蓝牙对接服务
 *      获取血压计测量的数据，并已内部广播形式发出
 * date:2020/12/09
 */
class BpBleReceiverService : Service() {

    companion object {
        const val LOGE_TAG = LogUtils.LOG_TAG

        const val TAG = "BpBleReceiverService"
        const val TAG_EXIT = -1 //退出服务
        const val TAG_START_SCAN = 0 //开始扫描
        const val TAG_STOP_SCAN = 1  //停止扫描
        const val TAG_CONNECT = 2 //开始连接
        const val TAG_DIS_CONNECT = 3 //断开连接
        const val TAG_AUTO_DATA = 4 //直接连接获取数据

        const val STATE_NORMAL = 1000 //正常状态
        const val STATE_SCANNING = 1001 //扫描中
        const val STATE_SCAN_COMPLETE = 1002 //扫描完成发现目标设备

        const val ACTION_BP_DATA = "com.ncmed.bp.data" //获取到血压数据发送的内部广播 action
        const val INTENT_BUNDLE_KEY = "bp_data"//获取到血压数据发送数据 BUNDLE key
    }

    private var mBlueToothStateReceiver: BlueToothStateReceiver? = null //蓝牙开关广播
    private var isConnectedDevice = false //是否处于连接中
    private var tryToStartBLE = false //是否尝试开启蓝牙
    private var mCurStatus =
        STATE_NORMAL //当前扫描状态
    private var mCurStartTag: Int? = null //当前service 启动状态
    private var mBPDevice: BluetoothDevice? = null//血压计设备
    private var scanCallback: ScanCallback? = null//android 5.1回调
    private var mLeScanCallback: BluetoothAdapter.LeScanCallback? = null//android 5.1以上回调
    private var mBluetoothGatt: BluetoothGatt? = null //GATT连接
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private lateinit var mContext: Context

    private var mHandle = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                TAG_AUTO_DATA,
                TAG_START_SCAN -> {
                    startScan()
                }
                TAG_STOP_SCAN -> {
                    stopScan(mCurStatus == STATE_SCAN_COMPLETE)
                }
                TAG_CONNECT -> {
                    connectBpDevices()
                }
                TAG_DIS_CONNECT -> {
                    disConnect()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LOGE_TAG, "BpBleReceiverService onCreate")
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mContext = this
        var intentFilter = IntentFilter()
        mBlueToothStateReceiver = BlueToothStateReceiver()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED) //蓝牙广播

        registerReceiver(mBlueToothStateReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mCurStartTag = intent?.getIntExtra(
            TAG,
            TAG_EXIT
        )
        Log.d(LOGE_TAG, "BpBleReceiverService onStartCommand startTag = $mCurStartTag")
        if (mCurStartTag == TAG_EXIT) {
            exitService()
        } else {
            mHandle.sendEmptyMessage(mCurStartTag ?: TAG_EXIT)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        unregisterReceiver()
        super.onDestroy()
        Log.d(LOGE_TAG, "BpBleReceiverService onDestroy")
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    fun unregisterReceiver() {
        if (mBlueToothStateReceiver != null) {
            unregisterReceiver(mBlueToothStateReceiver)
            mBlueToothStateReceiver = null
        }
    }

    private fun exitService() {
        Log.d(LOGE_TAG, "exitService")
        mHandle.removeCallbacksAndMessages(null)
        unregisterReceiver()
        disConnect()
        stopSelf()
    }

    /**
     * 扫描设备
     */
    private fun startScan() {
        if (mBluetoothAdapter == null) {
            Log.e(LOGE_TAG, "设备不支持BLE蓝牙")
            return
        }
        if (!mBluetoothAdapter.isEnabled) {
            Log.e(LOGE_TAG, "未打开蓝牙，尝试蓝牙开启中")
            tryToStartBLE = true
            mBluetoothAdapter.enable()
            return
        }
//        if (!checkBpBlePairing()) {
//            Toast.makeText(this, "设备未配对，请先配对设备", Toast.LENGTH_SHORT).show()
//            return
//        }
        if (mCurStatus == STATE_SCANNING) {
            Log.e(LOGE_TAG, "already scan")
            return
        }
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (scanCallback == null) {
                scanCallback = object : ScanCallback() {
                    override fun onScanResult(callbackType: Int, result: ScanResult) {
                        checkAndConnectBpDevice(result.device)
                    }

                    override fun onScanFailed(errorCode: Int) {
                        Log.d(LOGE_TAG, "onScanFailed errorCode = $errorCode")
                    }

                    override fun onBatchScanResults(results: MutableList<ScanResult>) {
                        Log.d(LOGE_TAG, "onBatchScanResults")
                    }
                }
            }
            val mScanSettingsBuild = ScanSettings.Builder()
            mScanSettingsBuild.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                //定义回调类型
                mScanSettingsBuild.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                //设置蓝牙LE扫描滤波器硬件匹配的匹配模式
                mScanSettingsBuild.setMatchMode(ScanSettings.MATCH_MODE_STICKY);
            }
            //芯片组支持批处理芯片上的扫描
            if (mBluetoothAdapter.isOffloadedScanBatchingSupported) {
                //设置蓝牙LE扫描的报告延迟的时间（以毫秒为单位）
                //设置为0以立即通知结果
                mScanSettingsBuild.setReportDelay(0L)
            }

            Log.d(LOGE_TAG, "开始扫描 sdk=${android.os.Build.VERSION.SDK_INT}")
            mCurStatus = STATE_SCANNING
            //执行扫描 android 6.0 sdk23 后需要模糊定位权限，否则无法扫描出结果
            mBluetoothAdapter.bluetoothLeScanner.startScan(
                null,
                mScanSettingsBuild.build(), scanCallback
            )
        } else {
            if (mLeScanCallback == null) {
                mLeScanCallback =
                    BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
                        checkAndConnectBpDevice(device)
                    }
            }
            mCurStatus = STATE_SCANNING
            var scanResult = mBluetoothAdapter.startLeScan(mLeScanCallback)
            Log.d(LOGE_TAG, "开始扫描 sdk=${android.os.Build.VERSION.SDK_INT} result $scanResult")
            if (!scanResult) {
                stopScan(false)
                exitService()
            }
        }
    }

    /**
     * 停止扫描
     */
    private fun stopScan(findDevice: Boolean) {
        Log.d(LOGE_TAG, "停止扫描")
        mCurStatus = if (findDevice) {
            STATE_SCAN_COMPLETE
        } else {
            STATE_NORMAL
        }
        mLeScanCallback?.let { mBluetoothAdapter.stopLeScan(mLeScanCallback) }
        scanCallback.let { mBluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback) }
    }

    /**
     * 检查并连接设备
     */
    var deviceList = HashSet<String>()
    fun checkAndConnectBpDevice(
        device: BluetoothDevice
    ) {
        if (device.name.isNullOrEmpty()) {
            return
        }
        if (!deviceList.contains(device.name)) {
            deviceList.add(device.name)
            Log.d(LOGE_TAG, "发现设备:${device.name}")
            RxBus.post(RxEvent(RxEventType.BLE, device.name))
        }
        if (isBPDevices(device)) {
            if (mBPDevice == null) {
                Log.d(LOGE_TAG, "发现目标设备:${device.name},可以开始连接")
                mBPDevice = device
                mCurStatus =
                    STATE_SCAN_COMPLETE
                stopScan(true)
                if (mCurStartTag == TAG_AUTO_DATA) {
                    mHandle.sendEmptyMessage(TAG_CONNECT)
                }
            }
        }
    }

    /**
     * 血压计设备是否已配对
     */
    private fun checkBpBlePairing(): Boolean {
        val pairedDevices: Set<BluetoothDevice> = mBluetoothAdapter.bondedDevices
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                if (isBPDevices(device)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 是否是血压计设备
     */
    private fun isBPDevices(device: BluetoothDevice): Boolean {
        return device.name.contains("A&D")
    }

    /**
     * 连接设备
     */
    fun connectBpDevices() {
        if (mBPDevice == null) {
            Log.e(LOGE_TAG, "请先扫描设备，在开始连接")
            return
        }
        Log.d(LOGE_TAG, "开始连接设备：" + mBPDevice?.name)
        mBluetoothGatt = mBPDevice?.connectGatt(this, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(
                gatt: BluetoothGatt,
                status: Int,
                newState: Int
            ) {
                Log.d(LOGE_TAG, "onConnectionStateChange status = $status; newState = $newState")
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d(LOGE_TAG, "onConnectionStateChange 连接成功")
                    isConnectedDevice = true
                    try {
                        Thread.sleep(600)
                        var disCover = gatt.discoverServices()
                        Log.d(LOGE_TAG, "discoverServices disCover = $disCover")
                    } catch (e: InterruptedException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    isConnectedDevice = false
                    mBPDevice = null
                    Log.d(LOGE_TAG, "onConnectionStateChange  断开连接")
                    exitService()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                Log.d(LOGE_TAG, "发现服务 status = $status")
                var service = mBluetoothGatt?.getService(ADGattUUID.DeviceInformationService)
                if (service != null) {
                    val characteristic: BluetoothGattCharacteristic =
                        service.getCharacteristic(ADGattUUID.FirmwareRevisionString)
                    if (characteristic != null) {
                        Log.d(LOGE_TAG, "readCharacteristic 读取固件信息")
                        mBluetoothGatt?.readCharacteristic(characteristic)
                    }
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                var firmwareRevisionString = String(characteristic.value)
                Log.d(
                    LOGE_TAG,
                    "onCharacteristicRead status = ${status}; 固件信息 firmwareRevisionString = $firmwareRevisionString"
                )
                setDateTimeSetting(gatt, Calendar.getInstance())
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                Log.d(LOGE_TAG, "onCharacteristicWrite  status = $status")
                var success = setIndication(gatt, true)
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic
            ) {
                Log.d(LOGE_TAG, "onCharacteristicChanged 接收数据")
                try {
                    var bundle = BloodPressureMeasurement.readCharacteristic(characteristic)
                    val intent = Intent(ACTION_BP_DATA)
                    intent.putExtra(INTENT_BUNDLE_KEY, bundle)
                    //广播通知数据
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
                    RxBus.post(
                        RxEvent(
                            RxEventType.BLE_DATA,
                            "sys = " + bundle.getFloat(ADGattService.KEY_SYSTOLIC) +
                                    "\ndia = " + bundle.getFloat(ADGattService.KEY_DIASTOLIC) +
                                    "\npul = " + bundle.getFloat(ADGattService.KEY_PULSE_RATE)
                        )
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onDescriptorRead(
                gatt: BluetoothGatt,
                descriptor: BluetoothGattDescriptor?,
                status: Int
            ) {
                Log.d(LOGE_TAG, "onDescriptorRead status = $status")
            }

            override fun onDescriptorWrite(
                gatt: BluetoothGatt,
                descriptor: BluetoothGattDescriptor,
                status: Int
            ) {
                Log.d(LOGE_TAG, "onDescriptorWrite status = $status")
            }

            override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
                Log.d(LOGE_TAG, "onReadRemoteRssi status = $status")
            }

            override fun onReliableWriteCompleted(gatt: BluetoothGatt, status: Int) {
                Log.d(LOGE_TAG, "onReliableWriteCompleted status$status")
            }
        })
        if (mBluetoothGatt == null) {
            Log.d(LOGE_TAG, "连接设备失败：" + mBPDevice?.name)
        }
    }


    /**
     * 断开设备连接
     */
    fun disConnect() {
        Log.d(LOGE_TAG, "停止扫描，设备连接断开")
        mBluetoothGatt?.close()
        mBluetoothGatt?.disconnect()
        mBluetoothGatt = null
        isConnectedDevice = false
    }


    /**
     * 设置血压计测量时间
     */
    private fun setDateTimeSetting(
        gatt: BluetoothGatt,
        cal: Calendar?
    ): Boolean {
        var isSuccess = false
        val gattService: BluetoothGattService? = getGattSearvice(gatt)
        if (gattService != null) {
            var characteristic =
                gattService.getCharacteristic(ADGattUUID.DateTime)
            if (characteristic != null) {
                characteristic = DateTime.writeCharacteristic(
                    characteristic,
                    cal
                )
                Log.d(LOGE_TAG, "writeCharacteristic DateTimeSetting 往血压计中写入时间参数")
                isSuccess = gatt.writeCharacteristic(characteristic)
            }
        }
        return isSuccess
    }

    /**
     * 获取Ble服务
     */
    private fun getGattSearvice(gatt: BluetoothGatt): BluetoothGattService? {
        var service: BluetoothGattService? = null
        for (uuid in ADGattUUID.ServicesUUIDs) {
            service = gatt.getService(uuid)
            if (service != null) break
        }
        return service
    }

    /**
     * 从BLE服务获取数据
     */
    private fun getGattMeasuCharacteristic(service: BluetoothGattService): BluetoothGattCharacteristic? {
        var characteristic: BluetoothGattCharacteristic? = null
        for (uuid in ADGattUUID.MeasuCharacUUIDs) {
            characteristic = service.getCharacteristic(uuid)
            if (characteristic != null) break
        }
        return characteristic
    }

    private fun setIndication(gatt: BluetoothGatt, enable: Boolean): Boolean {
        var isSuccess = false
        if (gatt != null) {
            val service: BluetoothGattService? = getGattSearvice(gatt)
            if (service != null) {
                val characteristic: BluetoothGattCharacteristic? =
                    getGattMeasuCharacteristic(service)
                if (characteristic != null) {
                    isSuccess = gatt.setCharacteristicNotification(characteristic, enable)
                    val descriptor =
                        characteristic.getDescriptor(ADGattUUID.ClientCharacteristicConfiguration)
                    if (enable) {
                        descriptor.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                        Log.d(LOGE_TAG, "writeDescriptor = ENABLE_INDICATION_VALUE")
                        gatt.writeDescriptor(descriptor)
                    } else {
                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        Log.d(LOGE_TAG, "writeDescriptor = ENABLE_NOTIFICATION_VALUE")
                        gatt.writeDescriptor(descriptor)
                    }
                }
            }
        }
        return isSuccess
    }


    inner class BlueToothStateReceiver : BroadcastReceiver() {
        private var DEFAULT_VALUE_BULUETOOTH = 1000
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                val state =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, DEFAULT_VALUE_BULUETOOTH)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        Log.d(LOGE_TAG, "蓝牙已关闭 STATE_OFF")
                    }
                    BluetoothAdapter.STATE_ON -> {
                        Log.d(LOGE_TAG, "蓝牙已打开 STATE_ON")
                        if (tryToStartBLE) {
                            mHandle.sendEmptyMessage(TAG_START_SCAN)
                        }
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {
                        Log.d(LOGE_TAG, "蓝牙打开中 STATE_TURNING_ON")
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        Log.d(LOGE_TAG, "蓝牙关闭中 STATE_TURNING_OFF")
                    }
                    else -> Log.d("BlueToothError", "蓝牙状态未知")
                }
            }
        }
    }
}