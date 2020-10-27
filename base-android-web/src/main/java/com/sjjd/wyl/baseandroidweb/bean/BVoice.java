package com.sjjd.wyl.baseandroidweb.bean;

/**
 * Created by wyl on 2019/8/16.
 */
public class BVoice {

    private String patientName;//当前就诊
    private String patientNum;//当前就诊排队号
    private String patientId;//当前就诊id
    private String doctorName;//医生
    private String docid;//医生
    private String departmentName;//科室
    private String clinicName;//诊室
    private String clinicId;//诊室ID
    private String count;//等候人数
    private String nextName;//下一位
    private String nextNum;//下一位排队号

    private String type;
    private String departName;
    private String departId;
    private String room;
    private String depart;
    private String doctor;
    private String queNum;
    private String qId;
    private String txt;

    public String getDocid() {
        return docid == null ? "" : docid;
    }

    public void setDocid(String docid) {
        this.docid = docid == null ? "" : docid;
    }

    public String getDepartmentName() {
        return departmentName == null ? "" : departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName == null ? "" : departmentName;
    }

    public String getCount() {
        return count == null ? "" : count;
    }

    public void setCount(String count) {
        this.count = count == null ? "" : count;
    }

    public String getNextName() {
        return nextName == null ? "" : nextName;
    }

    public void setNextName(String nextName) {
        this.nextName = nextName == null ? "" : nextName;
    }

    public String getNextNum() {
        return nextNum == null ? "" : nextNum;
    }

    public void setNextNum(String nextNum) {
        this.nextNum = nextNum == null ? "" : nextNum;
    }

    public void setQueNum(String queNum) {
        this.queNum = queNum == null ? "" : queNum;
    }

    public void setqId(String qId) {
        this.qId = qId == null ? "" : qId;
    }

    public String getDoctorName() {

        return doctorName == null ? "" : doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName == null ? "" : doctorName;
    }

    public String getClinicName() {
        return clinicName == null ? "" : clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getDepartName() {
        return departName == null ? "" : departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getClinicId() {
        return clinicId == null ? "" : clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getDepartId() {
        return departId == null ? "" : departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

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


    public String getTxt() {
        return txt == null ? "" : txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getQueNum() {
        return queNum == null ? "" : queNum;
    }

    public String getqId() {
        return qId == null ? "" : qId;
    }
}

