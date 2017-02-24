package com.vstar.sacredsun_android_pda.util.rest;

import android.content.Context;
import android.text.TextUtils;

import com.vstar.sacredsun_android_pda.App;
import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;

import java.io.IOException;

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
    private Context context  = App.getInstance();

    public CookieHeaderProvider(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String result = "";
        String workerSession = (String) SPHelper.get(context,context.getString(R.string.WORKER_SESSION),"");
        String driverSession = (String) SPHelper.get(context,context.getString(R.string.DRIVER_SESSION),"");
        if(workerSession.isEmpty() || driverSession.isEmpty()) {
            if(!workerSession.isEmpty()) {
                result = "worker="+workerSession;
            }
            if(!driverSession.isEmpty()) {
                result = "driver="+driverSession;
            }
        }else {
            result = "driver="+workerSession+";"+"worker="+driverSession;
        }
        if(!TextUtils.isEmpty(result)) {
            builder.addHeader("Cookie", result);
        }
        return chain.proceed(builder.build());
    }
}
