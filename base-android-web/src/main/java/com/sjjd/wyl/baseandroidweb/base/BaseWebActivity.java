package com.sjjd.wyl.baseandroidweb.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sjjd.wyl.baseandroidweb.R;
import com.sjjd.wyl.baseandroidweb.bean.Register;
import com.sjjd.wyl.baseandroidweb.bean.RegisterResult;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;
import com.sjjd.wyl.baseandroidweb.tools.ToolDisplay;
import com.sjjd.wyl.baseandroidweb.tools.ToolLog;
import com.sjjd.wyl.baseandroidweb.tools.ToolRegister;
import com.sjjd.wyl.baseandroidweb.tools.ToolToast;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by wyl on 2020/3/16.
 */
public class BaseWebActivity extends AppCompatActivity implements BaseDataHandler.MessageListener {
    public String TAG = this.getClass().getSimpleName();
    public Context mContext;
    public LinearLayout mBaseLlRoot;//根布局
    public BaseDataHandler mDataHandler;
    public String HOST = "";
    public boolean isRegistered = false;
    public String mRegisterViper = "";
    public String mClientID = "";
    public int mRegisterCode = 0;
    public String mMark = "";
    public String[] PERMISSIONS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        mContext = this;
        mBaseLlRoot = findViewById(R.id.baseLlRoot);
        mDataHandler = new BaseDataHandler(this);
        mDataHandler.setMessageListener(this);

    }

    public void isDeviceRegistered() {
        RegisterResult mRegisterResult = ToolRegister.getInstance(mContext).checkDeviceRegistered();

        ToolLog.e(TAG, "onCreate: " + mRegisterResult);
        switch (mRegisterResult.getRegisterCode()) {
            case IConfigs.REGISTER_FORBIDDEN://禁止注册/未注册
                //请求注册
                //请求信息密文
                mRegisterViper = ToolRegister.getInstance(mContext).register2Base64(false, mMark);
                mRegisterCode = IConfigs.DEVICE_FORBIDDEN;
                break;
            case IConfigs.REGISTER_FOREVER://永久注册
                mRegisterCode = IConfigs.DEVICE_REGISTERED;
                isRegistered = true;
                break;
            default://注册时间
                Register mRegister = ToolRegister.getInstance(mContext).getRegister();
                if (mRegister != null) {
                    isRegistered = true;
                    mRegisterCode = IConfigs.DEVICE_REGISTERED;
                    long rt = Long.parseLong(mRegister.getDate());//获取注册时间
                    long mMillis = System.currentTimeMillis();//本地时间

                    Date newDate2 = new Date(rt + (long) mRegisterResult.getRegisterCode() * 24 * 60 * 60 * 1000);

                    //到期了 再次申请注册
                    if (newDate2.getTime() < mMillis) {
                        ToolToast.showToast(mContext, "设备注册已过期！", 2000);
                        mRegisterCode = IConfigs.DEVICE_OUTTIME;
                        mRegisterViper = ToolRegister.getInstance(mContext).register2Base64(false, mMark);
                        isRegistered = false;
                    }

                }

                break;
        }
    }

    public void hasPermission() {
        if (PERMISSIONS != null && PERMISSIONS.length > 0) {
            if (AndPermission.hasPermissions(mContext, PERMISSIONS)) {
                initData();
            } else {
                AndPermission.with(mContext)
                        .runtime()
                        .permission(PERMISSIONS)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                initData();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                showError("权限请求被拒绝将无法正常使用！");
                            }
                        })
                        .start();
            }
        } else {
            initWithoutPermission();
        }
    }

    public void initWithoutPermission() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ToolDisplay.hideBottomUIMenu(this);
    }


    public void initListener() {

    }

    public void initData() {
        initListener();

    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {

        if (mBaseLlRoot == null) return;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBaseLlRoot.addView(view, lp);
    }

    @Override
    public void showError(String error) {
        Toasty.error(mContext, error, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void userHandler(Message msg) {
        //

    }

    public void close() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDataHandler != null) {
            mDataHandler.removeCallbacksAndMessages(null);
        }
    }

}
