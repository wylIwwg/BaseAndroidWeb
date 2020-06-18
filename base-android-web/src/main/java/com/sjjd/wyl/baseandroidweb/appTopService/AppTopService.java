package com.sjjd.wyl.baseandroidweb.appTopService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sjjd.wyl.baseandroidweb.tools.ToolLog;

/*
*    <service
            android:name=".appService.MyService"
            android:process=":service2"></service>
*
* */
public class AppTopService extends Service {
    AppTopThread mAppThread;
    private String TAG = " AppTopService  ";

    public AppTopService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ToolLog.e(TAG, "onStartCommand: ");

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppThread = new AppTopThread(getApplicationContext());
        mAppThread.start();
        ToolLog.e(TAG, "onCreate: ");

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ToolLog.e(TAG, "onDestroy: ");
    }
}
