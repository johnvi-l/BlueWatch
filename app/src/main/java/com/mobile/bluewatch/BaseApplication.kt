package com.mobile.bluewatch

import android.app.Application
import com.bhm.ble.BleManager
import com.tencent.mmkv.MMKV
import java.util.UUID





class BaseApplication : Application() {


    companion object {
        private lateinit var instance: BaseApplication

        fun getInstance(): BaseApplication {
            return instance
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        initMMKV()
        initBle()
    }

    private fun initBle() {
        BleManager
            .get()
            .init(this)
//        .options() //开启配置
//            .setLogBleEnable(true) //设置是否输出打印蓝牙日志（非正式打包请设置为true，以便于调试）
//            .setThrowBleException(true) //设置是否抛出蓝牙异常 （默认true）
//            .setAutoConnect(false) //设置是否自动连接 （默认false）
//            .setIgnoreRepeat(false) //设置是否过滤扫描到的设备(已扫描到的不会再次扫描)
//            .setConnectTimeout(10 * 1000) //设置连接超时时长（默认10*1000 ms）
//            .setMaxConnectNum(7) //最大连接数量
//            .setScanPeriod(12 * 1000) //设置扫描时长（默认10*1000 ms）
//            .setScanFilter(scanFilter) //设置扫描过滤
//            .setUuidService(UUID.fromString(UuidUtils.uuid16To128("fd00"))) //设置主服务的uuid（必填）
//            .setUuidWriteCha(UUID.fromString(UuidUtils.uuid16To128("fd01"))) //设置可写特征的uuid （必填,否则写入失败）
//            .setUuidReadCha(UUID.fromString(UuidUtils.uuid16To128("fd02"))) //设置可读特征的uuid （选填）
//            .setUuidNotifyCha(UUID.fromString(UuidUtils.uuid16To128("fd03"))) //设置可通知特征的uuid （选填，库中默认已匹配可通知特征的uuid）
//            .setUuidServicesExtra(arrayOf<UUID>(BATTERY_SERVICE_UUID)) //设置额外的其他服务组，如电量服务等
//            .setFactory(object : BleFactory() {
//                //实现自定义BleDevice时必须设置
//                fun create(address: String?, name: String?): MyDevice? {
//                    return MyDevice(address, name) //自定义BleDevice的子类
//                }
//            })
//            .setBleWrapperCallback(MyBleWrapperCallback()) //设置全部蓝牙相关操作回调（例： OTA升级可以再这里实现,与项目其他功能逻辑完全解耦）
//            .create(mApplication, object : InitCallback() {
//                fun success() {
//                    BleLog.e("MainApplication", "初始化成功")
//                }
//
//                fun failed(failedCode: Int) {
//                    BleLog.e("MainApplication", "初始化失败：$failedCode")
//                }
//            })
    }


    private fun initMMKV() {
        MMKV.initialize(this)
    }
}