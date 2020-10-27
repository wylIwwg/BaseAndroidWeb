package com.sjjd.wyl.baseandroidweb.bean;

import com.sjjd.wyl.baseandroidweb.base.IDescription;

public class BBaseSetting {
    @IDescription("服务器IP地址:")
    private String ip;
    @IDescription("服务器http端口:")
    private String httpPort;
    @IDescription("服务器socket端口:")
    private String socketPort;
    @IDescription("房间名称:")
    private String roomName;
    @IDescription("房间id")
    private String roomId;
    @IDescription("科室名称:")
    private String deptName;
    @IDescription("科室id")
    private String deptId;
    @IDescription("诊室名称:")
    private String clinicName;
    @IDescription("诊室id")
    private String clinicId;
    @IDescription("单位id")
    private String unitId;
    @IDescription("楼层id")
    private String floorId;
    @IDescription("楼层名称:")
    private String floorName;
    @IDescription("区域id")
    private String areaId;
    @IDescription("区域名称:")
    private String areaName;
    @IDescription("窗口id:")
    private String windowId;
    @IDescription("窗口名称:")
    private String windowName;
    @IDescription("声音开关:")
    private String voiceSwitch;
    @IDescription("声音格式:")
    private String voiceFormat;
    @IDescription("声音格式2:")
    private String voiceFormat2;
    @IDescription("设备关机时间:")
    private String devDownTime;
    @IDescription("设备开机时间:")
    private String devUpTime;
    @IDescription("日志目录:")
    private String pathLog;
    @IDescription("mac地址目录:")
    private String pathMac;
    @IDescription("注册文件目录:")
    private String pathRegister;


    public String getIp() {
        return ip == null ? "" : ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? "" : ip;
    }

    public String getHttpPort() {
        return httpPort == null ? "" : httpPort;
    }

    public void setHttpPort(String httpPort) {
        this.httpPort = httpPort == null ? "" : httpPort;
    }

    public String getSocketPort() {
        return socketPort == null ? "" : socketPort;
    }

    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort == null ? "" : socketPort;
    }

    public String getRoomName() {
        return roomName == null ? "" : roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? "" : roomName;
    }

    public String getRoomId() {
        return roomId == null ? "" : roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? "" : roomId;
    }

    public String getDeptName() {
        return deptName == null ? "" : deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? "" : deptName;
    }

    public String getDeptId() {
        return deptId == null ? "" : deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? "" : deptId;
    }

    public String getClinicName() {
        return clinicName == null ? "" : clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName == null ? "" : clinicName;
    }

    public String getClinicId() {
        return clinicId == null ? "" : clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId == null ? "" : clinicId;
    }

    public String getUnitId() {
        return unitId == null ? "" : unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId == null ? "" : unitId;
    }

    public String getFloorId() {
        return floorId == null ? "" : floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId == null ? "" : floorId;
    }

    public String getFloorName() {
        return floorName == null ? "" : floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName == null ? "" : floorName;
    }

    public String getAreaId() {
        return areaId == null ? "" : areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId == null ? "" : areaId;
    }

    public String getAreaName() {
        return areaName == null ? "" : areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? "" : areaName;
    }

    public String getWindowId() {
        return windowId == null ? "" : windowId;
    }

    public void setWindowId(String windowId) {
        this.windowId = windowId == null ? "" : windowId;
    }

    public String getWindowName() {
        return windowName == null ? "" : windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName == null ? "" : windowName;
    }

    public String getVoiceSwitch() {
        return voiceSwitch == null ? "" : voiceSwitch;
    }

    public void setVoiceSwitch(String voiceSwitch) {
        this.voiceSwitch = voiceSwitch == null ? "" : voiceSwitch;
    }

    public String getVoiceFormat() {
        return voiceFormat == null ? "" : voiceFormat;
    }

    public void setVoiceFormat(String voiceFormat) {
        this.voiceFormat = voiceFormat == null ? "" : voiceFormat;
    }

    public String getVoiceFormat2() {
        return voiceFormat2 == null ? "" : voiceFormat2;
    }

    public void setVoiceFormat2(String voiceFormat2) {
        this.voiceFormat2 = voiceFormat2 == null ? "" : voiceFormat2;
    }

    public String getDevDownTime() {
        return devDownTime == null ? "" : devDownTime;
    }

    public void setDevDownTime(String devDownTime) {
        this.devDownTime = devDownTime == null ? "" : devDownTime;
    }

    public String getDevUpTime() {
        return devUpTime == null ? "" : devUpTime;
    }

    public void setDevUpTime(String devUpTime) {
        this.devUpTime = devUpTime == null ? "" : devUpTime;
    }

    public String getPathLog() {
        return pathLog == null ? "" : pathLog;
    }

    public void setPathLog(String pathLog) {
        this.pathLog = pathLog == null ? "" : pathLog;
    }

    public String getPathMac() {
        return pathMac == null ? "" : pathMac;
    }

    public void setPathMac(String pathMac) {
        this.pathMac = pathMac == null ? "" : pathMac;
    }

    public String getPathRegister() {
        return pathRegister == null ? "" : pathRegister;
    }

    public void setPathRegister(String pathRegister) {
        this.pathRegister = pathRegister == null ? "" : pathRegister;
    }
}
