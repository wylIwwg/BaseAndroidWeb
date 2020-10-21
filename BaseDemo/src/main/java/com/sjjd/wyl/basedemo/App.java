package com.sjjd.wyl.basedemo;

import android.os.Environment;

import com.sjjd.wyl.baseandroidweb.base.BaseApp;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;

/**
 * Created by wyl on 2019/2/25.
 */
public class App extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();

        initDebug(null);
        initOkGO();

       // initCrashRestart(false);
        // initTTs(IConfigs.PATH_TTS);


        //initTopService();
    }
}
