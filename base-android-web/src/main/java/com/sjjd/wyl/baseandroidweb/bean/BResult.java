package com.sjjd.wyl.baseandroidweb.bean;

/**
 * Created by wyl on 2020/5/12.
 */
public class BResult {
    private String state;
    private String msg;
    private String type;

    public String getState() {
        return state == null ? "" : state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
