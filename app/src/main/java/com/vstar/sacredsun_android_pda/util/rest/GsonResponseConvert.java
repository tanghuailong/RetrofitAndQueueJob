package com.vstar.sacredsun_android_pda.util.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.vstar.sacredsun_android_pda.entity.HttpResult;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by tanghuailong on 2017/1/11.
 */

public class GsonResponseConvert<T> implements Converter<ResponseBody,T> {

    private final Gson gson;
    private final Type type;

    GsonResponseConvert(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        Log.d("NetWork","response"+response);
        HttpResult httpResult = gson.fromJson(response,HttpResult.class);
        if(!httpResult.getCode().equals("0")) {
            throw new ApiException(httpResult.getMessage());
        }
        return gson.fromJson(response,type);

    }
}
