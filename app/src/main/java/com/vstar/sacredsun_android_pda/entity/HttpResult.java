package com.vstar.sacredsun_android_pda.entity;

import java.util.List;

/**
 * Created by tanghuailong on 2017/1/11.
 */
public class HttpResult<T,U> {
    //0 代表成功 非0 代表不成功
    private String code;
    private String message;
    //数据部分
    private T item;
    private List<U> items;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public List<U> getItems() {
        return items;
    }

    public void setItems(List<U> items) {
        this.items = items;
    }
}
