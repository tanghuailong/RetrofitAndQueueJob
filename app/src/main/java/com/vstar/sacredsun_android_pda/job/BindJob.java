package com.vstar.sacredsun_android_pda.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.vstar.sacredsun_android_pda.entity.RunResult;
import com.vstar.sacredsun_android_pda.service.PDAApiNoRx;
import com.vstar.sacredsun_android_pda.util.queue.Priority;
import com.vstar.sacredsun_android_pda.util.rest.ApiException;
import com.vstar.sacredsun_android_pda.util.rest.HttpMethodNoRx;
import com.vstar.sacredsun_android_pda.util.rxjava.RxBus;

/**
 * Created by tanghuailong on 2017/2/10.
 */

/**
 * 订单绑定job
 */
public class BindJob extends Job {

    private static final String LOG_TAG = "BindJob";

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
        RxBus.getDefault().post(new RunResult(0,"添加到队列"));
    }

    @Override
    public void onRun() throws Throwable {
        HttpMethodNoRx.getInstane().getService(PDAApiNoRx.class)
                .orderBind(workerSession,orderCode,orderCount,materialCode,assetsCode,relCreateTime,driverSession,number)
                .execute();
        Log.d(LOG_TAG,"job execute success");
        RxBus.getDefault().post(new RunResult(1,"订单绑定成功"));
    }

    @Override
    protected void onCancel() {

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
