package com.mobile.bluewatch.bluetooth

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhm.ble.BleManager
import com.bhm.ble.callback.BleConnectCallback
import com.bhm.ble.data.BleScanFailType
import com.bhm.ble.device.BleDevice
import com.bhm.ble.utils.BleLogger
import com.bhm.ble.utils.BleUtil
import com.mobile.bluewatch.R
import com.mobile.bluewatch.adapter.BlueToothAdapter
import com.mobile.bluewatch.databinding.ActivityBlueToothScanBinding
import com.mobile.bluewatch.toast
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.item_bluetooth.btnConnect

class BlueToothScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlueToothScanBinding
    private var blueToothAdapter: BlueToothAdapter = BlueToothAdapter()

    companion object {
        var cbleDevice: BleDevice? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueToothScanBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = blueToothAdapter

        blueToothAdapter.setOnItemClickListener(object : BlueToothAdapter.onItemClickListener {
            override fun connect(bleDevice: BleDevice) {
                cbleDevice = bleDevice

                BleManager.get().connect(bleDevice) {
                    onConnectSuccess { bleDevice, gatt ->
                        blueToothAdapter.notifyDataSetChanged()
                        Log.d("jwl", "onConnectSuccess:${bleDevice.deviceName}")
                    }
                    onConnectFail { bleDevice, connectFailType ->

                        "${bleDevice.deviceName}设备连接失败 :${connectFailType}!".toast()
                        blueToothAdapter.notifyDataSetChanged()
                        Log.d("jwl", "onConnectFail:${connectFailType}")
                    }
                    onDisConnected { isActiveDisConnected, bleDevice, gatt, status ->
                        Log.d("jwl", "status:${status}")
                    }
                }
            }

            override fun disConnect(bleDevice: BleDevice) {
                BleManager.get().disConnect(bleDevice)
                Handler().postDelayed({
                    blueToothAdapter.notifyDataSetChanged()
                }, 3000)
            }

            override fun operate(bleDevice: BleDevice) {

                if (!BleManager.get().isConnected(bleDevice)) {
                    "蓝牙未连接".toast()
                    return
                }
                startActivity(
                    Intent(
                        this@BlueToothScanActivity,
                        BlueToothDetailsActivity::class.java
                    )
                )
            }
        })
        binding.scan.setOnClickListener {
            requestsPermisson()
        }
    }


    private var isConnecting = false

    private fun requestsPermisson() {
        RxPermissions(this).request(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
        ).subscribe { granted ->
            if (granted) {
                if (!BleUtil.isGpsOpen(this@BlueToothScanActivity)) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    return@subscribe
                }

                if (BleManager.get().isScanning()) {
                    "正在扫描".toast()
                    return@subscribe
                }

                blueToothAdapter.clearData()
                BleManager.get().startScan {
                    onScanStart {
                        Log.d("jwl", "onScanStart")
                    }
                    onLeScan { bleDevice, currentScanCount ->
                        //可以根据currentScanCount是否已有清空列表数据
                    }
                    onLeScanDuplicateRemoval { bleDevice, currentScanCount ->
                        //与onLeScan区别之处在于：同一个设备只会出现一次
                        Log.d("jwl", "onLeScanDuplicateRemoval")
                        runOnUiThread {
                            bleDevice.deviceName?.let {
                                blueToothAdapter.addData(bleDevice)
                            }
                        }
                    }
                    onScanComplete { bleDeviceList, bleDeviceDuplicateRemovalList ->
                        Log.d("jwl", "onScanComplete")
                        if (blueToothAdapter.getData().isNullOrEmpty()) {
                            "未扫描到设备".toast()
                        }

                        //扫描到的数据是所有扫描次数的总和
//                        blueToothAdapter.setData(bleDeviceList)
                    }
                    onScanFail {
                        val msg: String = when (it) {
                            is BleScanFailType.UnSupportBle -> "设备不支持蓝牙"
                            is BleScanFailType.NoBlePermission -> " 权限不足，请检查"
                            is BleScanFailType.GPSDisable -> "设备未打开GPS定位"
                            is BleScanFailType.BleDisable -> "蓝牙未打开"
                            is BleScanFailType.AlReadyScanning -> "正在扫描"
                            is BleScanFailType.ScanError -> {
                                "BleScanFailType.ScanError: ${it.throwable?.message}"
                            }
                        }
                        Log.d("jwl", msg)

                        BleLogger.e(msg)
                        Toast.makeText(application, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                "请赋予蓝牙权限".toast()
                startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
            }
        }
    }
}