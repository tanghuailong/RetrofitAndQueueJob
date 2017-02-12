package com.vstar.sacredsun_android_pda.service;

/**
 * Created by tanghuailong on 2017/2/9.
 */


import com.vstar.sacredsun_android_pda.entity.EmptyResult;
import com.vstar.sacredsun_android_pda.entity.HttpResult;
import com.vstar.sacredsun_android_pda.entity.LoginEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * PDA 访问的一些接口
 */
public interface PDAApi {

    /**
     * 用户登陆
     * @param userLoginId  工号
     * @param password     密码
     * @param workCenterCode  工位编号
     * @return
     */
    @GET("staffmgt/control/clientLogin")
    Observable<HttpResult<LoginEntity,EmptyResult>> userLogin(@Query("userLoginId") String userLoginId, @Query("password")String password, @Query("workCenterCode")String workCenterCode);
    /**
     * 用户登出
     * @param session 登陆时获取到的session
     * @return
     */
    @GET("staffmgt/control/clientLogout")
    Observable<HttpResult> userLoginOut(@Query("session") String session);


}
