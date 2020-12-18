package com.sjjd.wyl.baseandroidweb.tools;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by wyl on 2018/5/22.
 */

public class ToolLog {

    public static String TAG = "ToolLog";
    public static boolean showLog = true;//是否打印日志
    static int LOG_MAX_LENGTH = 2000;

    public static void i(String tag, String i) {
        Log.e(tag, i);
    }

    public static void i(String i) {
        Log.e(TAG, i);
    }

    public static void e(String msg) {
        if (showLog) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAX_LENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.e(TAG + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAX_LENGTH;
                } else {
                    Log.e(TAG + i, msg.substring(start, strLength));
                    break;
                }

            }
        }
    }

    public static void e(String tag, String msg) {
        if (showLog) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAX_LENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.e(tag + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAX_LENGTH;
                } else {
                    Log.e(tag + i, msg.substring(start, strLength));
                    break;
                }

            }
        }
    }


    public static void efile(String tag, String msg) {

        e(tag, msg);
        LogUtils.file(tag, msg);
    }

    public static void efile(String msg) {

        e(TAG, msg);
        LogUtils.file(TAG, msg);
    }
}
