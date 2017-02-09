package com.vstar.sacredsun_android_pda;

import android.app.Application;
import android.util.Log;


import com.facebook.stetho.Stetho;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

/**
 * Created by tanghuailong on 2017/1/12.
 */

public class App extends Application {

    private static App instance;
    private JobManager jobManager;

    public App() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化调试工具
        Stetho.initializeWithDefaults(this);
        //之前的配置方式
        //AppJobManager.getJobManager(this);
        configureJobManager();
    }

    public static App getInstance() {
        return instance;
    }


    /**
     * 配置JobManger的一些基本配置
     * consumer 代表了这些job将会被谁处理掉，比如添加了一个job,job会被执行，而执行者就是consumer,
     * 我自己的理解是，consumer 会有JobManager提供
     */
    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String LOG_TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(LOG_TAG,String.format(text,args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(LOG_TAG,String.format(text,args),t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(LOG_TAG,String.format(text,args));
                    }
                })
                .minConsumerCount(1)       //最少维持一个消费者
                .maxConsumerCount(3)       //最多维持三个消费者
                .loadFactor(3)             //每个消费者能有三个job
                .consumerKeepAlive(120)    //jobManager 运行完'准备着的'job,存活2分钟
                .build();
        jobManager = new JobManager(this,configuration);
    }

    public JobManager getJobManager() {
        return jobManager;
    }
}
