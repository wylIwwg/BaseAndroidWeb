package com.sjjd.wyl.baseandroidweb.bean;

/**
 * Created by wyl on 2020/1/20.
 */
public class BRegisterResult {
    private String registerStr;
    private int registerCode;
    private boolean registered;


    @Override
    public String toString() {
        return "BRegisterResult{" +
                "registerStr='" + registerStr + '\'' +
                ", registerCode=" + registerCode +
                ", registered=" + registered +
                '}';
    }

    public String getRegisterStr() {
        return registerStr == null ? "" : registerStr;
    }

    public void setRegisterStr(String registerStr) {
        this.registerStr = registerStr;
    }

    public int getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(int registerCode) {
        this.registerCode = registerCode;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
