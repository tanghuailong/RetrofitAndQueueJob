package com.vstar.sacredsun_android_pda.util.rest;

import android.content.Context;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tanghuailong on 2017/1/12.
 */

/**
 * 处理 cookie 的拦截器
 */
public class CookieHeaderProvider implements Interceptor {

    public static final  String PREF_COOKIES = "PREF_COOKIES";
    private Context context;

    public CookieHeaderProvider(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        // 从sharePrefernces 获得的共享数据
        HashSet<String> preferences = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(PREF_COOKIES, new HashSet<String>());

        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }
        return chain.proceed(builder.build());
    }
}
