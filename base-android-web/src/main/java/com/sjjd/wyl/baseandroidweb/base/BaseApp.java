package com.sjjd.wyl.baseandroidweb.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.sjjd.wyl.baseandroidweb.R;
import com.sjjd.wyl.baseandroidweb.anr.ANRThread;
import com.sjjd.wyl.baseandroidweb.appTopService.AppTopLifecycleHandler;
import com.sjjd.wyl.baseandroidweb.appTopService.AppTopService;
import com.sjjd.wyl.baseandroidweb.crash.config.CrashConfig;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;
import com.sjjd.wyl.baseandroidweb.tools.ToolApp;
import com.sjjd.wyl.baseandroidweb.tools.ToolLZ;
import com.sjjd.wyl.baseandroidweb.tools.ToolLog;
import com.sjjd.wyl.baseandroidweb.tools.ToolSP;
import com.sjjd.wyl.baseandroidweb.tools.ToolTts;

import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;

/**
 * Created by wyl on 2018/11/13.
 */
public class BaseApp extends Application {

    public static final String TAG = " 【Application】 ";
    public Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        new ANRThread().start();
        mContext = getApplicationContext();
        Toasty.Config.getInstance()
                .allowQueue(false)
                .setTextSize(18)
                .apply();

        ToolSP.Init(mContext, getPackageName());

        Utils.init(this);

        if (Build.USER.contains("liaokai")) {
            ToolLZ.Init(this);
        }
        LogUtils.getConfig().setDir(IConfigs.PATH_LOG).setFilePrefix("log");


    }

    /**
     * 删除指定时间内的文件  保留
     *
     * @param days
     */
    public void deleteFiles(final int days) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.listFilesInDirWithFilter(IConfigs.PATH_LOG, new FileFilter() {
                    @Override
                    public boolean accept(File mFile) {
                        long mLastModified = mFile.lastModified();
                        long date = 1000 * 60 * 60 * 24 * days;
                        if (System.currentTimeMillis() - mLastModified > date) {
                            FileUtils.delete(mFile);
                        }
                        return false;
                    }
                });
            }
        }).start();
    }

    /**
     * 删除指定时间内的文件  保留
     *
     * @param days
     */
    public void deleteFiles(final String path, final int days) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.listFilesInDirWithFilter(path, new FileFilter() {
                    @Override
                    public boolean accept(File mFile) {
                        long mLastModified = mFile.lastModified();
                        long date = 1000 * 60 * 60 * 24 * days;
                        if (System.currentTimeMillis() - mLastModified > date) {
                            FileUtils.delete(mFile);
                        }
                        return false;
                    }
                });
            }
        }).start();
    }

    public void initTopService() {
        registerActivityLifecycleCallbacks(new AppTopLifecycleHandler());

        //3秒之后启动服务
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent service = new Intent(getApplicationContext(), AppTopService.class);
                startService(service);
            }
        }, 5000);
    }


    public void initDebug(Class<Activity> activity) {
        CrashConfig.Builder.create()
                .backgroundMode(CrashConfig.BACKGROUND_MODE_SILENT) //default: CrashConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(true) //default: true
                .showErrorDetails(true) //default: true
                .showRestartButton(true) //default: true
                .logErrorOnRestart(true) //default: true
                .trackActivities(false) //default: false
                .minTimeBetweenCrashesMs(2000) //default: 3000
                .errorDrawable(R.drawable.error) //default: bug image
                .restartActivity(activity) //default: null (your app's launch activity)
                .errorActivity(activity) //default: null (default error activity)
                .eventListener(null) //default: null
                .apply();
    }


    public void initOkGO() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        //添加OkGo默认debug日志
        builder.addInterceptor(loggingInterceptor);

        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));


        //超时时间设置，默认60秒
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(5000L, TimeUnit.MILLISECONDS);


        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(1)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
        ;
    }


    /**
     * 用于程序崩溃重启
     */
    public boolean restart = true;

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    public void initCrashRestart() {
        Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程

    }

    /**
     * 添加崩溃重启选择
     *
     * @param restart
     */
    public void initCrashRestart(boolean restart) {
        setRestart(restart);
        Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程

    }

    // 创建服务用于捕获崩溃异常
    public Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            handleException(ex);
            try {
                Thread.sleep(500 * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    //保存错误日志
    public void handleException(Throwable ex) {
        String s = formatCrashInfo(ex);
        LogUtils.file(TAG, s);
        if (restart) {
            String message = ex.getMessage();
            //如果大于10
            int count = ToolSP.getDIYInt(message);
            count += 1;
            ToolLog.e("【count】", message + "  " + count + "");
            ToolSP.putDIYInt(message, count);
            if (count > 5) {
                //不再重启
            } else {

                ToolApp.restartApp(mContext);
            }
        }

    }


    public String formatCrashInfo(Throwable ex) {
        String lineSeparator = "\r\n";

        StringBuilder sb = new StringBuilder();

        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        String dump = info.toString();
        String exception = "exception:" + "{\n" + dump + "\n}";
        printWriter.close();


        sb.append("----------------------------").append(lineSeparator);
        sb.append(exception).append(lineSeparator);
        sb.append("----------------------------").append(lineSeparator);

        return sb.toString();

    }


    private String getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(date);
        return time;
    }


}
