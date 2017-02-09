package com.vstar.sacredsun_android_pda.entity;

/**
 * Created by tanghuailong on 2017/2/9.
 */

public class LoginEntity {

    private String session;
    private String name;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LoginEntity{" +
                "session='" + session + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
