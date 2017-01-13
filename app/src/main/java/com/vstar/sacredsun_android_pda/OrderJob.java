package com.vstar.sacredsun_android_pda;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by tanghuailong on 2017/1/12.
 */

public class OrderJob extends Job {

    private static final String LOG_TAG = "OrderJob";
    private static final String TAG = OrderJob.class.getCanonicalName();

    public OrderJob() {
        super(new Params(JobConstants.PRIORITY_NORMAL)
                .requireNetwork()
                .persist().addTags(TAG));
    }

    @Override
    public void onAdded() {
        Log.d(LOG_TAG,"job success");
    }

    @Override
    public void onRun() throws Throwable {
        GithubApi service = HttpMethods.getInstane().getService(GithubApi.class);
        Call<User> call = service.getUser("tanghuailong");
        Response<User> response = call.execute();
        if(response == null || !response.isSuccessful() || response.errorBody()!=null) {
            throw new RuntimeException("response fail");
        }
        Log.d(LOG_TAG,response.body().toString());
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.RETRY;
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

   //默认重试次数为20次，表明在运行抛出异常默认重试20次
//    @Override
//    protected int getRetryLimit() {
//        return Integer.MAX_VALUE;
//    }
}

