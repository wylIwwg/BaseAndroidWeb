package com.sjjd.wyl.baseandroidweb.appTopService;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.sjjd.wyl.baseandroidweb.tools.ToolApp;
import com.sjjd.wyl.baseandroidweb.tools.ToolLog;

/**
 * Created by wyl on 2019/8/30.
 */
public class AppTopLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = " AppTopLifecycleHandler ";
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        ++paused;
        if (activity != null) {
            ToolLog.e(TAG, "onActivityPaused: " + "application is visible: " + activity.getLocalClassName() + "   " + (stopped > started));
            if (activity.getLocalClassName().contains("MainActivity")) {
                ToolApp.restartApp(activity);
                ToolLog.e(TAG, "onActivityPaused: 重启 ");
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        if (activity != null) {
            ToolLog.e(TAG, "onActivityStopped: " + "application is visible: " + activity.getLocalClassName() + "   " + (stopped > started));
        }


    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        // 当所有 Activity 的状态中处于 resumed 的大于 paused 状态的，即可认为有Activity处于前台状态中
        return resumed > paused;
    }
}