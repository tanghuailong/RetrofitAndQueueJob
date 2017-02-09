package com.vstar.sacredsun_android_pda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vstar.sacredsun_android_pda.R;

import butterknife.OnClick;

/**
 * Created by tanghuailong on 2017/2/9.
 */

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    /**
     * 确认输入的工位编号
     */
    @OnClick(R.id.btn_commit)
    public void inputConfirm() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
