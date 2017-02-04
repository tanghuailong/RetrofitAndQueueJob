package com.vstar.sacredsun_android_pda.util.queue;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


/**
 * Created by tanghuailong on 2017/1/12.
 */
/**
 * job队列的配置类
 */
@Deprecated
public class AppJobManager {
    private static JobManager mJobManager;
    private static final String LOG_TAG = "AppJobManager";

    public static synchronized JobManager getJobManager() {
        return mJobManager;
    }

    public static synchronized JobManager getJobManager(Context context) {
        if(mJobManager == null) {
            configureJobManager(context);
        }
        return mJobManager;
    }
    public static synchronized void configureJobManager(Context context){
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

                        @Override
                        public void v(String text, Object... args) {

                        }
                    });
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(context,AppJobService.class),true);
            }else {
                int enableGcm = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

                Log.d(LOG_TAG,enableGcm + " "+ConnectionResult.SUCCESS);
                if(enableGcm == ConnectionResult.SUCCESS) {
                    builder.scheduler(GcmJobSchedulerService.createSchedulerFor(context,AppGcmJobService.class),true);
                }
            }
            mJobManager = new JobManager(builder.build());
        }
    }

}
