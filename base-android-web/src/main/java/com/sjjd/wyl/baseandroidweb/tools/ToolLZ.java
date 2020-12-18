package com.sjjd.wyl.baseandroidweb.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.blankj.utilcode.util.LogUtils;
import com.lztek.toolkit.Lztek;

import java.io.File;
import java.util.Date;

public class ToolLZ {

    static ToolLZ mToolLZ = new ToolLZ();
    static Lztek mLztek;

    public static void Init(Context context) {
        if (mLztek == null)
            mLztek = Lztek.create(context);
    }

    public static ToolLZ Instance() {
        return mToolLZ;
    }

    public boolean isLZDevice() {
        return mLztek != null;
    }

    /**
     * 读取以太网状态
     *
     * @return
     */
    public boolean getEthEnable() {
        if (mLztek == null)
            return false;
        return mLztek.getEthEnable();
    }

    /**
     * 设置以太网静态地址
     *
     * @param ip
     * @param mask
     * @param gateway
     * @param dns
     */
    public void setEthIpAddress(String ip, String mask, String gateway, String dns) {
        if (mLztek == null)
            return;
        mLztek.setEthIpAddress(ip, mask, gateway, dns);
    }

    /**
     * 读取以太网 MAC 地址
     *
     * @return
     */
    public String getEthMac() {
        if (mLztek == null)
            return "";
        return mLztek.getEthMac();
    }

    /**
     * 设置安卓系统的实时时钟时间，参数为从 1970 年开始到设置点的总秒数。请在应用中申
     * 明权限并进行系统签名<uses-permission android:name="android.permission.SET_TIME" />。
     *
     * @param milliseconds1970
     */
    public boolean setSystemTime(long milliseconds1970) {
        if (mLztek == null)
            return false;
        mLztek.setSystemTime(milliseconds1970);
        return true;
    }

    /**
     * 设置安卓系统的实时时钟时间，参数为从 1970 年开始到设置点的总秒数。请在应用中申
     * 明权限并进行系统签名<uses-permission android:name="android.permission.SET_TIME" />。
     *
     * @param date
     */
    public void setSystemTime(Date date) {
        if (mLztek == null)
            return;
        mLztek.setSystemTime(date);

    }

    /**
     * 基于开关机电路的硬件关机操作。关机后只能通过遥控器、重新插拔电源、开机按键等
     * 方式开机。注意：该 API 需要 root 权限
     */
    public void hardShutdown() {
        if (mLztek == null)
            return;
        mLztek.hardShutdown();
    }

    /**
     * 通过系统 reboot 命令实现的系统热复位重启。注意软件重启只是让 CPU 重新从初始指令
     * 开始启动，而并不能确保所有硬件设备和接口的完全复位。注意：该 API 需要 root 权限！
     */
    public void softReboot() {
        if (mLztek == null)
            return;
        mLztek.softReboot();
    }

    /**
     * 通过硬件开关机电路实现的系统关机重启。此接口可以确保系统除了 12V 输入和待机 5V
     * 之外的电源断电，是一种比较可靠的掉电重启机制。因为此命令需要触发一个关机断电动作，因而
     * 并不能实时重启，从断电到重新上电之间的时间间隔为几十秒钟（这个时间最大不会超过 60 秒）。
     */
    public void hardReboot() {
        if (mLztek == null)
            return;
        mLztek.hardReboot();

    }

    /**
     * 此接口调用后会立即关机并在指定的时间秒数后自动开机。参数秒数最小为 60 秒即 1 分
     * 钟以后自动开机。比如现在是 18:00，希望主板现在关机并于第二天 08:00 开机，则参数应该设置为
     * 50400。注意：该 API 需要 root 权限！
     *
     * @param onSeconds
     */
    public void alarmPoweron(int onSeconds) {
        mLztek.alarmPoweron(onSeconds);
    }

    /**
     * 隐藏安卓底部导航条。
     */
    public void hideNavigationBar() {
        if (mLztek == null)
            return;
        mLztek.hideNavigationBar();
    }

    /**
     * 指定需要安装的 APK 存放路径即可自动完成安装，安装时无需用户交互。注意：该 API
     * 需要 root 权限！
     *
     * @param apkPath
     */
    public void installApplication(String apkPath) {
        if (mLztek == null)
            return;
        mLztek.installApplication(apkPath);
    }

    public void installAppSlince(Context context, File apk) {

        //7.0以下 安装升级
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Intent intentapk = new Intent(Intent.ACTION_VIEW);
            intentapk.setDataAndType(Uri.fromFile(apk),
                    "application/vnd.android.package-archive");
            intentapk.putExtra("IMPLUS_INSTALL", "SILENT_INSTALL");
            context.startActivity(intentapk);

        } else {
            if (mLztek == null)
                return;
            String SHELL = "am start -a android.intent.action.VIEW -d %1$s " +
                    "-t application/vnd.android.package-archive -e IMPLUS_INSTALL SILENT_INSTALL";
            String apkPath = "file:///" + apk.getAbsolutePath();
            LogUtils.file("【7.0系统升级】", "onSuccess: " + String.format(SHELL, apkPath));
            mLztek.suExec(String.format(SHELL, apkPath));
        }
    }

    /**
     * 指定需要卸载的 APK 包名即可自动完成卸载，卸载时时无需用户交互。此处请注意函数
     * 的参数并不是 APK 的文件名而是实际的包名，比如要卸载微信程序则包名通常为 com.tencent.mm
     *
     * @param packageName
     */
    public void uninstallApplication(String packageName) {
        if (mLztek == null)
            return;
        mLztek.uninstallApplication(packageName);
    }

    /**
     * 应用自动卸载
     *
     * @param context
     * @param packageName
     */
    public void uninstallAppSlince(Context context, String packageName) {
        Intent intentapk = new Intent(Intent.ACTION_DELETE);
        intentapk.setDataAndType(Uri.parse("package:" + packageName),
                "application/vnd.android.package-archive");
        intentapk.putExtra("IMPLUS_UNINSTALL", "SILENT_UNINSTALL");
        context.startActivity(intentapk);
    }

    /**
     * 执行命令
     *
     * @return
     */
    public String suExec(String su) {
        if (mLztek == null)
            return "";
        return mLztek.getEthMac();
    }
}
