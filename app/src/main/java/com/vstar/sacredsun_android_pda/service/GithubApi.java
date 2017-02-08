package com.vstar.sacredsun_android_pda.service;

import com.vstar.sacredsun_android_pda.entity.User;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by tanghuailong on 2017/1/12.
 */
@Deprecated
public interface GithubApi {
    @GET("/users/{user}")
    Observable<User> getUser(@Path("user") String user);
}
