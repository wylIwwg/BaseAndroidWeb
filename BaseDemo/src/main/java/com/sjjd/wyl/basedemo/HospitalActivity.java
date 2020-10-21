package com.sjjd.wyl.basedemo;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;

import com.sjjd.wyl.baseandroidweb.base.BaseHospitalActivity;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;
import com.sjjd.wyl.baseandroidweb.tools.ToolLog;
import com.yanzhenjie.permission.runtime.Permission;

import org.json.JSONException;
import org.json.JSONObject;

public class HospitalActivity extends BaseHospitalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);

        mPermissions = new String[]{Permission.READ_PHONE_STATE, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            hasPermission();
        } else {
            initData();
        }
    }


    @Override
    public void initData() {
        super.initData();
        mIP = "192.168.2.188";
        mSocketPort = "11211";

        initSocket();
    }

    @Override
    public void initListener() {
        super.initListener();

    }


    @Override
    public void userHandler(Message msg) {
        super.userHandler(msg);
        switch (msg.what) {
            case IConfigs.MSG_SOCKET_RECEIVED:

                JSONObject mObject = null;
                try {
                    mObject = new JSONObject(msg.obj.toString());
                    String mType = mObject.getString("type");
                    switch (mType) {
                        case "test":
                            ToolLog.e(TAG, mType);
                            break;
                        case "log":
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
