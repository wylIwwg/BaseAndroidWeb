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


}
