package com.sjjd.wyl.baseandroid.tools;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by wyl on 2018/10/23.
 */
public class ToolToast {
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text, int duration) {
        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        mHandler.postDelayed(r, duration);
        mToast.show();
    }

    public static void clearToast() {
        if (mHandler != null && mToast != null) {
            mHandler.removeCallbacks(r);
            mToast = null;
        }
    }

}
