package com.mobile.bluewatch

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart.line
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
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
            showDate()
        }
        initData()

        binding.sure.setOnClickListener {
            initChart()
        }
    }

    private fun showDate() {
        val arrayListOf = arrayListOf<String>()

        chartData?.forEachIndexed { index, deviceBean ->
            val split = deviceBean.createTime.split(" ")
            if (split.size >= 2) {
                arrayListOf.add(split[0])
            }
        }
        BottomMenu.show(arrayListOf.distinct().toTypedArray())
            .setMessage("请选择时间").onMenuItemClickListener =
            OnMenuItemClickListener { dialog, text, index ->
                binding.date.text = text.toString()
                false
            }

    }

    private fun initChart() {
//        "心率", "收缩压", "舒张压", "呼吸", "疲劳值"
        val toString = binding.project.text.toString()
        val date = binding.date.text.toString()

        if (toString == "请选择项目" || date == "请选择时间") {
            "请选择".toast()
            return
        }
        val list = mutableListOf<Entry>()

        chartData?.forEachIndexed { index, deviceBean ->
            if (!deviceBean.createTime.isNullOrEmpty()) {
                val split = deviceBean.createTime.split(" ")
                if (split.size >= 2) {
                    val s = split[0]
                    if (s == date) {
                        if (toString == "心率") {
                            list.add(Entry(index.toFloat(), deviceBean.heartData.toFloat()))
                        } else if (toString == "收缩压") {
                            list.add(Entry(index.toFloat(), deviceBean.systolicPressure.toFloat()))
                        } else if (toString == "舒张压") {
                            list.add(Entry(index.toFloat(), deviceBean.diastolicPressure.toFloat()))
                        } else if (toString == "呼吸") {
                            list.add(Entry(index.toFloat(), deviceBean.respiratoryRate.toFloat()))
                        } else if (toString == "呼吸") {
                            list.add(Entry(index.toFloat(), deviceBean.fatigueValue.toFloat()))
                        }
                    }
                }
            }
        }

        val lineDataSet = LineDataSet(list, toString)
        val lineData = LineData(lineDataSet)
        binding.chartView.data = lineData

//                        binding.chartView.setBackgroundColor(0x30000000)
        binding.chartView.xAxis.setDrawGridLines(false)
        binding.chartView.axisLeft.setDrawGridLines(false)

        val legend = binding.chartView.legend
        legend.isEnabled = true //是否显示图例


        val xAxis: XAxis = binding.chartView.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.axisLineColor = getColor(R.color.main)
        xAxis.axisLineWidth = 2f
        xAxis.position = XAxis.XAxisPosition.BOTTOM


        val AxisLeft: YAxis = binding.chartView.axisLeft
        AxisLeft.setDrawGridLines(false)
        AxisLeft.axisLineColor = getColor(R.color.main)
        AxisLeft.axisLineWidth = 2f
//                        AxisLeft.setValueFormatter(IAxisValueFormatter { v, axisBase ->
//
//                            //Y轴自定义坐标
//                            for (a in 0..15) {     //用个for循环方便
//                                if (a.toFloat() == v) {
//                                    return@IAxisValueFormatter "第" + a + "个"
//                                }
//                            }
//                            ""
//                        })
//                        AxisLeft.axisMaximum = 15f //Y轴最大数值

//                        AxisLeft.axisMinimum = 0f //Y轴最小数值

        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        AxisLeft.setLabelCount(15, false)

        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）

        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER;
        lineDataSet.color = Color.GREEN;  //折线的颜色
        lineDataSet.lineWidth = 2f;        //折线的粗细

        binding.chartView.axisRight.isEnabled = false;

        binding.chartView.description.isEnabled = false
        binding.chartView.notifyDataSetChanged();
        binding.chartView.invalidate()
    }


    private var chartData: MutableList<DeviceBean>? = null
    private fun initData() {
        val jsonObject = JSONObject()
        jsonObject.put("deviceId", MMKV.defaultMMKV().decodeString("deviceId"))
        jsonObject.put("deviceVersion", "1")
        jsonObject.put("gguid", "1")

        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"), jsonObject.toString()
        )

        RetrofitUtils.getApi().getBraceletDeviceInfo(body)
            .enqueue(object : Callback<BaseBean<MutableList<DeviceBean>>> {
                override fun onResponse(
                    call: Call<BaseBean<MutableList<DeviceBean>>>,
                    response: Response<BaseBean<MutableList<DeviceBean>>>
                ) {
                    val data = response.body()?.data
                    if (data.isNullOrEmpty()) {
                        "暂无数据".toast()
                    } else {
//                        https://blog.csdn.net/chuyouyinghe/article/details/123231056

                        chartData = data

                    }
                }

                override fun onFailure(
                    call: Call<BaseBean<MutableList<DeviceBean>>>, t: Throwable
                ) {
                }
            })
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