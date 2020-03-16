package com.sjjd.wyl.baseandroid.base;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.sjjd.wyl.baseandroid.tools.IConfigs;

import java.lang.ref.WeakReference;

/**
 * Created by wyl on 2019/5/22.
 */
public class BaseDataHandler extends Handler {
    WeakReference<Activity> mReference;

    public interface MessageListener {
        void showError(String error);

        void userHandler(Message msg);
    }

    public MessageListener mMessageListener;

    public void setMessageListener(MessageListener errorListener) {
        mMessageListener = errorListener;
    }

    public BaseDataHandler(Activity reference) {
        mReference = new WeakReference<>(reference);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        //常用消息处理
        switch (msg.what) {
            case IConfigs.NET_CONNECT_ERROR:
            case IConfigs.NET_SERVER_ERROR:
            case IConfigs.NET_UNKNOWN_ERROR:
            case IConfigs.NET_TIMEOUT:
                if (mMessageListener != null)
                    mMessageListener.showError(msg.obj == null ? "处理异常" : (String) msg.obj);
                break;

        }
        //自定义消息处理
        if (mMessageListener != null)
            mMessageListener.userHandler(msg);
    }
}
