package com.sjjd.wyl.baseandroidweb.tools;

import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.sjjd.wyl.baseandroidweb.bean.BVoiceSetting;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wyl on 2019/2/25.
 */
public class ToolTts {
    private static final String TAG = "ToolTts";
    SpeechSynthesizer mTTSPlayer;//呼叫播放

    static ToolTts mToolTts;
    static Object mObject = new Object();
    static Context mContext;

    public static ToolTts getInstance(Context context) {
        mContext = context;
        if (mToolTts == null) {
            synchronized (mObject) {
                if (mToolTts == null) {
                    mToolTts = new ToolTts();
                }
            }
        }
        return mToolTts;
    }

    public SpeechSynthesizer getTTSPlayer() {
        return mTTSPlayer;
    }

    public String defaultDir = IConfigs.PATH_TTS;

    String frontName = "frontend_model";
    String backName = "backend_female";


    class Config {

        public static final String appKey = "medtrhg7qrnnhkxploclzxezjumq667zc3l3rkaf";
        public static final String secret = "bbe919b0d4234c4b0f13ebfeb4e7173f";
    }


    public void copyFile() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ToolLog.e(TAG, "run: 复制语音文件：");
                File dir = new File(defaultDir);
                if (!dir.exists())
                    dir.mkdirs();

                {
                    LogUtils.file("复制语音文件:");
                    File mFileFont = new File(defaultDir + frontName);
                    File mFileBack = new File(defaultDir + backName);
                    try {
                        InputStream front = mContext.getAssets().open(frontName);
                        byte[] frontmBytes = new byte[1024];
                        mFileFont.delete();
                        FileOutputStream frontmBytesfos = new FileOutputStream(mFileFont);
                        while ((front.read(frontmBytes, 0, frontmBytes.length)) != -1) {
                            frontmBytesfos.write(frontmBytes);
                        }
                        frontmBytesfos.close();
                        front.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        InputStream front = mContext.getAssets().open(backName);
                        byte[] frontmBytes = new byte[1024];
                        mFileBack.delete();
                        FileOutputStream frontmBytesfos = new FileOutputStream(mFileBack);
                        while ((front.read(frontmBytes, 0, frontmBytes.length)) != -1) {
                            frontmBytesfos.write(frontmBytes);
                        }
                        frontmBytesfos.close();
                        front.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File mFile = new File(defaultDir + "tts.txt");

                    FileIOUtils.writeFileFromString(mFile, backName + ":" + FileUtils.getSize(mFileBack) + "\n" + frontName + ":" + FileUtils.getSize(mFileFont));
                }
            }
        }).

                start();


    }

    public boolean existsTTsFile() {
        return existsTTsFile(defaultDir);
    }

    /**
     * 指定目录下是否存在语音播放文件
     *
     * @param dir
     * @return
     */
    public boolean existsTTsFile(String dir) {

        if (dir == null || dir.length() < 1) {
        } else {
            defaultDir = dir.endsWith("/") ? dir : dir + "/";
        }
        File back = new File(defaultDir + backName);
        File front = new File(defaultDir + frontName);

        File txt = new File(defaultDir + "tts.txt");

        if (back.isFile() && back.exists() && front.isFile() && front.exists() && txt.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 初始化呼叫
     */
    public void initTts(Context context) {
        // 初始化语音合成对象
        mTTSPlayer = new SpeechSynthesizer(context, Config.appKey, Config.secret);
        // 设置本地合成
        mTTSPlayer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_LOCAL);
        // 设置前端模型
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_FRONTEND_MODEL_PATH, defaultDir + frontName);
        // 设置后端模型
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_BACKEND_MODEL_PATH, defaultDir + backName);
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_SPEED, 30);
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_VOLUME, 100);

        // 设置回调监听
        mTTSPlayer.setTTSListener(new SpeechSynthesizerListener() {

            @Override
            public void onEvent(int type) {
                switch (type) {
                    case SpeechConstants.TTS_EVENT_INIT:
                        // 初始化成功回调
                        ToolLog.e(TAG, "onEvent: TTS_EVENT_INIT");
                        break;
                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_START:
                        ToolLog.e(TAG, "onEvent: TTS_EVENT_SYNTHESIZER_START");
                        // 开始合成回调
                        break;
                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_END:
                        ToolLog.e(TAG, "onEvent: TTS_EVENT_SYNTHESIZER_END");
                        // 合成结束回调
                        break;
                    case SpeechConstants.TTS_EVENT_BUFFER_BEGIN:
                        // 开始缓存回调
                        ToolLog.e(TAG, "onEvent: TTS_EVENT_BUFFER_BEGIN");
                        break;
                    case SpeechConstants.TTS_EVENT_BUFFER_READY:
                        // 缓存完毕回调
                        ToolLog.e(TAG, "onEvent: TTS_EVENT_BUFFER_READY");
                        break;
                    case SpeechConstants.TTS_EVENT_PLAYING_START:
                        // 开始播放回调
                        ToolLog.e(TAG, "onEvent: TTS_EVENT_PLAYING_START");
                        break;
                    case SpeechConstants.TTS_EVENT_PLAYING_END:
                        ToolLog.e(TAG, "onEvent: TTS_EVENT_PLAYING_END");
                        break;
                    case SpeechConstants.TTS_EVENT_PAUSE:
                        // 暂停回调
                        break;
                    case SpeechConstants.TTS_EVENT_RESUME:
                        // 恢复回调
                        break;
                    case SpeechConstants.TTS_EVENT_STOP:
                        // 停止回调
                        ToolLog.e(TAG, "onEvent: TTS_EVENT_STOP");
                        break;
                    case SpeechConstants.TTS_EVENT_RELEASE:
                        // 释放资源回调
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onError(int type, String errorMSG) {
                // 语音合成错误回调
            }
        });
        // 初始化合成引擎
        int mInit = mTTSPlayer.init("");
        ToolLog.e(TAG, "initTts: " + mInit);
    }

    public void initTtsSetting(BVoiceSetting mVoiceSetting) {
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_VOLUME, 100);
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_SPEED, (mVoiceSetting.getVoSpeed().length() > 0 ? Integer.parseInt(mVoiceSetting.getVoSpeed()) * 10 : 30));
        /*voiceFormat = mVoiceSetting.getVoFormat();
        String mNumber = mVoiceSetting.getVoNumber();
        if (mNumber.length() > 0) {
            voiceCount = Integer.parseInt(mNumber);
            voiceCount = voiceCount > 0 ? voiceCount : 1;
        }*/
        //  mTTSPlayer.setOption(SpeechConstants.TTS_KEY_BACKEND_MODEL_PATH, TTSManager.getInstance(mContext).defaultDir + TTSManager.getInstance(mContext).backName);
    }
}
