package com.mobile.bluewatch

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.mobile.bluewatch.bean.BaseBean
import com.mobile.bluewatch.databinding.ActivityLocationBinding
import com.mobile.bluewatch.http.RetrofitUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import com.tencent.mmkv.MMKV
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

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

            uploadInfo(it)


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

    private fun uploadInfo(it: AMapLocation?) {
        val jsonObject = JSONObject()
        jsonObject.put("mapLatitude", it?.latitude)
        jsonObject.put("mapLongitude", it?.longitude)
        jsonObject.put("deviceId", MMKV.defaultMMKV().decodeString("deviceId"))
        jsonObject.put("guuid", MMKV.defaultMMKV().decodeString("gguid"))

        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            jsonObject.toString()
        )
        RetrofitUtils
            .getApi()
            .pushBraceletMapLog(body)
            .enqueue(object : retrofit2.Callback<BaseBean<String>> {
                override fun onResponse(
                    call: Call<BaseBean<String>>,
                    response: Response<BaseBean<String>>
                ) {
                    Log.e("TAG", "onResponse: " + response.body().toString())
                    if (response.body()?.code == 200) {
                        "上报成功".toast()
                    }
                }

                override fun onFailure(call: Call<BaseBean<String>>, t: Throwable) {
                    "上报失败".toast()
                }
            })

    }

}