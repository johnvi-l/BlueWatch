package com.mobile.bluewatch.bluetooth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhm.ble.BleManager
import com.bhm.ble.callback.BleConnectCallback
import com.bhm.ble.data.BleScanFailType
import com.bhm.ble.device.BleDevice
import com.bhm.ble.utils.BleLogger
import com.mobile.bluewatch.R
import com.mobile.bluewatch.adapter.BlueToothAdapter
import com.mobile.bluewatch.databinding.ActivityBlueToothScanBinding
import com.mobile.bluewatch.toast
import kotlinx.android.synthetic.main.item_bluetooth.btnConnect

class BlueToothScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlueToothScanBinding
    private var blueToothAdapter: BlueToothAdapter = BlueToothAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueToothScanBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = blueToothAdapter

        blueToothAdapter.setOnItemClickListener(object : BlueToothAdapter.onItemClickListener {
            override fun connect(bleDevice: BleDevice) {
                BleManager.get().connect(bleDevice) {
                    onConnectSuccess { bleDevice, gatt ->
                        blueToothAdapter.notifyDataSetChanged()
                    }
                    onConnectFail { bleDevice, connectFailType ->
                        connectFailType.toString().toast()
                    }
                    onDisConnected { isActiveDisConnected, bleDevice, gatt, status ->

                    }
                }
            }

            override fun disConnect(bleDevice: BleDevice) {
            }
        })
        binding.scan.setOnClickListener {

            if (BleManager.get().isScanning()) {
                "正在扫描".toast()
                return@setOnClickListener
            }
            blueToothAdapter.clearData()
            BleManager.get().startScan {
                onScanStart {

                }
                onLeScan { bleDevice, currentScanCount ->
                    //可以根据currentScanCount是否已有清空列表数据
                }
                onLeScanDuplicateRemoval { bleDevice, currentScanCount ->
                    //与onLeScan区别之处在于：同一个设备只会出现一次
                }
                onScanComplete { bleDeviceList, bleDeviceDuplicateRemovalList ->
                    //扫描到的数据是所有扫描次数的总和
                    blueToothAdapter.setData(bleDeviceList)
                }
                onScanFail {
                    val msg: String = when (it) {
                        is BleScanFailType.UnSupportBle -> "BleScanFailType.UnSupportBle: 设备不支持蓝牙"
                        is BleScanFailType.NoBlePermission -> "BleScanFailType.NoBlePermission: 权限不足，请检查"
                        is BleScanFailType.GPSDisable -> "BleScanFailType.BleDisable: 设备未打开GPS定位"
                        is BleScanFailType.BleDisable -> "BleScanFailType.BleDisable: 蓝牙未打开"
                        is BleScanFailType.AlReadyScanning -> "BleScanFailType.AlReadyScanning: 正在扫描"
                        is BleScanFailType.ScanError -> {
                            "BleScanFailType.ScanError: ${it.throwable?.message}"
                        }
                    }
                    BleLogger.e(msg)
                    Toast.makeText(application, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}