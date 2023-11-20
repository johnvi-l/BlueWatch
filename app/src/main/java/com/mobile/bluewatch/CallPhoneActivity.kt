package com.mobile.bluewatch

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile.bluewatch.databinding.ActivityCallPhoneBinding
import com.tbruyelle.rxpermissions3.RxPermissions


class CallPhoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCallPhoneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btn.setOnClickListener {
            val toString = binding.et.text.toString()
            if (toString.isNullOrEmpty()) {
                "请输入号码".toast()
                return@setOnClickListener
            }

            RxPermissions(this).request(
                Manifest.permission.CALL_PHONE,
            ).subscribe { granted ->
                if (granted) {
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$toString"))
                    startActivity(intent)
                }
            }
        }
    }
}