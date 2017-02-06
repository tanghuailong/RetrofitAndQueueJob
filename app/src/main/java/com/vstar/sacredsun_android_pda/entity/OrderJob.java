package com.vstar.sacredsun_android_pda.entity;


import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.vstar.sacredsun_android_pda.service.GithubApi;
import com.vstar.sacredsun_android_pda.util.rxjava.RxBus;
import com.vstar.sacredsun_android_pda.util.queue.Priority;
import com.vstar.sacredsun_android_pda.util.rest.HttpMethods;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by tanghuailong on 2017/1/12.
 */
@Deprecated
public class OrderJob extends Job {

    private static final String LOG_TAG = "OrderJob";
    private static final String TAG = OrderJob.class.getCanonicalName();

    public OrderJob() {
        //groupBy用来保证顺序执行，具体参考文档
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("order"));
    }

    @Override
    public void onAdded() {
        Log.d(LOG_TAG,"job is added");
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
        RxBus.getDefault().post(new UserEvent());
    }

    @Override
    protected void onCancel() {
        Log.d(LOG_TAG,"job is cancel");
    }

    /**
     * 决定是否重新运行job
     * @param throwable
     * @return true 重新运行job false 运行onCancel()
     */
    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        Log.d(LOG_TAG,"retry");
        return true;
    }

}

