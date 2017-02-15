package com.vstar.sacredsun_android_pda.service;

import com.vstar.sacredsun_android_pda.entity.HttpResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tanghuailong on 2017/2/12.
 */

public interface PDAApiNoRx {
    //订单绑定
    @GET("equipmentmgt/control/bindProductionOrder")
    Call<HttpResult> orderBind(@Query("session") String workerSession, @Query("orderCode") String orderCode, @Query("orderCount") String orderCount, @Query("materialCode") String materialCode, @Query("assetsCode") String assetsCode, @Query("relCreateTime") String relCreateTime, @Query("driver") String driverSession, @Query("number") String number);

    //订单解绑
    @GET("equipmentmgt/control/unbindProductionOrder")
    Call<HttpResult> orderUnBind(@Query("assetsCode")String assetsCode,@Query("session")String session);
}
