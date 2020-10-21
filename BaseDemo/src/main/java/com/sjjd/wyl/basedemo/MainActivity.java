package com.sjjd.wyl.basedemo;

import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sjjd.wyl.baseandroidweb.adapter.CommonAdapter;
import com.sjjd.wyl.baseandroidweb.adapter.ViewHolder;
import com.sjjd.wyl.baseandroidweb.base.BaseActivity2;
import com.sjjd.wyl.baseandroidweb.bean.BBanner;
import com.sjjd.wyl.baseandroidweb.tekcos.SocketManager;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;
import com.sjjd.wyl.baseandroidweb.tools.ToolApp;
import com.sjjd.wyl.baseandroidweb.tools.ToolDevice;
import com.sjjd.wyl.baseandroidweb.tools.ToolDisplay;
import com.sjjd.wyl.baseandroidweb.tools.ToolLog;
import com.sjjd.wyl.baseandroidweb.tools.ToolWifi;
import com.sjjd.wyl.baseandroidweb.view.ImageBanner;
import com.sjjd.wyl.baseandroidweb.view.ItemScrollLayoutManager;
import com.sjjd.wyl.baseandroidweb.view.SettingDialog;
import com.sjjd.wyl.baseandroidweb.view.SuperBanner;
import com.sjjd.wyl.baseandroidweb.view.VerticalScrollTextView;
import com.yanzhenjie.permission.runtime.Permission;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
public class MainActivity extends BaseActivity2 {

    Button mBtnPlayer;
    @BindView(R.id.tvMac)
    TextView mTvMac;
    @BindView(R.id.rlv)
    SuperBanner mRlv;
    @BindView(R.id.banner)
    ImageBanner mBanner;
    @BindView(R.id.tvVs)
    VerticalScrollTextView mTvVs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

        PERMISSIONS = new String[]{Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE, Permission.READ_PHONE_STATE};
       /* setLeftTitleName("左边标题");
        setMiddleTitleName("中间标题");

        mTvTitleMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingPop(view);
            }
        });
*/

        mTvVs.setText("阿松大好收到好耍到后饿哦了阿松大好收到好耍到后饿阿松大好收到好耍到后饿");

        mTvVs.setTextSize(ToolDisplay.sp2px(mContext, 10));
        mTvVs.setSpeed(0.5f);
        mTvVs.setLineSpace(18);
        mTvVs.setGraphSpace(100);

        ToolLog.e(TAG, "onCreate: ");
        initListener();
        hasPermission();

        mDataHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(null, "11111", Toast.LENGTH_SHORT).show();
            }
        }, 1000);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ToolLog.e(TAG, "【onSaveInstanceState】");
        ToolApp.restartApp(mContext);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onUserLeaveHint() {
        ToolLog.e(TAG, "【onUserLeaveHint】");
        super.onUserLeaveHint();
    }

    @Override
    public void initListener() {
        super.initListener();
        initSetting();
        mDrawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                String ip = "11";
                String port = "22";
                String port2 = "33";
                mHolder.mEtIp.setText(ip);
                mHolder.mEtPort.setText(port);
                mHolder.mEtSocketPort.setText(port2);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mHolder.mLlPsw.setVisibility(View.VISIBLE);
                mHolder.mLlSetting.setVisibility(View.GONE);

              /*  test = test.substring(0, test.length() - 1);
                mTvDepartName.setText(test);
                mTvDepartName.setTextSize(ToolDisplay.px2sp(mContext, calTextSize(test)));
                if (test.length() >= 8) {
                    String c = test;
                    int cm = c.length() / 2;
                    StringBuilder sb = new StringBuilder();
                    sb.append(c.substring(0, cm));
                    sb.append("\n");
                    sb.append(c.substring(cm, c.length()));
                    mTvDepartName.setText(sb.toString());
                }*/

            }
        });
    }

    Holder mHolder;
    String host;
    CommonAdapter<SettingBean.Data> mDepartAdapter;
    CommonAdapter<SettingBean.Sublevel> mClinicAdapter;
    TextView tvDepart = null;
    TextView tvClinic = null;
    View mSettingView;


    private void initSetting() {
        mSettingView = LayoutInflater.from(mContext).inflate(R.layout.item_settings, null);

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ToolDisplay.dip2px(mContext, 200), -2);
        //  mParams.setMargins(20, 50, 5, 50);
        mSettingView.setLayoutParams(mParams);
        navigationView.removeAllViews();
        navigationView.addView(mSettingView);
        mHolder = new Holder(mSettingView);
        final List<SettingBean.Data> mDepartsList = new ArrayList<>();
        final List<SettingBean.Sublevel> mClinicList = new ArrayList<>();

        mHolder.mBtnPswConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mHolder.mEtAdmin.getText().toString())) {
                    String psw = mHolder.mEtAdmin.getText().toString().trim();
                    if (psw.equals("sjjd")) {
                        mHolder.mLlPsw.setVisibility(View.GONE);
                        mHolder.mLlSetting.setVisibility(View.VISIBLE);

                        mHolder.mBtnGetArea.setVisibility(View.VISIBLE);

                    }
                }
            }
        });
        mHolder.mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process.killProcess(Process.myPid());
                showLoading("asdasd");
            }
        });

        mDepartAdapter = new CommonAdapter<SettingBean.Data>(mContext, R.layout.item_area, mDepartsList) {
            @Override
            protected void convert(ViewHolder holder, final SettingBean.Data depart, int position) {
                holder.setText(R.id.tvArea, depart.getDepartName());
                final TextView tv = holder.getView(R.id.tvArea);
                tv.setBackgroundColor(getResources().getColor(R.color.main_theme2));
                holder.setOnClickListener(R.id.tvArea, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tvDepart != null) {
                            tvDepart.setBackgroundColor(getResources().getColor(R.color.main_theme2));
                        }
                        tv.setBackgroundColor(getResources().getColor(R.color.main_theme3));
                        tvDepart = tv;
                        tvDepart.setTag(depart.getId());

                        int id = depart.getId();
                        List<SettingBean.Sublevel> mSublevel = depart.getSublevel();
                        mClinicList.clear();
                        tvClinic = null;
                        mClinicList.clear();
                        mClinicList.addAll(mSublevel);
                        mClinicAdapter.notifyDataSetChanged();
                    }
                });

            }
        };

        mClinicAdapter = new CommonAdapter<SettingBean.Sublevel>(mContext, R.layout.item_area, mClinicList) {
            @Override
            protected void convert(ViewHolder holder, final SettingBean.Sublevel clinic, int position) {
                holder.setText(R.id.tvArea, clinic.getName());
                final TextView tv = holder.getView(R.id.tvArea);
                tv.setBackgroundColor(getResources().getColor(R.color.main_theme2));
                holder.setOnClickListener(R.id.tvArea, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tvClinic != null) {
                            tvClinic.setBackgroundColor(getResources().getColor(R.color.main_theme2));
                        }
                        tv.setBackgroundColor(getResources().getColor(R.color.main_theme3));
                        tvClinic = tv;
                        tvClinic.setTag(clinic.getId());
                    }
                });
            }
        };

        mHolder.mRlvClinic.setLayoutManager(new LinearLayoutManager(mContext));
        mHolder.mRlvClinic.setAdapter(mClinicAdapter);
        mHolder.mRlvDepart.setLayoutManager(new LinearLayoutManager(mContext));
        mHolder.mRlvDepart.setAdapter(mDepartAdapter);
    }

    public void ANR(View view) {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void EXCPETION(View view) {
        int i = 0;
        Toast.makeText(mContext, 10 / i, Toast.LENGTH_SHORT).show();
    }

    /*SpeechSynthesizer mTTSPlayer;


    private synchronized void TTSspeak(String txt) {
        if (txt != null && mTTSPlayer != null) {
            mTTSPlayer.playText(txt);
            ToolLog.e(TAG, "TTSspeak: " + txt);
        }

    }*/
    static class Holder {
        @BindView(R.id.etAdmin)
        EditText mEtAdmin;
        @BindView(R.id.btnPswConfirm)
        Button mBtnPswConfirm;
        @BindView(R.id.llPsw)
        LinearLayout mLlPsw;
        @BindView(R.id.labelIp)
        TextView mLabelIp;
        @BindView(R.id.etIp)
        EditText mEtIp;
        @BindView(R.id.labelPort)
        TextView mLabelPort;
        @BindView(R.id.etPort)
        EditText mEtPort;
        @BindView(R.id.labelNum)
        TextView mLabelNum;
        @BindView(R.id.etSocketPort)
        EditText mEtSocketPort;
        @BindView(R.id.llSetting)
        LinearLayout mLlSetting;
        @BindView(R.id.btnGetArea)
        Button mBtnGetArea;
        @BindView(R.id.rlvDepart)
        RecyclerView mRlvDepart;
        @BindView(R.id.rlvClinic)
        RecyclerView mRlvClinic;
        @BindView(R.id.btnConfirm)
        Button mBtnConfirm;
        @BindView(R.id.btnClose)
        Button mBtnClose;
        @BindView(R.id.llArea)
        LinearLayout mLlArea;
        @BindView(R.id.popRoot)
        LinearLayout mPopRoot;

        Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void initData() {
        super.initData();
        String[] mStrings = new String[]{
                "https://s1.ax1x.com/2018/11/16/ixCTOJ.jpg",
                "https://s1.ax1x.com/2018/11/16/ixCoy4.jpg",
                "https://s1.ax1x.com/2018/11/16/ixCIlF.jpg",
                "https://s1.ax1x.com/2018/11/16/ixC5SU.jpg",
                "https://s1.ax1x.com/2018/11/16/ixChWT.jpg",
                "https://s1.ax1x.com/2018/11/16/ixCfYV.jpg",
                "https://s1.ax1x.com/2018/11/16/ixCWF0.jpg",
                "https://s1.ax1x.com/2018/11/16/ixC2oq.jpg",
                "https://s1.ax1x.com/2018/11/16/ixCgwn.jpg"
        };

        List<BBanner> mBBanners = new ArrayList<>();
        for (int i = 0; i < mStrings.length; i++) {
            BBanner nb = new BBanner();
            nb.setUrl(mStrings[i]);
            mBBanners.add(nb);
        }
        mBanner.startPlayLoop(mBBanners, 3000, 500);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            data.add("test" + i);

        }
        CommonAdapter<String> mAdapter = new CommonAdapter<String>(mContext, R.layout.item, data) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tvContent, s);
            }
        };
        mAdapter.setLoop(false);
        ItemScrollLayoutManager mManager = new ItemScrollLayoutManager(mContext, RecyclerView.VERTICAL);
        //   mAdapter.setLoop(true);

        mRlv.setAdapter(mAdapter);
        mRlv.setLayoutManager(mManager);
        mManager.setScrollTime(100);
        mRlv.setTypeTime(SuperBanner.ROLL_ITEM, 1000);

        //   mRlv.start();
        //showLoading("aaaaaa");

     /*   mBtnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int speed = mSbSpeed.getProgress();
                int volume = mSpVolume.getProgress();
                mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_VOLUME, volume);
                mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_SPEED, speed);
                ToolLog.e(TAG, "onClick: speed " + speed + "  volume  " + volume);
                TTSspeak(mEtText.getText().toString());
            }
        });
        mSpVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mVolume.setText(progress + "");
                ToolLog.e(TAG, "onProgressChanged: mSpVolume ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSpeed.setText(progress + "");
                ToolLog.e(TAG, "onProgressChanged: mSbSpeed ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if (mTTSPlayer == null) {
            mTTSPlayer = ToolTts.getInstance(mContext).getTTSPlayer();
            if (mTTSPlayer == null) {
                ToolTts.getInstance(mContext).initTts(mContext);
                mTTSPlayer = ToolTts.getInstance(mContext).getTTSPlayer();
            }
        }
        if (mTTSPlayer != null) {

            mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_VOLUME, 100);
            mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_SPEED, 100);
        }
*/
        SocketManager.getInstance(mContext).startTcpConnection("192.168.2.188", "8282");
        SocketManager.getInstance(mContext).setTIME_OUT(30 * 1000);
        SocketManager.getInstance(mContext).setHEARTBEAT_RATE(10 * 1000);
        SocketManager.getInstance(mContext).setHandler(mDataHandler);


        //  initSocket();


    }

    @Override
    public void userHandler(Message msg) {
        super.userHandler(msg);
        switch (msg.what) {
            case IConfigs.MSG_SOCKET_RECEIVED:
                ToolLog.e(TAG, "userHandler:" + msg.obj);
                break;
        }
    }


    private String INFO() {
        //BOARD 主板
        String phoneInfo = "BOARD: " + Build.BOARD + "\n";
        //android.os.Build.VERSION.RELEASE：获取系统版本字符串。如4.1.2 或2.2 或2.3等
        phoneInfo += ", BOOTLOADER: " + Build.BOOTLOADER + "\n";
        //BRAND 运营商
        phoneInfo += ", BRAND: " + Build.BRAND + "\n";
        phoneInfo += ", CPU_ABI: " + Build.CPU_ABI + "\n";
        phoneInfo += ", CPU_ABI2: " + Build.CPU_ABI2 + "\n";

        //DEVICE 驱动
        phoneInfo += ", DEVICE: " + Build.DEVICE + "\n";
        //DISPLAY Rom的名字 例如 Flyme 1.1.2（魅族rom） &nbsp;JWR66V（Android nexus系列原生4.3rom）
        phoneInfo += ", DISPLAY: " + Build.DISPLAY + "\n";
        //指纹
        phoneInfo += ", FINGERPRINT: " + Build.FINGERPRINT + "\n";
        //HARDWARE 硬件
        phoneInfo += ", HARDWARE: " + Build.HARDWARE + "\n";
        phoneInfo += ", HOST: " + Build.HOST + "\n";
        phoneInfo += ", ID: " + Build.ID + "\n";
        //MANUFACTURER 生产厂家
        phoneInfo += ", MANUFACTURER: " + Build.MANUFACTURER + "\n";
        //MODEL 机型
        phoneInfo += ", MODEL: " + Build.MODEL + "\n";
        phoneInfo += ", PRODUCT: " + Build.PRODUCT + "\n";
        phoneInfo += ", RADIO: " + Build.RADIO + "\n";
        phoneInfo += ", RADITAGSO: " + Build.TAGS + "\n";
        phoneInfo += ", TIME: " + Build.TIME + "\n";
        phoneInfo += ", TYPE: " + Build.TYPE + "\n";
        phoneInfo += ", USER: " + Build.USER + "\n";
        //VERSION.RELEASE 固件版本
        phoneInfo += ", VERSION.RELEASE: " + Build.VERSION.RELEASE + "\n";
        phoneInfo += ", VERSION.CODENAME: " + Build.VERSION.CODENAME + "\n";
        //VERSION.INCREMENTAL 基带版本
        phoneInfo += ", VERSION.INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n";
        //VERSION.SDK SDK版本
        phoneInfo += ", VERSION.SDK: " + Build.VERSION.SDK + "\n";
        phoneInfo += ", VERSION.SDK_INT: " + Build.VERSION.SDK_INT + "\n";

        return phoneInfo;
    }

    /**
     * 获取设备HardwareAddress地址
     *
     * @return
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = "+";
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress += bytesToString(iF.getHardwareAddress()) + "+";
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }


    public void showInfo(View view) {

        mTvMac.setText("\n"
                + ToolDevice.getMacFromCatOrder() + "\n"
                + getMachineHardwareAddress() + "\n"
                + ToolWifi.getBroadcastAddress() + "\n\n"
                + INFO()
        );
        SettingDialog mDialog = SettingDialog.newInstance();
        mDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        mDialog.show(getFragmentManager(), "");
    }
}
