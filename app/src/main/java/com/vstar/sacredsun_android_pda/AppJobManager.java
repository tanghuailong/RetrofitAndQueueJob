package com.vstar.sacredsun_android_pda;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;


/**
 * Created by tanghuailong on 2017/1/12.
 */
/**
 * job队列的配置类
 */
public class AppJobManager {
    private static JobManager mJobManager;

    public static synchronized JobManager getJobManager() {
        return mJobManager;
    }

    public static void configureJobManager(Context context){
        if(mJobManager == null) {
            Configuration.Builder builder = new Configuration.Builder(context)
                    .minConsumerCount(1)    //至少有一个消费线程
                    .maxConsumerCount(3)    //最多3 个消费线程
                    .loadFactor(3)           //每个消费线程三个job
                    .consumerKeepAlive(120)
                    .customLogger(new CustomLogger() {
                        private static final String TAG = "JOBS";
                        @Override
                        public boolean isDebugEnabled() {
                            return true;
                        }

                        @Override
                        public void d(String text, Object... args) {
                            Log.d(TAG,String.format(text,args));
                        }

                        @Override
                        public void e(Throwable t, String text, Object... args) {
                            Log.e(TAG,String.format(text,args),t);
                        }

                        @Override
                        public void e(String text, Object... args) {
                            Log.e(TAG,String.format(text,args));
                        }
                    });
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(context));
            }

        }
    }

}
