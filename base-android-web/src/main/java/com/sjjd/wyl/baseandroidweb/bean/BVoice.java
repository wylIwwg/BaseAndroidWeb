package com.sjjd.wyl.baseandroidweb.bean;

/**
 * Created by wyl on 2019/8/16.
 */
public class BVoice {
    private String type;
    private String patientName;
    private String patientNum;
    private String patientId;
    private String room;
    private String depart;
    private String doctor;
    private int queNum;
    private int qId;
    private String txt;

    public String getDepart() {
        return depart == null ? "" : depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getDoctor() {
        return doctor == null ? "" : doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getRoom() {
        return room == null ? "" : room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPatientNum() {
        return patientNum == null ? "" : patientNum;
    }

    public void setPatientNum(String patientNum) {
        this.patientNum = patientNum;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPatientName() {
        return patientName == null ? "" : patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientId() {
        return patientId == null ? "" : patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getQueNum() {
        return queNum;
    }

    public void setQueNum(int queNum) {
        this.queNum = queNum;
    }

    public int getqId() {
        return qId;
    }

    public void setqId(int qId) {
        this.qId = qId;
    }

    public String getTxt() {
        return txt == null ? "" : txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}

