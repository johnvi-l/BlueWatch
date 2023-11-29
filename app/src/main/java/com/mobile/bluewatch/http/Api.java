package com.mobile.bluewatch.http;


import com.mobile.bluewatch.bean.BaseBean;

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

    @POST("bracelet/api/registerBraceletUser")
    Call<BaseBean<String>> registerBraceletUser(@Body RequestBody body);


    @POST("bracelet/api/uploadInfo")
    Call<BaseBean<String>> uploadInfo(@Body RequestBody body);
}
