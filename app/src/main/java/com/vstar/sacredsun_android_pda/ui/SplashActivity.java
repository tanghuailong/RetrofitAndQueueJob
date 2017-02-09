package com.vstar.sacredsun_android_pda.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vstar.sacredsun_android_pda.util.other.FunctionUtil;

/**
 * Created by tanghuailong on 2017/2/9.
 */

/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FunctionUtil.checkIsLogin(SplashActivity.this);
    }
}
