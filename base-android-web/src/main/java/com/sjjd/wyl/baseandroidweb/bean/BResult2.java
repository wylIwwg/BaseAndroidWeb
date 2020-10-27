package com.sjjd.wyl.baseandroidweb.bean;

public class BResult2 {
    private String code;
    private String msg;
    private Object data;

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code == null ? "" : code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? "" : msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

