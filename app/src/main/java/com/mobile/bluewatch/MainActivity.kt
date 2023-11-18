package com.mobile.bluewatch

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile.bluewatch.bluetooth.BlueToothScanActivity
import com.mobile.bluewatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.mainFunction.setOnClickListener {
            startActivity(Intent(this, BlueToothScanActivity::class.java))
        }
    }
}
