package com.vstar.sacredsun_android_pda.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.vstar.sacredsun_android_pda.App;
import com.vstar.sacredsun_android_pda.service.PDAApiNoRx;
import com.vstar.sacredsun_android_pda.util.other.NotificationHelper;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;
import com.vstar.sacredsun_android_pda.util.queue.Priority;
import com.vstar.sacredsun_android_pda.util.rest.ApiException;
import com.vstar.sacredsun_android_pda.util.rest.HttpMethodNoRx;

/**
 * Created by tanghuailong on 2017/2/10.
 */

/**
 * 订单解绑jobjob
 */
public class UnBindJob extends Job{

    private static final String LOG_TAG = "UnBindJob";

    private String assetsCode = "";
    private String session = "";

    private UnBindJob() {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("order"));
    }

    public static UnBindJob create() {
       return new UnBindJob();
    }


    public UnBindJob setAssetsCode(String assetsCode) {
        this.assetsCode = assetsCode;
        return this;
    }

    public UnBindJob setSession(String session) {
        this.session = session;
        return this;
    }

    @Override
    public void onAdded() {
        Log.d(LOG_TAG,"add job to queue");
//        RxBus.getDefault().post(new RunResult(0,"添加到队列"));
        int notHandleNumber = (int) SPHelper.get(App.getInstance(),"not_handle",0);
        SPHelper.putAndApply(App.getInstance(),"not_handle",notHandleNumber+1);
    }

    @Override
    public void onRun() throws Throwable {
        HttpMethodNoRx.getInstane().getService(PDAApiNoRx.class)
                .orderUnBind(assetsCode,session)
                .execute();
        Log.d(LOG_TAG,"job execute success");
//        RxBus.getDefault().post(new RunResult(1,"订单绑定成功"));
        NotificationHelper.showNotification(assetsCode,1);
        int notHandleNumber = (int)SPHelper.get(App.getInstance(),"not_handle",0);
        if(notHandleNumber > 0) {
            SPHelper.putAndApply(App.getInstance(), "not_handle", notHandleNumber - 1);
        }
    }

    @Override
    protected void onCancel() {
        int notHandleNumber = (int)SPHelper.get(App.getInstance(),"not_handle",0);
        if(notHandleNumber > 0) {
            SPHelper.putAndApply(App.getInstance(), "not_handle", notHandleNumber - 1);
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        Log.d(LOG_TAG,"job execute fail");
        //Exceptionoe为ApiException时候不需要去重试
        if(throwable instanceof ApiException) {
            return false;
        }else {
            return true;
        }
    }
}
