package com.mobile.bluewatch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kongzue.dialogx.dialogs.BottomMenu
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener
import com.mobile.bluewatch.bean.BaseBean
import com.mobile.bluewatch.bean.BindBean
import com.mobile.bluewatch.databinding.ActivityBlueToothBindingBinding
import com.mobile.bluewatch.http.RetrofitUtils
import com.tencent.mmkv.MMKV
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import kotlin.system.exitProcess

class BlueToothBindingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlueToothBindingBinding
    var sex = 1
    private var deviceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueToothBindingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        deviceId = intent.getStringExtra("deviceId")
        binding.deviceId.setText(deviceId)

        binding.sex.setOnClickListener {
            BottomMenu.show(arrayOf<String>("男", "女"))
                .setMessage("请选择性别").onMenuItemClickListener =
                OnMenuItemClickListener<BottomMenu?> { dialog, text, index ->
                    if (text == "女") {
                        sex = 0
                    }
                    binding.sex.text = text.toString()
                    false
                }
        }


        binding.back.setOnClickListener { finish() }
        binding.sure.setOnClickListener {
            val userPhone = binding.phone.text.toString()
            val userName = binding.nick.text.toString()
            val emergencyContact = binding.emergencyName.text.toString()
            val emergencyContactPhone = binding.emergencyPhone.text.toString()

            if (userPhone.isEmpty() || userName.isEmpty() || emergencyContact.isEmpty() || emergencyContactPhone.isEmpty()) {
                "请填写完整信息".toast()
                return@setOnClickListener
            }

            val jsonObject = JSONObject()
            jsonObject.put("userPhone", userPhone)
            jsonObject.put("deviceId", MMKV.defaultMMKV().decodeString("deviceId"))
            jsonObject.put("deviceVersion", "1")
            jsonObject.put("userName", userName)
            jsonObject.put("userSex", sex)
            jsonObject.put("emergencyContact", emergencyContact)
            jsonObject.put("emergencyContactPhone", emergencyContactPhone)

            val body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), jsonObject.toString()
            )
            RetrofitUtils.getApi().registerBraceletUser(body)
                .enqueue(object : retrofit2.Callback<BaseBean<BindBean>> {
                    override fun onResponse(
                        call: Call<BaseBean<BindBean>>, response: Response<BaseBean<BindBean>>
                    ) {
                        Log.e("TAG", "onResponse: " + response.body().toString())
                        if (response.body()?.data?.guuid.isNullOrEmpty()) {
                            "绑定失败".toast()
                        } else {
                            MMKV.defaultMMKV().encode("gguid", response.body()?.data?.guuid)
                            "绑定成功".toast()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<BaseBean<BindBean>>, t: Throwable) {
                        "绑定失败".toast()
                    }
                })
        }
    }
}