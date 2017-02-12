package com.vstar.sacredsun_android_pda.entity;

/**
 * Created by tanghuailong on 2017/2/12.
 */

/**
 * RxBus 传递数据entity
 */
public class RunResult {

    //传递的消息
    private String message;
    //0 添加到队列 1 执行成功 2 执行失败 3 取消
    private int statue;

    public RunResult(int statue,String message) {
        this.statue = statue;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }
}
