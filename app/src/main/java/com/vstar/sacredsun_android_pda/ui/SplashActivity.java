package com.vstar.sacredsun_android_pda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vstar.sacredsun_android_pda.util.other.FunctionUtil;
import com.vstar.sacredsun_android_pda.util.other.StartMode;

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
        StartMode startMode = FunctionUtil.getStartMode(SplashActivity.this);
        switch (startMode) {
            case NO_SETTING:
                Intent settingIntent = new Intent(this,SettingActivity.class);
                startActivity(settingIntent);
                finish();
                break;
            case NO_LOGIN:
                Intent loginIntent = new Intent(this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            case NORMAL:
                Intent normalIntent = new Intent(this,MainActivity.class);
                startActivity(normalIntent);
                finish();
                break;
        }
    }
}
