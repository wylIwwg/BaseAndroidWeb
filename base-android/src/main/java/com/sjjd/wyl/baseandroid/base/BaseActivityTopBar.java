package com.sjjd.wyl.baseandroid.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjjd.wyl.baseandroid.R;
import com.sjjd.wyl.baseandroid.tools.ToolApp;
import com.sjjd.wyl.baseandroid.tools.IConfigs;
import com.sjjd.wyl.baseandroid.tools.ToolSP;
import com.sjjd.wyl.baseandroid.tools.ToolToast;

import java.lang.ref.WeakReference;

public class BaseActivityTopBar extends AppCompatActivity {

    public String TAG = this.getClass().getSimpleName();
    public Context mContext;
    public ImageView mImgBack;//返回
    public TextView mTvBack;//返回
    public TextView mTvTitleMiddle;//中间标题
    public TextView mTvTitleLeft;//左边标题
    public LinearLayout mBaseLlRoot;//根布局
    public RelativeLayout mRlTopBar;//顶部header

    public DataHandler mDataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_topbar);
        mContext = this;
        mImgBack = findViewById(R.id.imgBack);
        mTvBack = findViewById(R.id.tvBack);
        mTvTitleMiddle = findViewById(R.id.tvTitleMiddle);
        mTvTitleLeft = findViewById(R.id.tvTitleLeft);
        mBaseLlRoot = findViewById(R.id.baseLlRoot);
        mRlTopBar = findViewById(R.id.rlTopBar);
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
    }

    private void close() {
        this.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu();
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    //设置左边标题
    public void setLeftTitleName(String title) {
        if (mTvTitleLeft != null && title != null) {
            mTvTitleLeft.setText(title);
        }
    }

    //设置中间标题
    public void setMiddleTitleName(String title) {
        if (mTvTitleMiddle != null && title != null) {
            mTvTitleMiddle.setText(title);
        }
    }

    @Override
    public void setContentView(View view) {

        if (mBaseLlRoot == null) return;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBaseLlRoot.addView(view, lp);
    }

    public void hideBottomUIMenu() {
        //隐藏虚拟按键，并全屏
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //for new api versions
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;

            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    class DataHandler extends Handler {
        WeakReference<Activity> mReference;

        public DataHandler(Activity reference) {
            mReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //常用消息处理
            switch (msg.what) {
                case IConfigs.NET_CONNECT_ERROR:
                case IConfigs.NET_SERVER_ERROR:
                case IConfigs.NET_UNKNOWN_ERROR:
                case IConfigs.NET_TIMEOUT:
                    showError(msg.obj == null ? "处理异常" : (String) msg.obj);
                    break;

            }
            //自定义消息处理
            userHandler(msg);
        }

    }

    public void userHandler(Message msg) {

    }

    private void showError(String error) {
        ToolToast.showToast(mContext, error, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDataHandler != null) {
            mDataHandler.removeCallbacksAndMessages(null);
        }
        ToolToast.clearToast();
    }

    /**
     * @param anchor
     * @param bgColor
     * @param txtColor
     * @param etbg
     * @param btnbg
     */
    public void showSettingPop(View anchor, int bgColor, int txtColor, int etbg, int btnbg) {

        View view = View.inflate(mContext, R.layout.pop_setting, null);
        final PopupWindow pop = new PopupWindow(view, 600,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop.setFocusable(true);// 点击back退出pop
        pop.setOutsideTouchable(true);
        pop.setClippingEnabled(false);
        pop.setAnimationStyle(R.style.PopupAnimation);
        pop.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        if (!pop.isShowing()) {
            pop.showAsDropDown(anchor);
            // pop.showAtLocation(mActivityMain, Gravity.CENTER, 1, 1);
        }
        hideBottomUIMenu();
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideBottomUIMenu();
            }
        });

        final EditText etIp = view.findViewById(R.id.etIp);
        final EditText etNum = view.findViewById(R.id.etWinNum);
        final EditText etPort = view.findViewById(R.id.etPort);

        final Button mBtnCommit = view.findViewById(R.id.btnConfirm);

        final TextView labelNum = view.findViewById(R.id.labelNum);
        final TextView labelPort = view.findViewById(R.id.labelPort);
        final TextView labelIp = view.findViewById(R.id.labelIp);

        final LinearLayout popRoot = view.findViewById(R.id.popRoot);

        if (bgColor != -1) {
            popRoot.setBackgroundResource(bgColor);
        }
        if (etbg != -1) {
            etIp.setBackgroundResource(etbg);
            etNum.setBackgroundResource(etbg);
            etPort.setBackgroundResource(etbg);
        }
        if (txtColor != -1) {
            etIp.setTextColor(getResources().getColor(txtColor));
            etNum.setTextColor(getResources().getColor(txtColor));
            etPort.setTextColor(getResources().getColor(txtColor));

            labelIp.setTextColor(getResources().getColor(txtColor));
            labelPort.setTextColor(getResources().getColor(txtColor));
            labelNum.setTextColor(getResources().getColor(txtColor));
        }
        if (btnbg != -1) {
            mBtnCommit.setBackgroundResource(btnbg);
        }

        etIp.setText(ToolSP.init(mContext).getDIYString(IConfigs.SP_IP));
        etNum.setText(ToolSP.init(mContext).getDIYString(IConfigs.SP_WINDOW_NUM));
        etPort.setText(ToolSP.init(mContext).getDIYString(IConfigs.SP_PORT_HTTP));
        //提交
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新启动应用
                ToolSP.init(mContext).putDIYString(IConfigs.SP_IP, etIp.getText().toString());
                ToolSP.init(mContext).putDIYString(IConfigs.SP_WINDOW_NUM, etNum.getText().toString());
                ToolSP.init(mContext).putDIYString(IConfigs.SP_PORT_HTTP, etPort.getText().toString());
                shouldDestory();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToolApp.restartApp(mContext);
                    }
                }, 500);
                if (pop.isShowing()) {
                    pop.dismiss();
                }

            }

        });
    }

    public void shouldDestory() {

    }

    public void showSettingPop(View anchor) {
        showSettingPop(anchor, -1, -1, -1, -1);
    }
}