package com.vstar.sacredsun_android_pda;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

/**
 * Created by tanghuailong on 2017/1/12.
 */

public class OrderJob extends Job{

    public static final int PRIORITY = 1;

    public OrderJob() {
        super(new Params(PRIORITY).requireNetwork().persist());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    @Override
    protected void onCancel() {

    }
}

