package com.vstar.sacredsun_android_pda.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vstar.sacredsun_android_pda.R;

import butterknife.ButterKnife;

/**
 * Created by tanghuailong on 2017/1/20.
 */

public class LoginActivity extends AppCompatActivity {





    private static final String LOG_TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ButterKnife.bind(this);

    }
}
