package com.sjjd.wyl.baseandroidweb.thread;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.lzy.okgo.OkGo;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;
import com.sjjd.wyl.baseandroidweb.tools.ToolLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wyl on 2019/7/19.
 */
public class RestartThread extends Thread {
    public String TAG = this.getClass().getSimpleName();
    public boolean loop = true;//是否循环
    public boolean pause = false;//是否暂停

    public int load_times = 0;//默认请求几次网络

    public long sleep_time = 1000 * 2;//默认2秒请求一次
    public int call_times = 0;//网络状态异常请求次数
    public int CALL_TIMES = 3;

    public Handler mHandler;//通知线程
    public Context mContext;

    private Object lock = new Object();
    private SimpleDateFormat mTimeFormat;

    private String rebootTime = "";//重启时间
    private String netTime = null;//网络时间

    public RestartThread(Context context, Handler handler) {
        mHandler = handler;
        mContext = context;
        mTimeFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
    }

    public void setNetTime(String netTime) {
        this.netTime = netTime;
    }

    public void setRebootTime(String rebootTime) {
        this.rebootTime = rebootTime;
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
        String timeStr = "";
        if (NetworkUtils.isConnected() && netTime != null) {
            timeStr = netTime;//有网的时候取网络时间
            ToolLog.e(TAG, "initData: 网络时间" + netTime);
        } else {//没网去本地时间
            long l = System.currentTimeMillis();
            Date mDate = new Date(l);
            timeStr = mTimeFormat.format(mDate);
            ToolLog.e(TAG, "initData: 本地时间" + timeStr);
        }
        if (timeStr.equals(rebootTime)) {
            LogUtils.file("【关机时间到了】: " + rebootTime);
            if (mHandler != null) {
                mHandler.sendEmptyMessage(IConfigs.MSG_REBOOT_LISTENER);
            }
        }

    }


    public void onDestroy() {
        call_times = 0;
        pause = true;
        loop = false;
        OkGo.getInstance().cancelTag(this);
    }
}
