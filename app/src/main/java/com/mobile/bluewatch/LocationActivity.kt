package com.mobile.bluewatch

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.mobile.bluewatch.databinding.ActivityLocationBinding
import com.tbruyelle.rxpermissions3.RxPermissions

class LocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {
            rPermissons()
        }
    }

    private fun rPermissons() {

        RxPermissions(this).request(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,

            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
        ).subscribe { granted ->
            if (granted) {
                startLocation()
            }
        }
    }

    private fun startLocation() {
        val aMapLocationClient = AMapLocationClient(this)
        aMapLocationClient.setLocationListener {
            Log.d("jwl", "it:${it.toString()}")
            binding.tv.text = "信息:${it.latitude},${it.longitude}"
        }

        val aMapLocationClientOption = AMapLocationClientOption()
        aMapLocationClientOption.locationMode =
            AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
        aMapLocationClientOption.isOnceLocation = true
        //获取最近3s内精度最高的一次定位结果：
//        aMapLocationClientOption.isOnceLocationLatest = true;

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        aMapLocationClientOption.interval = 1000;


        aMapLocationClient.setLocationOption(aMapLocationClientOption)
        aMapLocationClient.startLocation()
    }

}