package com.sjjd.wyl.baseandroidweb.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sjjd.wyl.baseandroidweb.bean.BRegisterResult;
import com.sjjd.wyl.baseandroidweb.bean.Result;
import com.sjjd.wyl.baseandroidweb.listeners.RegisterListener;
import com.sjjd.wyl.baseandroidweb.thread.JsonCallBack;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;
import com.sjjd.wyl.baseandroidweb.tools.ToolApp;
import com.sjjd.wyl.baseandroidweb.tools.ToolCommon;
import com.sjjd.wyl.baseandroidweb.tools.ToolDevice;
import com.sjjd.wyl.baseandroidweb.tools.ToolFile;
import com.sjjd.wyl.baseandroidweb.tools.ToolRegister;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyl on 2020/5/12.
 */
public class Presenter {

    private IView mView;
    private Context mContext;

    public Presenter(Context context, IView mView) {
        mContext = context;
        this.mView = mView;

    }

    private boolean isUping;

    /**
     * 上传截图
     *
     * @param url
     * @param base64
     * @param sessionId
     */
    public void uploadScreen(final String url, final String base64, final String sessionId) {
        if ((isUping))
            return;
        if (base64 != null && base64.length() > 0) {
            isUping = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkGo.<String>post(url)
                            .tag(this)
                            .params("macId", ToolDevice.getMac())
                            .params("sessionId", sessionId)
                            .params("baseStr", base64)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    isUping = false;
                                }

                                @Override
                                public void onError(Response<String> response) {
                                    isUping = false;
                                    mView.showError("截图上传失败");
                                }
                            });
                }
            }).start();
        } else {
            mView.showError("截图失败");
            isUping = false;

        }
    }

    boolean isCapturing = false;

    public void uploadCapture(final String url, final String base64, final String sessionId, File file) {
        if (isCapturing) return;
        isCapturing = true;
        JSONObject content = new JSONObject();
        content.put("beaseStr", base64);
        content.put("sessionId", "123");
        content.put("macId", ToolDevice.getMac());
        OkGo.<Result>post(url)
                .params("content", content.toJSONString())
                .params("checkinfo", "{\"timestamp\":\"123\",\"token\":\"123\"}")
                .params("method", IConfigs.METHOD_UPLOAD_CAPTURE)
                .params("file", file)
                .tag(this)
                .execute(new JsonCallBack<Result>(Result.class) {
                    @Override
                    public void onSuccess(Response<Result> response) {
                        isCapturing = false;
                    }

                    @Override
                    public void onError(Response<Result> response) {
                        super.onError(response);
                        isCapturing = false;
                    }
                });

    }

    private boolean isLoging = false;
    private List<File> fileList = new ArrayList<>();

    /**
     * 上传日志
     *
     * @param url
     */
    public void uploadLogs(String url) {
        if (isLoging) return;
        isLoging = true;
        JSONObject content = new JSONObject();
        content.put("beaseStr", "");
        content.put("sessionId", "123");
        content.put("mac", ToolDevice.getMac());
        File dir = new File(IConfigs.PATH_LOG);

        fileList.clear();
        if (dir.isDirectory()) {
            dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    fileList.add(pathname);
                    return false;
                }
            });
            if (fileList.size() > 0) {
                OkGo.<Result>post(url)
                        .params("content", content.toJSONString())
                        .params("checkinfo", "{\"timestamp\":\"123\",\"token\":\"123\"}")
                        .params("method", IConfigs.METHOD_UPLOAD_LOG)
                        // .params("file", files[0])//单个文件
                        .addFileParams("file[]", fileList)//文件集合

                        .tag(this)
                        .execute(new JsonCallBack<Result>(Result.class) {
                            @Override
                            public void onSuccess(Response<Result> response) {
                                isLoging = false;
                            }

                            @Override
                            public void onError(Response<Result> response) {
                                super.onError(response);
                                isLoging = false;
                            }
                        });
            }
        }

    }

    /**
     * 检查权限
     *
     * @param mPermissions
     */
    public void checkPermission(String[] mPermissions) {
        if (mPermissions != null && mPermissions.length > 0) {
            if (AndPermission.hasPermissions(mContext, mPermissions)) {
                mView.initData();
            } else {
                AndPermission.with(mContext)
                        .runtime()
                        .permission(mPermissions)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                mView.initData();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                mView.showError("权限请求被拒绝将无法正常使用！");
                            }
                        })
                        .start();
            }
        } else {
            mView.showError("请到》设置》应用 授权！");
        }
    }

    /**
     * 下载更新软件
     *
     * @param url
     */
    public void downloadApk(String url) {
        OkGo.<File>get(url)
                .tag(this)
                .execute(new FileCallback(IConfigs.PATH_APK, "") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        final File apk = response.body();
                        if (apk != null) {
                            String mPackageName = ToolApp.getPackageName(mContext, apk.getAbsolutePath());
                            if (mPackageName.equals(mContext.getPackageName())) {//包名一样  是正确的apk
                                //如果更新包的apk的版本号大 则更新apk
                                Intent intentapk = new Intent(Intent.ACTION_VIEW);
                                intentapk.setDataAndType(Uri.fromFile(apk),
                                        "application/vnd.android.package-archive");
                                intentapk.putExtra("IMPLUS_INSTALL", "SILENT_INSTALL");
                                mContext.startActivity(intentapk);

                            } else {
                                mView.showError("应用程序不匹配");
                            }
                        }
                    }

                });
    }

    public void checkRegister(RegisterListener listener) {
        BRegisterResult mRegisterResult = ToolRegister.getInstance(mContext).checkDeviceRegistered();
        if (listener != null)
            listener.RegisterCallBack(mRegisterResult);
    }
}
