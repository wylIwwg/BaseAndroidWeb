package com.sjjd.wyl.basedemo;

import android.content.Context;
import android.util.AttributeSet;

import com.sjjd.wyl.baseandroidweb.tools.ToolLog;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

/**
 * Created by wyl on 2018/7/7.
 */

public class SimpleJZPlayer extends JzvdStd {

    SimpleJZPlayer player;
    int screen;
    String url;


    public SimpleJZPlayer(Context context) {
        super(context);
        player = this;

    }

    public SimpleJZPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        player = this;

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_player;
    }


    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);

    }

    //onState 代表了播放器引擎的回调，播放视频各个过程的状态的回调


    //changeUiTo 真能能修改ui的方法
    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();
    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
    }

    @Override
    public void changeUiToError() {
        super.changeUiToError();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }


    @Override
    public void updateStartImage() {
        super.updateStartImage();
        if (state == STATE_PLAYING) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(cn.jzvd.R.drawable.jz_click_pause_selector);
            replayTextView.setVisibility(INVISIBLE);
        } else if (state == STATE_ERROR) {
            startButton.setVisibility(INVISIBLE);
            replayTextView.setVisibility(INVISIBLE);
        } else if (state == STATE_AUTO_COMPLETE) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(cn.jzvd.R.drawable.jz_click_replay_selector);
            replayTextView.setVisibility(VISIBLE);
        } else {
            // startButton.setImageResource(R.drawable.player);
            replayTextView.setVisibility(INVISIBLE);
        }
    }


    @Override
    public void onStateError() {
        super.onStateError();
        ToolLog.e(TAG, "onStateError: ");
    }

    @Override
    public void onStateNormal() {
        ToolLog.e(TAG, "onStateNormal: ");
        super.onStateNormal();
    }


    @Override
    public void onScreenStateChanged(int screenState) {
        ToolLog.e(TAG, "onScreenStateChanged: ");
        super.onScreenStateChanged(screenState);
    }

    @Override
    public void onStatePause() {
        ToolLog.e(TAG, "onStatePause: ");
        super.onStatePause();
    }

    @Override
    public void onStateAutoComplete() {
        ToolLog.e(TAG, "onStateAutoComplete: ");
        super.onStateAutoComplete();
        if (mCompleteListener != null) {
            mCompleteListener.complete(player, url, screen);
        }
    }

    StartPlayingListener mStartListener;

    public void setStartPlayingListener(StartPlayingListener listener) {
        mStartListener = listener;
    }

    public interface StartPlayingListener {
        void start();

    }

    CompleteListener mCompleteListener;

    public void setOnCompleteListener(CompleteListener listener) {
        mCompleteListener = listener;
    }

    public interface CompleteListener {
        void complete(SimpleJZPlayer player, String url, int screen);

    }

    @Override
    public void onStatePreparing() {
        ToolLog.e(TAG, "onStatePreparing: ");
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        ToolLog.e(TAG, "onStatePlaying: ");
        super.onStatePlaying();
        if (mStartListener != null) {
            mStartListener.start();
        }
    }
}
