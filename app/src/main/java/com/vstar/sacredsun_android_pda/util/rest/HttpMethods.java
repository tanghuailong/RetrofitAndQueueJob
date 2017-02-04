package com.vstar.sacredsun_android_pda.util.rest;

/**
 * Created by tanghuailong on 2017/1/12.
 */

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.vstar.sacredsun_android_pda.App;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 访问网络的工具类
 */
public class HttpMethods {

    //配置参数
    private static final String BASE_URL = "https://api.github.com/";
    private static final int DEFAULT_TIME = 5;

    private Retrofit retrofit;
    //获得全局的上下文
    private static Context context = App.get();

    private HttpMethods() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //添加网络调试工具设置cookie和设置超时时间
        OkHttpClient client = builder.addNetworkInterceptor(new StethoInterceptor()).
                addInterceptor(new CookieHeaderProvider(context)).
                connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

   //单例模式
    public static class SingleHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }
    public static HttpMethods getInstane(){
        return SingleHolder.INSTANCE;
    }

    public <T> T getService(Class<T> tClass) {
        return retrofit.create(tClass);
    }
}
