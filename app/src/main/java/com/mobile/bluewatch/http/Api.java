package com.mobile.bluewatch.http;


import com.mobile.bluewatch.bean.BaseBean;
import com.mobile.bluewatch.bean.BindBean;
import com.mobile.bluewatch.bean.DeviceBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
//
//    @POST("user/login")
//    Call<BaseBean<LoginBean>> login(@Body RequestBody body);

//    @POST("config")
//    Call<BaseBean<ConfigBean>> config(@Body RequestBody body);

    @POST("gateway/bracelet/api/registerBraceletUser")
    Call<BaseBean<BindBean>> registerBraceletUser(@Body RequestBody body);


    @POST("gateway/bracelet/api/pushBraceletDeviceLog")
    Call<BaseBean<String>> uploadInfo(@Body RequestBody body);

    @POST("gateway/bracelet/api/getBraceletDeviceLogList")
    Call<BaseBean<List<DeviceBean>>> getBraceletDeviceInfo(@Body RequestBody body);


    @POST("gateway/bracelet/api/pushBraceletMapLog")
    Call<BaseBean<String>>pushBraceletMapLog(@Body RequestBody body);
}
