package com.sjjd.wyl.baseandroidweb.bean;

/**
 * Created by wyl on 2020/5/12.
 */
public class BResult {
    private String state;
    private String message;
    private Object data;

    public String getState() {
        return state == null ? "" : state;
    }

    public void setState(String state) {
        this.state = state == null ? "" : state;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message == null ? "" : message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
