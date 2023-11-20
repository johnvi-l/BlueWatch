package com.mobile.bluewatch.bluetooth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bhm.ble.BleManager
import com.bhm.ble.data.BleDescriptorGetType
import com.bhm.ble.utils.BleUtil
import com.mobile.bluewatch.byteArrayToHexString
import com.mobile.bluewatch.databinding.ActivityBlutToothDetailsBinding
import com.mobile.bluewatch.toHexWithSpaces
import com.mobile.bluewatch.utils.Analysis

class BlueToothDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlutToothDetailsBinding

    private val sb = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlutToothDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bleDevice = BlueToothScanActivity.cbleDevice!!

        BleManager.get().getBluetoothGatt(bleDevice)
            ?.services?.get(1)?.let {
                val serviceUUID = it.uuid.toString()
                var characteristicsUUID = it.characteristics[0].uuid.toString()
                BleManager.get().notify(
                    bleDevice,
                    serviceUUID,
                    characteristicsUUID,
                    BleDescriptorGetType.AllDescriptor
                ) {
                    onNotifyFail {
                    }
                    onNotifySuccess {
                    }
                    onCharacteristicChanged {
                        //数据处理在IO线程，显示UI要切换到主线程
                        launchInMainThread {
                            val byteToString = Analysis.getByteToString(it, "UTF-8", false, false)
                            Log.d("jwlll", "byteToString:${byteToString}")
                            parseData(byteToString)
                        }
                    }
                }
            }


        binding.btn.setOnClickListener {

        }
    }

    private fun parseData(byteToString: String?) {
        if (!byteToString.isNullOrEmpty()) {
            val splitData = byteToString.split(" ")
            if (splitData.isEmpty()) {
                return
            }
            if (splitData.size != 8) {
                return
            }
            val headData = splitData[0]
            if (headData != "A1") {
                return
            }
            val tailData = splitData[7]
            if (tailData != "A0") {
                return
            }

            val heartDta = splitData[1].toInt(16)
            val systolicPressure = splitData[2].toInt(16)
            val diastolicPressure = splitData[3].toInt(16)
            val respiratoryRate = splitData[4].toInt(16)
            val fatigueValue = splitData[5].toInt(16)
            val reservedByte = splitData[6].toInt(16)
            binding.tv.text =
                sb.append("心率: $heartDta 收缩压:${systolicPressure} 舒张压:${diastolicPressure} 呼吸:${respiratoryRate} 疲劳值:${fatigueValue}")
                    .append("\n")
        }
    }

    //    fun parseData(byteArray: ByteArray){
//        val byte3 = byteArray[3]
//        val byte4 = byteArray[4]
//        val parsedValue = parseBytes(
//            listOf<Byte>(byte3, byte4)
//        )
//        binding.tv.text = parsedValue.toString()
//    }
    private fun parData(byteArray: ByteArray) {

        val byte3 = byteArray[3]
        val byte4 = byteArray[4]
        val parsedValue = parseBytes(
            listOf<Byte>(byte3, byte4)
        )
        binding.tv.text = parsedValue.toString()
        /*if (byteArray.size != 23) {
            // 数据长度不符合要求
            // 在这里可以进行相应的错误处理逻辑
            Log.d("jwl", "数据长度不符合要求")
            return
        }

        // 解析帧头
        val frameHeader = byteArray[0]
        when (frameHeader.toInt()) {
            0xA1 -> println("Frame Header: <SOH>")
            0xA2 -> {
                println("Frame Header: A2 (Phone should return it as is)")
                // 在这里添加处理 A2 帧头的逻辑
            }
            else -> {
                Log.d("jwl", "帧头不符合要求")
                return
            }
        }


        // 解析心率
        val heartRate = byteArray[1].toInt()
        if (heartRate in 20..200) {
            println("Heart Rate: $heartRate N/min")
        } else {
            Log.d("jwl", "Invalid Heart Rate")
        }

        // 解析收缩压
        val systolicPressure = byteArray[2].toInt()
        if (systolicPressure in 20..200) {
            println("Systolic Pressure: $systolicPressure mmHg")
        } else {
            Log.d("jwl", "Invalid Systolic Pressure")
        }

        // 解析舒张压
        val diastolicPressure = byteArray[3].toInt()
        if (diastolicPressure in 20..200) {
            println("Diastolic Pressure: $diastolicPressure mmHg")
        } else {
            Log.d("jwl", "Invalid Diastolic Pressure")
        }

        // 解析呼吸
        val respiratoryRate = byteArray[4].toInt()
        if (respiratoryRate in 1..120) {
            println("Respiratory Rate: $respiratoryRate N/min")
        } else {
            Log.d("jwl", "Invalid Respiratory Rate")
        }

        // 解析预留字节
        val reservedByte = byteArray[5].toInt()
        if (reservedByte != 0) {
            println("Invalid Reserved Byte")
        }

        // 解析疲劳值
        val fatigueValue = byteArray[6].toInt()
        if (fatigueValue in 0..9) {
            println("Fatigue Value: $fatigueValue")
        } else {
            println("Invalid Fatigue Value")
        }


        // 解析帧尾
        val frameTail = byteArray[21]
        when (frameTail.toInt()) {
            0xA0 -> println("Frame Tail: <EOT>")
            else -> {
                Log.d("jwl", "帧尾不符合要求")
                return
            }
        }*/


//        binding.tv.text =
//            sb.append("心率: $heartRate 收缩压:${systolicPressure} 舒张压:${diastolicPressure} 呼吸:${respiratoryRate} 疲劳值:${fatigueValue}")
//                .append("\n")
    }

    fun parseBytes(data: List<Byte>): Int {
        // 将字节数据列表转换为字节数组
        val byteArray = data.toByteArray()
        // 将字节数组的两个字节组合为一个 16 位整数
        val combinedValue =
            (byteArray[0].toInt() and 0xFF) or ((byteArray[1].toInt() and 0xFF) shl 8)
        return combinedValue
    }


    private fun getHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        bytes.forEach {
            sb.append((it.toInt() and 0xff).toString(16))
        }
        return sb.toString()
    }

    private fun charToDecimal(c: Char): Int {
        return when (c) {
            in 'A'..'F' -> {
                10 + c.toInt() - 'A'.toInt()
            }

            in 'a'..'f' -> {
                10 + c.toInt() - 'a'.toInt()
            }

            else -> c - '0'
        }
    }
}