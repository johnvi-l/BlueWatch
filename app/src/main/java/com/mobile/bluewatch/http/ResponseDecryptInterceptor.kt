package com.mobile.bluewatch.http
import android.util.Log
import com.google.gson.Gson
import com.mobile.bluewatch.bean.BaseBean
import com.mobile.bluewatch.http.err.ErrHttpHandle
import com.mobile.bluewatch.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.nio.charset.Charset


class ResponseDecryptInterceptor : Interceptor {

    private val gson: Gson = Gson()
    private val errHttpHandle: ErrHttpHandle = ErrHttpHandle()


    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        var response = chain.proceed(request)

        val url = request.url()
        /*本次请求的接口地址*/
        val apiPath = "${url.scheme()}://${url.host()}:${url.port()}${url.encodedPath()}".trim()
        /*服务端的接口地址*/
        val serverPath = "${url.scheme()}://${url.host()}/".trim()
        Log.d("=====>", "请求地址:${apiPath}")

        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE)
                val buffer = source.buffer()
                var charset = Charset.forName("UTF-8")
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(charset)
                }
                val bodyString = buffer.clone().readString(charset)
                Log.d("====", "response:$bodyString");
                val fromJson = gson.fromJson(bodyString, BaseBean::class.java)
                if (fromJson.code == 200) {
                    val newResponseBody = ResponseBody.create(contentType, bodyString.trim())
                    response = response.newBuilder().body(newResponseBody).build()
                } else {
                    if (fromJson.code == 500) {
                        GlobalScope.launch(Dispatchers.Main) {
//                            MMKVManager.getInstance().put("Token", "")
//                            MMKVManager.getInstance().put("Role", "")
//                            MMKVManager.getInstance().put("RealName", "")
//
//                            BaseApplication.getApplication().startActivity(
//                                Intent(
//                                    BaseApplication.getApplication(),
//                                    HomeActivity::class.java
//                                ).apply {
//                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                                }
//                            )
                        }
                    }
                    errHttpHandle.handleErr(
                        ErrHttpHandle.NOT_SUCCESS, fromJson.msg
                    )
                }
                /*将解密后的明文返回*/
//                val newResponseBody = ResponseBody.create(contentType, bodyString.trim())
//                response = response.newBuilder().body(newResponseBody).build()
//                Log.d("=====>", "网络接口成功:$bodyString")
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    "服务端数据不存在!".toast()
                }
                Log.d("=====>", "响应体为空")
            }
        } else {
//            val responseBody = response.body()
//            if (responseBody != null) {
//                /*开始解密*/
//                val source = responseBody.source()
//                source.request(java.lang.Long.MAX_VALUE)
//                val buffer = source.buffer()
//                var charset = Charset.forName("UTF-8")
//                val contentType = responseBody.contentType()
//                if (contentType != null) {
//                    charset = contentType.charset(charset)
//                }
//                val bodyString = buffer.clone().readString(charset) ?: return response
//                //todo 接口服务ssh关闭,会导致此处报IllegalStateException异常
//                val fromJson = gson.fromJson(bodyString, BaseErrResponseBean::class.java)
//
////                if (fromJson?.error?.status == 400) {
////                }
//                GlobalScope.launch(Dispatchers.Main) {
//                    if (fromJson?.error?.status == 500) {
//                        ToastUtils.toast("账号或密码错误!")
//                    } else {
//                        ToastUtils.toast(fromJson?.error?.message)
//                    }
//                }
//
//                Log.d("=====>", "网络接口失败:$bodyString")
//            }
        }
        return response
    }
}

