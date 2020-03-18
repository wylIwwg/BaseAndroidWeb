package com.sjjd.wyl.baseandroidweb.socket;

/**
 * Created by wyl on 2018/11/22.
 */
public interface  OnConnectionStateListener {
    void onSuccess();

    void onFailed(int errorCode);
}
