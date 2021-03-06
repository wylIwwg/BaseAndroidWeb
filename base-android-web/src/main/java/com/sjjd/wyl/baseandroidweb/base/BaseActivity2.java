package com.sjjd.wyl.baseandroidweb.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sjjd.wyl.baseandroidweb.R;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;
import com.sjjd.wyl.baseandroidweb.tools.ToolDisplay;
import com.sjjd.wyl.baseandroidweb.view.MEditView;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class BaseActivity2 extends AppCompatActivity implements BaseDataHandler.MessageListener {
    public String TAG = this.getClass().getSimpleName();
    public Context mContext;
    public BaseDataHandler mDataHandler;
    public String HOST = "";
    public boolean isRegistered = false;
    public String MARK = "";//软件类型

    public String REGISTER_STR = "";
    public String Client_ID = "";
    public int RegisterCode = 0;
    public String METHOD_AREA;
    public String[] PERMISSIONS;

    public LinearLayout mllContentRoot;//内容根布局
    public DrawerLayout mDrawer;//抽屉根布局
    public LinearLayout navigationView;//侧滑布局

    public MEditView mEtServerIp;
    public MEditView mEtServerPort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity2_base);

        mContext = this;

        mllContentRoot = findViewById(R.id.llContentRoot);
        mDrawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.llLeftDrawer);

        mEtServerIp = findViewById(R.id.etServerIp);
        mEtServerPort = findViewById(R.id.etServerPort);


        mDataHandler = new BaseDataHandler(this);
        mDataHandler.setMessageListener(this);


      /*  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();*/

        // hasPermission();

    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {

        if (mllContentRoot == null) return;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mllContentRoot.addView(view, lp);
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToolDisplay.hideBottomUIMenu(this);
    }

    public void hasPermission() {
      /*  if (PERMISSIONS != null && PERMISSIONS.length > 0) {
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
        }/*/
    }

    PopupWindow popLoading;

    public void closeLoading() {
        if (popLoading != null && popLoading.isShowing()) {
            popLoading.dismiss();
        }
    }

    public void showLoading(String tips, View view) {

        View mSettingView = LayoutInflater.from(mContext).inflate(R.layout.layout_loading, null);
        popLoading = new PopupWindow(mSettingView, ToolDisplay.dip2px(mContext, 200), ToolDisplay.dip2px(mContext, 200), true);
        popLoading.setFocusable(false);// 点击back退出pop
        popLoading.setOutsideTouchable(false);
        // popLoading.setElevation(5);
        TextView tv = mSettingView.findViewById(R.id.tvLoadTips);
        tv.setText(tips);
        popLoading.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        if (!popLoading.isShowing()) {
            popLoading.showAtLocation(view, Gravity.CENTER, 1, 1);
        }
        ToolDisplay.hideBottomUIMenu(this);
        popLoading.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ToolDisplay.hideBottomUIMenu((Activity) mContext);
            }
        });


    }

    public void showLoading(String tips) {
        showLoading(tips, mllContentRoot);


    }

    public void initData() {


    }

    public void initListener() {

    }

    @Override
    public void showError(String error) {
        Toasty.error(mContext, error, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void userHandler(Message msg) {
        switch (msg.what) {
            case IConfigs.MSG_CREATE_TCP_ERROR:
            case IConfigs.MSG_PING_TCP_TIMEOUT:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDataHandler != null) {
            mDataHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                hideKeyboard(ev, view);//调用方法判断是否需要隐藏键盘
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void hideKeyboard(MotionEvent event, View view) {
        try {
            if (view != null && view instanceof EditText) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null)
                        inputMethodManager.hideSoftInputFromWindow(token,
                                InputMethodManager.HIDE_NOT_ALWAYS);

                    ToolDisplay.hideBottomUIMenu(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
