package com.sjjd.wyl.baseandroidweb.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wyl on 2018/5/14.
 */

public class ToolSP {
    static ToolSP mToolSP = new ToolSP();
    static SharedPreferences sp;

    public static ToolSP init(Context context, String pkg) {
        if (sp == null) {
            sp = context.getSharedPreferences(pkg, Context.MODE_PRIVATE);
        }
        return mToolSP;
    }

    public static ToolSP putDIYString(String key, String value) {

        sp.edit().putString(key, value).apply();
        return mToolSP;
    }

    public static ToolSP putDIYBoolean(String key, boolean value) {

        sp.edit().putBoolean(key, value).apply();
        return mToolSP;
    }

    public static ToolSP putDIYInt(String key, int value) {

        sp.edit().putInt(key, value).apply();
        return mToolSP;
    }

    public static String getDIYString(String key) {
        return sp.getString(key, "");
    }

    public static boolean getDIYBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public static int getDIYInt(String key) {
        return sp.getInt(key, -1);
    }
}
