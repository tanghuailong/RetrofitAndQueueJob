package com.vstar.sacredsun_android_pda;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by tanghuailong on 2017/1/12.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
