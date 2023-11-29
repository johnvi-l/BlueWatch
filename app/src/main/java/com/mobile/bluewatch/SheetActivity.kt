package com.mobile.bluewatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kongzue.dialogx.dialogs.BottomMenu
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener
import com.mobile.bluewatch.bean.BaseBean
import com.mobile.bluewatch.bean.DeviceBean
import com.mobile.bluewatch.databinding.ActivitySheetBinding
import com.mobile.bluewatch.http.RetrofitUtils
import com.tencent.mmkv.MMKV
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SheetActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.project.setOnClickListener {
            showProject()
        }
        binding.date.setOnClickListener {

        }
        initHashMap()
        initData()
    }

    private fun initData() {
        val jsonObject = JSONObject()
        jsonObject.put("deviceId", MMKV.defaultMMKV().decodeString("deviceId"))
        jsonObject.put("deviceVersion", "1")
        jsonObject.put("gguid", "1")

        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            jsonObject.toString()
        )

        RetrofitUtils
            .getApi()
            .getBraceletDeviceInfo(body)
            .enqueue(object : Callback<BaseBean<MutableList<DeviceBean>>> {
                override fun onResponse(
                    call: Call<BaseBean<MutableList<DeviceBean>>>,
                    response: Response<BaseBean<MutableList<DeviceBean>>>
                ) {

                }

                override fun onFailure(
                    call: Call<BaseBean<MutableList<DeviceBean>>>,
                    t: Throwable
                ) {
                }
            })
    }

    private fun initHashMap() {

    }

    private var hashMap = mutableMapOf<String, String>()
    private fun showProject() {
        BottomMenu.show(arrayOf("心率", "收缩压", "舒张压", "呼吸", "疲劳值"))
            .setMessage("请选择项目").onMenuItemClickListener =
            OnMenuItemClickListener { dialog, text, index ->
                binding.project.text = text.toString()
                false
            }
    }
}