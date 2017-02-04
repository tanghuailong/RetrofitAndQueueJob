package com.vstar.sacredsun_android_pda;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.vstar.sacredsun_android_pda.util.queue.AppJobManager;

/**
 * Created by tanghuailong on 2017/1/12.
 */

public class App extends Application {
    private static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化调试工具
        Stetho.initializeWithDefaults(this);
        AppJobManager.getJobManager(this);
    }
    public static App get(){
        return instance;
    }
}
