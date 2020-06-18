package com.sjjd.wyl.baseandroidweb.appTopService;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.sjjd.wyl.baseandroidweb.tools.ToolLog;

import java.util.List;

/**
 * Created by wyl on 2019/8/30.
 */
public class AppTopThread extends Thread {
    public String TAG = this.getClass().getSimpleName();
    private boolean loop = true;//是否循环
    private boolean pause = false;//是否暂停

    private int load_times = 0;//默认请求几次网络

    private long sleep_time = 1000 * 5;//默认2秒请求一次
    private int call_times = 0;//网络状态异常请求次数

    private String pkg;

    private Context mContext;

    private Object lock = new Object();

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public AppTopThread(Context context) {
        mContext = context;
        pkg = context.getPackageName();
    }


    public void OnPause() {
        pause = true;
    }

    public void OnResume() {
        pause = false;
        synchronized (lock) {
            lock.notify();
        }
    }

    private void onPause() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

        while (loop) {

            if (pause) {//是否阻塞线程
                onPause();
            }
            if (load_times == 1) {//是否只需要请求一次网络  默认一直请求
                loop = false;
            }

            initData();//网络请求
            SystemClock.sleep(sleep_time);//沉睡sleep time
        }

    }

    protected void initData() {
        try {
            ToolLog.e(TAG, "initData: 监测应用包名： " + pkg);
          /*  ActivityManager activityManager = (ActivityManager) mContext.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> mProcesses = activityManager.getRunningAppProcesses();
           if (mProcesses.size() > 0 && mProcesses.get(0).processName.equals(pkg)) {
                //说明当前集合第一位是该应用
            } else {
                //启动
                ToolLog.e(TAG, "initData:  应用不在前台");
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(pkg);
                mContext.startActivity(intent);
            }*/
            List<AndroidAppProcess> mApps = AndroidProcesses.getRunningForegroundApps(mContext);
            if (mApps != null && mApps.size() > 0) {
                ToolLog.e(TAG, "initData: " + mApps.size());
                int size = mApps.size();
                int index = 0;
                for (AndroidAppProcess app : mApps) {
                    ToolLog.e(TAG, "initData: " + app.name + " " + app.foreground);
                    if (app.name.equals(pkg)) {
                        break;
                    } else {
                        index++;
                    }
                }
                if (index == size) {
                    //说明当前集合没有该应用
                    //启动它
                    ToolLog.e(TAG, "initData:  应用不在前台");
                    Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(pkg);
                    mContext.startActivity(intent);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onDestroy() {
        call_times = 0;
        pause = true;
        loop = false;
    }
}
