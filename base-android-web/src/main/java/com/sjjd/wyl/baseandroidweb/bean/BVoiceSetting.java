package com.sjjd.wyl.baseandroidweb.bean;

/**
 * Created by wyl on 2020/5/11.
 */
public class BVoiceSetting {

    private String voSex;
    private String voSpeed;
    private String voFormat;
    private String voNumber;

    public String getVoSex() {
        return voSex == null ? "" : voSex;
    }

    public void setVoSex(String voSex) {
        this.voSex = voSex;
    }

    public String getVoSpeed() {
        return voSpeed == null ? "" : voSpeed;
    }

    public void setVoSpeed(String voSpeed) {
        this.voSpeed = voSpeed;
    }

    public String getVoFormat() {
        return voFormat == null ? "" : voFormat;
    }

    public void setVoFormat(String voFormat) {
        this.voFormat = voFormat;
    }

    public String getVoNumber() {
        return voNumber == null ? "" : voNumber;
    }

    public void setVoNumber(String voNumber) {
        this.voNumber = voNumber;
    }

    @Override
    public String toString() {
        return "Data{" +
                "voSex='" + voSex + '\'' +
                ", voSpeed=" + voSpeed +
                ", voFormat='" + voFormat + '\'' +
                ", voNumber=" + voNumber +
                '}';
    }
}
