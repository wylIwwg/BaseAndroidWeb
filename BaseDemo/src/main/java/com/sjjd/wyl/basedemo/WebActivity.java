package com.sjjd.wyl.basedemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;
import com.sjjd.wyl.baseandroidweb.base.BaseWebActivity;
import com.sjjd.wyl.baseandroidweb.tekcos.SocketManager;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;
import com.sjjd.wyl.baseandroidweb.tools.ToolLog;

public class WebActivity extends BaseWebActivity {
    AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout-land.activity_web);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) mBaseLlRoot, new LinearLayout.LayoutParams(-1, -1))
                .closeIndicator()
               // .setAgentWebWebSettings(new CustomSettings())
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go("http://doc.wyliwwg.com");


        // mWeb.loadUrl("http://doc.wyliwwg.com");

        SocketManager.getInstance(mContext).startTcpConnection("192.168.2.188", "8282");
        SocketManager.getInstance(mContext).setTIME_OUT(30 * 1000);
        SocketManager.getInstance(mContext).setHEARTBEAT_RATE(10 * 1000);
        SocketManager.getInstance(mContext).setHandler(mDataHandler);
    }

    WebSettings webSettings;

    public class CustomSettings extends AbsAgentWebSettings {
        protected CustomSettings() {
            super();
        }

        @Override
        protected void bindAgentWebSupport(AgentWeb agentWeb) {
            webSettings = getWebSettings();

            if (webSettings != null)
                initSet();
        }


    }

    @Override
    public void userHandler(Message msg) {
        super.userHandler(msg);
        switch (msg.what) {
            case IConfigs.MSG_SOCKET_RECEIVED:
                ToolLog.e(TAG, "userHandler:" + msg.obj.toString());
                mAgentWeb.getJsAccessEntrace().quickCallJs("socketMsg", msg.obj.toString());
                break;
        }
        /*String script = "javascript:JsUseEvaluateJavascript('" + text + "')";
        mWebView.evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.e("bbbb", value);
                Log.e("bbbb", PrintUtils.init(mContext).getPrinterStatus()+"");
                Log.e("bbbb", PrintUtils.init(mContext).PrintTicketData(text, value, "123")+"");   ;

            }
        });*/

    }

    public void initSet() {
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setUseWideViewPort(true);//关键点

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);

        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本

        webSettings.setAllowFileAccess(true); // 允许访问文件

        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮

        webSettings.setSupportZoom(true); // 支持缩放

        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int mDensity = metrics.densityDpi;

        Log.e("maomao", "densityDpi = " + mDensity);

        if (mDensity == 240) {

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        } else if (mDensity == 160) {

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);

        } else if (mDensity == 120) {

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);

        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        } else if (mDensity == DisplayMetrics.DENSITY_TV) {

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        } else {

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);

        }

/**
 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放

 */

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

    }

    private WebViewClient mWebViewClient = new WebViewClient() {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
        }
    };

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void initData() {
        super.initData();

    }
}
