package com.vstar.sacredsun_android_pda.service;

import com.vstar.sacredsun_android_pda.entity.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by tanghuailong on 2017/1/12.
 */
@Deprecated
public interface GithubApi {
    @GET("/users/{user}")
    Call<User> getUser(@Path("user") String user);
}
