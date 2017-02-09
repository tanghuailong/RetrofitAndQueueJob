package com.vstar.sacredsun_android_pda.util.rest;

/**
 * Created by tanghuailong on 2017/1/11.
 */

public class ApiException extends RuntimeException{

    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }
    public ApiException(String message) {
        super(message);
    }
    //现在未使用到Code来做细致的区分
    private static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {
            default:
                message="未知错误";
        }
        return message;
    }
}
