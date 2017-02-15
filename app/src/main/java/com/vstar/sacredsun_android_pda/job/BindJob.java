package com.vstar.sacredsun_android_pda.job;

import android.content.Context;
import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.vstar.sacredsun_android_pda.App;
import com.vstar.sacredsun_android_pda.service.PDAApiNoRx;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;
import com.vstar.sacredsun_android_pda.util.queue.Priority;
import com.vstar.sacredsun_android_pda.util.rest.ApiException;
import com.vstar.sacredsun_android_pda.util.rest.HttpMethodNoRx;

/**
 * Created by tanghuailong on 2017/2/10.
 */

/**
 * 订单绑定job
 */
public class BindJob extends Job {

    private static final String LOG_TAG = "BindJob";
    Context context = null;

    private String workerSession="";
    private String orderCode="";
    private String orderCount="";
    private String materialCode="";
    private String assetsCode="";
    private String relCreateTime="";
    private String driverSession="";
    private String number="";


    private BindJob() {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("order"));
        context = App.getInstance();
    }

    public static BindJob create() {
        return new BindJob();
    }
    public BindJob setWorkerSession(String workerSession) {
        this.workerSession = workerSession;
        return this;
    }
    public BindJob setOrderCode(String orderCode) {
        this.orderCode = orderCode;
        return this;
    }
    public BindJob setOrderCount(String orderCount) {
        this.orderCount = orderCount;
        return this;
    }
    public BindJob setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
        return this;
    }
    public BindJob setAssetsCode(String assetsCode) {
        this.assetsCode = assetsCode;
        return this;
    }
    public BindJob setRelCreateTime(String relCreateTime) {
        this.relCreateTime = relCreateTime;
        return this;
    }
    public BindJob setDriverSession(String driverSession) {
        this.driverSession = driverSession;
        return this;
    }
    public BindJob setNumber(String number) {
        this.number = number;
        return this;
    }


    @Override
    public void onAdded() {
        Log.d(LOG_TAG,"add job to queue");
        int notHandleNumber = (int)SPHelper.get(context,"not_handle",0);
        SPHelper.putAndApply(App.getInstance(),"not_handle",notHandleNumber+1);
    }

    @Override
    public void onRun() throws Throwable {
        HttpMethodNoRx.getInstane().getService(PDAApiNoRx.class)
                .orderBind(workerSession,orderCode,orderCount,materialCode,assetsCode,relCreateTime,driverSession,number)
                .execute();
        Log.d(LOG_TAG,"job execute success");
        int notHandleNumber = (int)SPHelper.get(context,"not_handle",0);
        if(notHandleNumber > 0) {
            SPHelper.putAndApply(App.getInstance(), "not_handle", notHandleNumber - 1);
        }
    }

    @Override
    protected void onCancel() {
        int notHandleNumber = (int)SPHelper.get(context,"not_handle",0);
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
