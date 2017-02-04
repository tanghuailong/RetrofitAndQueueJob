package com.vstar.sacredsun_android_pda.util.queue;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;

/**
 * Created by tanghuailong on 2017/1/13.
 */
@Deprecated
public class AppGcmJobService extends GcmJobSchedulerService {
    @NonNull
    @Override
    protected JobManager getJobManager() {
        return AppJobManager.getJobManager();
    }
}
