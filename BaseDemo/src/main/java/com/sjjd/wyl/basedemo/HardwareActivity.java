package com.sjjd.wyl.basedemo;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sjjd.wyl.baseandroidweb.base.BaseHospitalActivity;
import com.sjjd.wyl.baseandroidweb.thread.TimeThread;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;
import com.sjjd.wyl.baseandroidweb.tools.ToolLog;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.Jzvd;

public class HardwareActivity extends BaseHospitalActivity {

    @BindView(R.id.btnVoiceSystemAdd)
    Button mBtnVoiceSystemAdd;
    @BindView(R.id.btnVoiceSystemMinus)
    Button mBtnVoiceSystemMinus;
    @BindView(R.id.btnVoiceMusicAdd)
    Button mBtnVoiceMusicAdd;
    @BindView(R.id.btnVoiceMusicMinus)
    Button mBtnVoiceMusicMinus;
    @BindView(R.id.btnVoiceRingAdd)
    Button mBtnVoiceRingAdd;
    @BindView(R.id.btnVoiceRingmMinus)
    Button mBtnVoiceRingmMinus;
    @BindView(R.id.btnVoiceAlarmAdd)
    Button mBtnVoiceAlarmAdd;
    @BindView(R.id.btnVoiceAlarmMinus)
    Button mBtnVoiceAlarmMinus;
    @BindView(R.id.videoplayer)
    SimpleJZPlayer mVideoplayer;
    @BindView(R.id.tvVoice)
    TextView mTvVoice;
    @BindView(R.id.btnAddTime)
    Button mBtnAddTime;
    @BindView(R.id.btnMinusTime)
    Button mBtnMinusTime;
    @BindView(R.id.tvCurrentTime)
    TextView mTvCurrentTime;
    @BindView(R.id.btnRestartNow)
    Button mBtnRestartNow;
    @BindView(R.id.btnOnOff)
    Button mBtnOnOff;
    @BindView(R.id.btnOnOffDelay)
    Button mBtnOnOffDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware);
        ButterKnife.bind(this);

        initData();

    }

    void requestPermission() {
        try {
            createSuProcess("chmod 777 /dev/alarm").waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Process createSuProcess() {
        File rootUser = new File("/system/xbin/ru");
        try {

            if (rootUser.exists()) {
                return Runtime.getRuntime().exec(rootUser.getAbsolutePath());
            } else {
                return Runtime.getRuntime().exec("su");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    Process createSuProcess(String cmd) {

        DataOutputStream os = null;
        Process process = createSuProcess();

        try {
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit $?\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }

        return process;
    }

    @Override
    public void showTime(String dateStr, String timeStr, String week) {
        super.showTime(dateStr, timeStr, week);
        mTvCurrentTime.setText(dateStr + " " + timeStr);

    }

    void setVoice(int type, int step) {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (mAudioManager != null) {
            int max = mAudioManager.getStreamMaxVolume(type);
            ToolLog.e(TAG, "setVoice: 最大值： " + max);
            int value = mAudioManager.getStreamVolume(type);
            ToolLog.e(TAG, "setVoice: 当前值： " + value);
            int index = value + step;
            ToolLog.e(TAG, "setVoice: 设置值： " + index);
            mTvVoice.setText(mTvVoice.getText() + "  " + "， 最大值： " + max + " ，当前值： " + value);
            //if (index != 0 && index <= max) {
            mAudioManager.setStreamVolume(type, index, 0); //音量
            ToolLog.e(TAG, "setVoice: 设置后 ： " + mAudioManager.getStreamVolume(type));
            mTvVoice.setText(mTvVoice.getText() + "， 设置后 ： " + mAudioManager.getStreamVolume(type));
            // }
        }
    }

    @OnClick({R.id.btnVoiceSystemAdd, R.id.btnVoiceSystemMinus, R.id.btnVoiceMusicAdd, R.id.btnVoiceMusicMinus, R.id.btnVoiceRingAdd,
            R.id.btnRestartNow, R.id.btnOnOff, R.id.btnOnOffDelay,
            R.id.btnVoiceRingmMinus, R.id.btnVoiceAlarmAdd, R.id.btnVoiceAlarmMinus, R.id.btnAddTime, R.id.btnMinusTime,
    })


    public void onViewClicked(View view) {
        mTvVoice.setText(((Button) view).getText());
        switch (view.getId()) {
            case R.id.btnRestartNow://立即重启
                hardReboot(0);
                break;
            case R.id.btnOnOff://定时开关机
                mDataHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "设备即将关机，并在5分钟后开机", Toast.LENGTH_SHORT).show();
                        hardReboot(60 * 5);
                    }
                }, 2000);

                break;
            case R.id.btnOnOffDelay://定时开关机
                mBtnOnOffDelay.setEnabled(false);
                mDataHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "设备即将关机，并在5分钟后开机", Toast.LENGTH_SHORT).show();
                        mDataHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hardReboot(60 * 5);
                            }
                        }, 2000);
                    }
                }, 60 * 1000);

                break;
            case R.id.btnVoiceSystemAdd:
                setVoice(AudioManager.STREAM_SYSTEM, 1);
                break;
            case R.id.btnVoiceSystemMinus:
                setVoice(AudioManager.STREAM_SYSTEM, -1);
                break;
            case R.id.btnVoiceMusicAdd:
                setVoice(AudioManager.STREAM_MUSIC, 1);
                break;
            case R.id.btnVoiceMusicMinus:
                setVoice(AudioManager.STREAM_MUSIC, -1);
                break;
            case R.id.btnVoiceRingAdd:
                setVoice(AudioManager.STREAM_RING, 1);
                break;
            case R.id.btnVoiceRingmMinus:
                setVoice(AudioManager.STREAM_RING, -1);
                break;
            case R.id.btnVoiceAlarmAdd:
                setVoice(AudioManager.STREAM_ALARM, 1);
                break;
            case R.id.btnVoiceAlarmMinus:
                setVoice(AudioManager.STREAM_ALARM, -1);
                break;
            case R.id.btnAddTime://增加时间
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // requestPermission();
//                        SystemClock.setCurrentTimeMillis(System.currentTimeMillis() + 10 * 60 * 1000);
                        //  setSystemTime(System.currentTimeMillis() + 10 * 60 * 1000);
                        // hardReboot(111111);

                    }
                }).start();

                break;
            case R.id.btnMinusTime://减少时间
                setSystemTime(System.currentTimeMillis() - 10 * 60 * 1000);
                // requestPermission();
                // SystemClock.setCurrentTimeMillis(System.currentTimeMillis() - 10 * 60 * 1000);
                break;
        }
    }


}
