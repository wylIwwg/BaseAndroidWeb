package com.sjjd.wyl.baseandroidweb.base;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sjjd.wyl.baseandroidweb.R;
import com.sjjd.wyl.baseandroidweb.bean.BPulse;
import com.sjjd.wyl.baseandroidweb.bean.BRegisterResult;
import com.sjjd.wyl.baseandroidweb.bean.BResult;
import com.sjjd.wyl.baseandroidweb.bean.BVoice;
import com.sjjd.wyl.baseandroidweb.bean.BVoiceSetting;
import com.sjjd.wyl.baseandroidweb.bean.BVolume;
import com.sjjd.wyl.baseandroidweb.listeners.RegisterListener;
import com.sjjd.wyl.baseandroidweb.thread.TimeThread;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;
import com.sjjd.wyl.baseandroidweb.tools.ToolApp;
import com.sjjd.wyl.baseandroidweb.tools.ToolCommon;
import com.sjjd.wyl.baseandroidweb.tools.ToolDevice;
import com.sjjd.wyl.baseandroidweb.tools.ToolDisplay;
import com.sjjd.wyl.baseandroidweb.tools.ToolLog;
import com.sjjd.wyl.baseandroidweb.tools.ToolRegister;
import com.sjjd.wyl.baseandroidweb.tools.ToolSP;
import com.sjjd.wyl.baseandroidweb.tools.ToolTts;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class BaseHospitalActivity extends AppCompatActivity implements BaseDataHandler.MessageListener, IView {
    public String TAG = this.getClass().getSimpleName();
    public Context mContext;
    public LinearLayout mBaseLlRoot;//根布局
    public BaseDataHandler mDataHandler;
    public String mHost = "";
    public boolean isRegistered = false;
    public int mRegisterCode = 0;
    public String mRegisterViper = "";

    public String mClientID = "";
    public String mIP;
    public String mHttpPort;
    public String mSocketPort;
    public String mMac;

    public String[] mPermissions;

    ///默认语音格式
    public String voiceFormat = "请(line)(name)到(department)(room)(doctor)就诊";

    public Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        mContext = this;
        mPresenter = new Presenter(mContext, this);

        ToolSP.init(mContext);
        mBaseLlRoot = findViewById(R.id.baseLlRoot);
        mDataHandler = new BaseDataHandler(this);
        mDataHandler.setMessageListener(this);
        mMac = ToolDevice.getMac();

        mTimeThread = new TimeThread(mContext, mDataHandler, "yyyy-MM-dd", "HH:mm", "EEEE");
        mTimeThread.sleep_time = 1000;
        mTimeThread.start();

        mTimeFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        mWeekFormat = new SimpleDateFormat("EEEE", Locale.CHINA);

        mPresenter.checkRegister(new RegisterListener() {
            @Override
            public void RegisterCallBack(BRegisterResult registerResult) {
                mRegisterCode = registerResult.getRegisterCode();
                mRegisterViper = registerResult.getRegisterStr();
                isRegistered = registerResult.isRegistered();
            }
        });
    }

    public void hasPermission() {
        mPresenter.checkPermission(mPermissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToolDisplay.hideBottomUIMenu(this);
    }


    public void initListener() {

    }

    public void initData() {
        initListener();

        mIP = ToolSP.getDIYString(IConfigs.SP_IP);
        mHttpPort = ToolSP.getDIYString(IConfigs.SP_PORT_HTTP);
        mSocketPort = ToolSP.getDIYString(IConfigs.SP_PORT_SOCKET);
        mVoiceSwitch = ToolSP.getDIYString(IConfigs.SP_VOICE_SWICH);
        if (mVoiceSwitch.length() < 1) {
            mVoiceSwitch = "1";
        }

        if (mIP.length() < 6) {
            Toasty.error(mContext, "请设置ip与端口号", Toast.LENGTH_LONG, true).show();
            return;
        }
        if (mHttpPort.length() < 1) {
            mHttpPort = "8080";
        }
        mHost = String.format(IConfigs.HOST, mIP, mHttpPort);


    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {

        if (mBaseLlRoot == null) return;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBaseLlRoot.addView(view, lp);
    }

    @Override
    public void showError(BResult result) {

    }

    @Override
    public void showError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasty.error(mContext, error, Toast.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    public void downloadApk(String url) {
        mPresenter.downloadApk(url);
    }

    @Override
    public void uploadScreen(String url, String sessionId) {
        String res = ToolCommon.getBitmapString(mBaseLlRoot);
        mPresenter.uploadScreen(url, res, sessionId);
    }

    public SimpleDateFormat mDateFormat;
    public SimpleDateFormat mTimeFormat;
    public SimpleDateFormat mWeekFormat;

    @Override
    public void userHandler(Message msg) {
        switch (msg.what) {
            case IConfigs.NET_TIME_CHANGED:
                HashMap<String, String> times = (HashMap<String, String>) msg.obj;
                if (times != null) {
                    //时间
                    String timeStr = times.get("date");
                    //日期
                    String dateStr = times.get("time");
                    //星期
                    String week = times.get("week");

                    showTime(dateStr, timeStr, week);
                }
                break;
            case IConfigs.MSG_SOCKET_RECEIVED:
                String obj = msg.obj.toString();
                ToolLog.e(TAG, "handleMessage: socket  " + obj);
                try {
                    JSONObject mObject = JSONObject.parseObject(obj);
                    String mType = mObject.getString("type");
                    switch (mType) {
                        case "pong"://心跳处理
                            Date mDate;
                            if (obj.contains("date")) {
                                long mTime = mObject.getLong("date");
                                if (mTime > 0) {
                                    mDate = new Date(mTime);
                                } else {
                                    mDate = new Date(System.currentTimeMillis());
                                }

                            } else {
                                //如果后台没有推送时间  采用本地的
                                mDate = new Date(System.currentTimeMillis());
                            }
                            //时间
                            String timeStr = mTimeFormat.format(mDate);
                            //日期
                            String dateStr = mDateFormat.format(mDate);
                            //星期
                            String week = mWeekFormat.format(mDate);
                            showTime(dateStr, timeStr, week);
                            break;
                        case "voiceSwitch"://flag
                            mVoiceSwitch = mObject.getString("flag");
                            ToolSP.putDIYString(IConfigs.SP_VOICE_SWICH, mVoiceSwitch);
                            break;

                        case "timing"://定时开关机
                            break;
                        case "screen"://截屏请求
                            String sessionId = mObject.getString("sessionId");
                            uploadScreen(URL_UPLOAD_SCREEN, sessionId);
                            break;
                        case "voiceSize"://设置声音大小
                            BVolume volume = JSON.parseObject(msg.obj.toString(), BVolume.class);
                            BVolume.Data vdata = volume.getData();
                            if (vdata != null) {
                                String vsize = vdata.getSize();
                                if (vsize.length() > 0) {
                                    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                    if (mAudioManager != null) {
                                        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                                        int value = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                        vsize = vsize.replace("%", "");
                                        int index = max * Integer.parseInt(vsize) / 100;
                                        if (index != 0) {
                                            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0); //音量
                                            ToolLog.e(TAG, "userHandler: " + index + "  " + max + "  " + value + "  " + mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
                                        }
                                    }
                                }
                            }
                            break;
                        case "restart":
                            Toasty.info(mContext, "设备即将重启", Toast.LENGTH_LONG).show();
                            hardReboot(0);

                            break;
                        case "upgrade"://更新apk
                            if (ToolLog.showLog) {
                                Toasty.info(mContext, "收到软件更新", Toast.LENGTH_LONG, true).show();
                            }
                            String link = mObject.getString("link");
                            if (link.length() > 0)
                                downloadApk(mHost + link);

                            break;
                        case "voice"://通知语音播报
                            ToolLog.e(TAG, "userHandler: voice  VoiceFlag = " + mVoiceSwitch + "   " + msg.obj.toString());
                            BVoice mVoiceBean = JSON.parseObject(mObject.get("data").toString(), BVoice.class);
                            if ("1".equals(mVoiceSwitch)) {
                                if (mVoiceBean != null && mVoiceBean.getPatientId().length() > 0) {
                                    mapVoice.put(mVoiceBean.getPatientId(), mVoiceBean);
                                }
                                if (!isSpeeking) {
                                    hasVoiceSpeak();
                                }
                            } else {
                                mapVoice.clear();
                            }

                            break;

                        case "register"://在线注册
                            String mRegister_code = mObject.getString("register_code");
                            boolean registered = ToolRegister.getInstance(mContext).registerDevice(mRegister_code);
                            if (registered) {
                                Toasty.success(mContext, "设备注册成功", Toast.LENGTH_SHORT, true).show();
                                if (mDataHandler != null)
                                    mDataHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToolApp.restartApp(mContext);
                                        }
                                    }, 1000);
                            }
                            break;
                        case "init": //socket连接成功之后 做初始化操作
                            String clientId = mObject.getString("id");
                            ToolSP.putDIYString(IConfigs.SP_CLIENT_ID, clientId);
                            ToolLog.e(TAG, "handleMessage:  clientId : " + clientId);
                            String ping = "{\"type\":\"ping\",\"id\":\"" + clientId + "\"}";
                            mPulseData.setPing(ping);
                            mSocketManager.getPulseManager().setPulseSendable(mPulseData);

                            mTimeThread.onDestroy();
                            mTimeThread = null;
                            addDevice(clientId);
                            break;

                        case "voiceFormat"://语音格式
                            mVoiceSetting = JSON.parseObject(mObject.get("data").toString(), BVoiceSetting.class);
                            if (mVoiceSetting != null) {
                                ToolSP.putDIYString("voice", JSON.toJSONString(mVoiceSetting));
                                initTts();
                            }

                            break;
                        //添加设备
                    }
                } catch (Exception e) {
                    showError(e.toString());
                }
        }

    }

    /**
     * 添加设备
     *
     * @param clientId
     */
    public void addDevice(String clientId) {

    }

    @Override
    public void release() {
        if (mTimeThread != null) {
            mTimeThread.onDestroy();
            mTimeThread = null;

        }
        if (mDataHandler != null) {
            mDataHandler.removeCallbacksAndMessages(null);
            mDataHandler = null;
        }
        if (mSocketManager != null) {
            mSocketManager.disconnect();
            mSocketManager = null;
        }
    }

    public TimeThread mTimeThread;


    public void hardReboot(final int l) {
        release();
        mDataHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ToolApp.restartApp(mContext);
            }
        }, 2000);

    }

    /**
     * 显示时间
     *
     * @param dateStr
     * @param timeStr
     * @param week
     */
    public void showTime(String dateStr, String timeStr, String week) {

    }

    public void close() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }


    public SpeechSynthesizer mTTSPlayer;//呼叫播放
    public boolean isSpeeking;//是否在播报语音
    public int speakTimes;//播报次数（已经）
    public boolean isSpeakTest;//测试语音
    public Map<String, BVoice> mapVoice = new HashMap<>();//记录语音播报
    public BVoice mNext;//下一位语音
    public String URL_UPDATE_VOICE;//修改语音完成的链接http
    public String URL_UPLOAD_SCREEN;//上传截图链接http

    public void initTTsListener() {
        if (mTTSPlayer == null) {
            mTTSPlayer = ToolTts.getInstance(mContext).getTTSPlayer();
            if (mTTSPlayer == null) {
                ToolTts.getInstance(mContext).initTts(mContext);
                mTTSPlayer = ToolTts.getInstance(mContext).getTTSPlayer();
            }
        }
        if (mTTSPlayer != null) {

            initTts();

            mTTSPlayer.setTTSListener(new SpeechSynthesizerListener() {
                @Override
                public void onEvent(int type) {
                    switch (type) {
                        case SpeechConstants.TTS_EVENT_PLAYING_START:
                            ToolLog.e(TAG, "onEvent:  TTS_EVENT_PLAYING_START ");
                            isSpeeking = true;
                            speakTimes++;
                            break;
                        case SpeechConstants.TTS_EVENT_PLAYING_END:
                            isSpeeking = false;
                            if (isSpeakTest) {
                                isSpeakTest = false;
                                return;
                            }

                            if (speakTimes == voiceCount) {//播放次数达标
                                // 呼叫成功 通知后台改状态
                                if (mapVoice != null && mapVoice.size() > 0 && mNext != null) {

                                    String url = URL_UPDATE_VOICE + mNext.getPatientId();
                                    OkGo.<String>get(url)
                                            .tag(this).execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            isSpeeking = false;
                                            if (response != null && response.body().contains("1")) {
                                                //修改状态成功后再移除
                                                mapVoice.remove(mNext.getPatientId());
                                                //继续播报下一个
                                                hasVoiceSpeak();

                                            } else {
                                                //重复呼叫
                                                speakTimes = 0;
                                                ttsSpeak();
                                            }
                                        }

                                        @Override
                                        public void onError(Response<String> response) {
                                            super.onError(response);
                                            //网络请求出错 重复呼叫
                                            isSpeeking = false;
                                            Toasty.error(mContext, response.body(), Toast.LENGTH_LONG, true).show();
                                            ttsSpeak();
                                        }
                                    });
                                }
                            } else {
                                //重复呼叫
                                mDataHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ttsSpeak();
                                    }
                                }, 1000);
                            }


                            break;
                    }
                }

                @Override
                public void onError(int i, String s) {
                    //语音播报失败
                    isSpeeking = false;
                    //重复呼叫
                    speakTimes = 0;
                    ttsSpeak();

                }
            });
        }

    }

    //是否可以播报
    public void hasVoiceSpeak() {
        if (mapVoice != null && mapVoice.size() > 0 && "1".equals(mVoiceSwitch)) {
            Iterator<BVoice> mIterator = mapVoice.values().iterator();
            if (mIterator.hasNext()) {
                if (isSpeeking) {
                    return;
                }
                mNext = mIterator.next();
                speakTimes = 0;//归0
                //开始语音播报
                ttsSpeak();

            }
        }
    }

    public String mVoiceSwitch = "1";//语音播报开关

    public synchronized void ttsSpeak() {
        if (mNext != null) {
            //"请(line)(name)到(department)(room)(doctor)就诊"
            String txt = mVoiceSetting.getVoFormat();
            txt = txt.replace("name", ToolCommon.SplitStarName(mNext.getPatientName(), "*", 1, 2))
                    .replace("line", mNext.getQueNum() + "")
                    .replace("department", mNext.getPatientName())
                    .replace("room", mNext.getRoom())
                    .replace("doctor", mNext.getDoctor())
                    .replace("(", "")
                    .replace(")", "");
            String voice = mVoiceSetting.getVoFormat();
            voice = voice.replace("name", mNext.getPatientName())
                    .replace("line", mNext.getQueNum() + "")
                    .replace("department", mNext.getPatientName())
                    .replace("room", mNext.getRoom())
                    .replace("doctor", mNext.getDoctor())
                    .replace("(", "")
                    .replace(")", "");

            showVoice(txt);
            if (mTTSPlayer != null && "1".equals(mVoiceSwitch)) {
                mTTSPlayer.playText(voice);
            }
        }


    }

    /**
     * 处理语音类容 是否显示
     *
     * @param txt
     */
    public void showVoice(String txt) {

    }


    /**
     * z直接播放语音
     *
     * @param txt
     */
    private synchronized void ttsSpeak(String txt) {
        //"请(line)(name)到(department)(room)(doctor)就诊"
        if (txt != null && mTTSPlayer != null) {
            mTTSPlayer.playText(txt);
        }

    }

    public int voiceCount = 1;//语音播报次数 默认1次
    public BVoiceSetting mVoiceSetting;//语音设置

    public void initTts() {
        ToolTts.getInstance(mContext).initTtsSetting(mVoiceSetting);
        voiceFormat = mVoiceSetting.getVoFormat();
        String mNumber = mVoiceSetting.getVoNumber();
        if (mNumber.length() > 0) {
            voiceCount = Integer.parseInt(mNumber);
            voiceCount = voiceCount > 0 ? voiceCount : 1;
        }
        //  mTTSPlayer.setOption(SpeechConstants.TTS_KEY_BACKEND_MODEL_PATH, TTSManager.getInstance(mContext).defaultDir + TTSManager.getInstance(mContext).backName);
    }


    public IConnectionManager mSocketManager;

    public void feed() {
        mSocketManager.getPulseManager().feed();
    }

    public void initSocket() {
        initSocket(mIP, mSocketPort);
    }

    public void initSocket(String ip, String port) {
        //连接参数设置(IP,端口号),这也是一个连接的唯一标识,不同连接,该参数中的两个值至少有其一不一样
        ConnectionInfo info = new ConnectionInfo(ip, Integer.parseInt(port));
        //调用OkSocket,开启这次连接的通道,拿到通道Manager
        mSocketManager = OkSocket.open(info);
        //获得当前连接通道的参配对象
        OkSocketOptions options = mSocketManager.getOption();
        //基于当前参配对象构建一个参配建造者类
        //  OkSocketOptions.Builder builder = new OkSocketOptions.Builder(options);
        //修改参配设置(其他参配请参阅类文档)
        //builder.setSinglePackageBytes(size);

        //设置自定义解析头
        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder(options);
        okOptionsBuilder.setPulseFrequency(5000);
        okOptionsBuilder.setReaderProtocol(new IReaderProtocol() {
            @Override
            public int getHeaderLength() {
                /*
                 * 返回不能为零或负数的报文头长度(字节数)。
                 * 您返回的值应符合服务器文档中的报文头的固定长度值(字节数),可能需要与后台同学商定
                 */
                //  return /*固定报文头的长度(字节数)*/;
                return 8;
            }

            @Override
            public int getBodyLength(byte[] header, ByteOrder byteOrder) {
     /*
         * 体长也称为有效载荷长度，
         * 该值应从作为函数输入参数的header中读取。
         * 从报文头数据header中解析有效负载长度时，最好注意参数中的byteOrder。
         * 我们强烈建议您使用java.nio.ByteBuffer来做到这一点。
         * 你需要返回有效载荷的长度,并且返回的长度中不应该包含报文头的固定长度
                        * /
                return /*有效负载长度(字节数)，固定报文头长度(字节数)除外*/
                ;
                // ToolLog.e("getBodyLengthheader: " + header.length);
                byte[] buffer = new byte[4];
                System.arraycopy(header, 0, buffer, 0, 4);
                int len = bytesToInt(buffer, 0);
                ToolLog.e(TAG, "getBodyLength: " + (len - 8));
                return len - 8;
            }

        });

        //建造一个新的参配对象并且付给通道
        mSocketManager.option(okOptionsBuilder.build());
        //调用通道进行连接
        mSocketManager.registerReceiver(adapter);
        mSocketManager.connect();


    }

    public BPulse mPulseData = new BPulse();
    private SocketActionAdapter adapter = new SocketActionAdapter() {

        @Override
        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
            ToolLog.e(TAG, "连接成功(Connecting Successful)");
            //连接成功其他操作...
            //链式编程调用,给心跳管理器设置心跳数据,一个连接只有一个心跳管理器,因此数据只用设置一次,如果断开请再次设置.
           /* OkSocket.open(info)
                    .getPulseManager()
                    .setPulseSendable(mPulseData)//只需要设置一次,下一次可以直接调用pulse()
                    .pulse();//开始心跳,开始心跳后,心跳管理器会自动进行心跳触发*/
            mSocketManager.getPulseManager().setPulseSendable(mPulseData).pulse();

        }

        @Override
        public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
            ToolLog.e(TAG, "onSocketDisconnection");
            showError("socket断开连接");

        }

        @Override
        public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
            ToolLog.e(TAG, "onSocketConnectionFailed");
            showError("socket连接失败");
        }

        @Override
        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
            byte[] b = data.getBodyBytes();
            String str = new String(b, Charset.forName("utf-8"));
            ToolLog.e(TAG, "onSocketReadResponse: " + action + "     " + b.length + "  " + str);
            //  JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
            // String type = jsonObject.get("type").getAsString();
            //处理socket消息
            Message msg = Message.obtain();
            msg.what = IConfigs.MSG_SOCKET_RECEIVED;
            msg.obj = str;
            mDataHandler.sendMessage(msg);

        }

        @Override
        public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
            byte[] bytes = data.parse();
            bytes = Arrays.copyOfRange(bytes, 4, bytes.length);
            String str = new String(bytes, Charset.forName("utf-8"));
            ToolLog.e(TAG, "onSocketWriteResponse: " + action + "     " + str);

        }

        @Override
        public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
            byte[] bytes = data.parse();
            bytes = Arrays.copyOfRange(bytes, 8, bytes.length);
            String str = new String(bytes, Charset.forName("utf-8"));
            // JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
            ToolLog.e(TAG, "发送心跳包(Heartbeat Sending)" + str);
        }
    };

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }


}
