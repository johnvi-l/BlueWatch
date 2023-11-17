package com.mobile.bluewatch.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.util.Log

class MyBluetoothGattCallback : BluetoothGattCallback() {

    override fun onConnectionStateChange(
        gatt: BluetoothGatt,
        status: Int,
        newState: Int
    ) {
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            Log.i("BLE", "Connected to GATT server.")
//            gatt.discoverServices()
        } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            Log.i("BLE", "Disconnected from GATT server.")
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            // 在这里处理服务发现后的逻辑
        } else {
            Log.w("BLE", "onServicesDiscovered received: $status")
        }
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            // 处理从Characteristic中读取到的数据
        }
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            // 处理Characteristic写入成功后的逻辑
        }
    }
}
