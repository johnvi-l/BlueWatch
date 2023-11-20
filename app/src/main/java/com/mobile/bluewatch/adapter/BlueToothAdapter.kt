package com.mobile.bluewatch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bhm.ble.BleManager
import com.bhm.ble.device.BleDevice
import com.mobile.bluewatch.R
import kotlinx.android.synthetic.main.item_bluetooth.view.btnConnect
import kotlinx.android.synthetic.main.item_bluetooth.view.btnOperate
import kotlinx.android.synthetic.main.item_bluetooth.view.ivRssi
import kotlinx.android.synthetic.main.item_bluetooth.view.tvName

class BlueToothAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tvName)
        val ivRssi = itemView.findViewById<TextView>(R.id.ivRssi)
        val btnConnect = itemView.findViewById<TextView>(R.id.btnConnect)
        val btnOperate = itemView.findViewById<TextView>(R.id.btnOperate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_bluetooth, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return bleDeviceList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = bleDeviceList[position]

        holder.itemView.tvName.text = buildString {
            append(
                if (item.deviceName.isNullOrEmpty()) {
                    "未知"
                } else {
                    item.deviceName
                }
            )
            append(", ")
            append(item.deviceAddress)
        }
        holder.itemView.ivRssi.text = "信号:${item.rssi ?: 0}"
//        val rssi = item.rssi ?: 0

        if (BleManager.get().isConnected(item)) {
            holder.itemView.btnConnect.text = "断开"
            holder.itemView.btnOperate.isEnabled = true
            holder.itemView.btnConnect.setBackgroundColor(
                ContextCompat
                    .getColor(holder.itemView.btnConnect.context, R.color.red)
            )
        } else {
            holder.itemView.btnConnect.text = "连接"
            holder.itemView.btnOperate.isEnabled = false
            holder.itemView.btnConnect.setBackgroundColor(
                ContextCompat
                    .getColor(holder.itemView.btnConnect.context, R.color.purple_500)
            )
        }

        holder.itemView.btnConnect.setOnClickListener {
            listener?.connect(item)
        }
        holder.itemView.btnOperate.setOnClickListener {
            listener?.disConnect(item)
        }
        holder.itemView.btnOperate.setOnClickListener {
            listener?.operate(item)
        }
    }

    private var bleDeviceList: MutableList<BleDevice> = mutableListOf()
    fun setData(bleDeviceList: MutableList<BleDevice>) {
        this.bleDeviceList = bleDeviceList
        notifyDataSetChanged()
    }


    fun getData(): MutableList<BleDevice> {
        return bleDeviceList
    }

    fun addData(bleDevice: BleDevice) {
        bleDeviceList.add(bleDevice)
        notifyDataSetChanged()
    }

    private var listener: onItemClickListener? = null
    public fun setOnItemClickListener(listener: onItemClickListener) {
        this.listener = listener
    }

    interface onItemClickListener {
        fun connect(bleDevice: BleDevice)
        fun disConnect(bleDevice: BleDevice)

        fun operate(bleDevice: BleDevice)
    }

    fun clearData() {
        bleDeviceList.clear()
        notifyDataSetChanged()
    }
}