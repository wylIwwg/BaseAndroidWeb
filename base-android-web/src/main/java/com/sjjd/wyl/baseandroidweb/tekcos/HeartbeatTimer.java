package com.sjjd.wyl.baseandroidweb.tekcos;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wyl on 2018/11/22.
 */
public class HeartbeatTimer {
    private Timer timer;
    private TimerTask task;
    private OnScheduleListener mListener;

    public HeartbeatTimer() {
        timer = new Timer();
    }

    public void startTimer(long delay, long period) {
        task = new TimerTask() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onSchedule();
                }
            }
        };
        timer.schedule(task, delay, period);
    }

    public void exit() {
        if (task != null) {
            task.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    public interface OnScheduleListener {
        void onSchedule();
    }

    public void setOnScheduleListener(OnScheduleListener listener) {
        this.mListener = listener;
    }
}
