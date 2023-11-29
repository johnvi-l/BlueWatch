package com.mobile.bluewatch.http;


import android.util.Log;


import com.mobile.bluewatch.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


/**
 * 针对私有化接口列表
 */
public class RetrofitUtils {

    private static int DEFAULT_TIMEOUT = 12;

    private static Api api;
    private static final String TAG = RetrofitUtils.class.getSimpleName();

    /**
     * 单例模式
     *
     * @return
     */
    public static Api getApi() {
        if (api == null) {
            synchronized (RetrofitUtils.class) {
                if (api == null) {
                    api = new RetrofitUtils().getRetrofit();
                }
            }
        }
        return api;
    }

    private RetrofitUtils() {
    }

    public Api getRetrofit() {
        // 初始化Retrofit
        Api api = initRetrofit().create(Api.class);
        return api;
    }

    private Retrofit initRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.d("RetrofitLog", "retrofitBack = " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .addInterceptor(loggingInterceptor)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request original = chain.request();
////                        String token_value = MMKVManager.getInstance().get("Token");
//
//                        if (TextUtils.isEmpty(token_value)) {
//                            return chain.proceed(original);
//                        }
//                        Request request = original.newBuilder()
//                                .header("Authorization", "Bearer "+token_value)
//                                .method(original.method(), original.body())
//                                .build();
//                        return chain.proceed(request);
//                    }
//                })
                .addInterceptor(new ResponseDecryptInterceptor())
                .build();
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JsonConverterFactory.create())
                .client(client)
                .build();


    }

}
