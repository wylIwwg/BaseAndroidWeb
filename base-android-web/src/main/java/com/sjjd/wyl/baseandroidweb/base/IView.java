package com.sjjd.wyl.baseandroidweb.base;

import com.sjjd.wyl.baseandroidweb.bean.BResult;
import com.sjjd.wyl.baseandroidweb.bean.BResult2;

/**
 * Created by wyl on 2020/5/12.
 */
public interface IView {

    void showSuccess(String success);

    void showMessage(BResult2 result);

    void showError(String error);

    /**
     * 下载更新软件
     *
     * @param url
     */
    void downloadApk(String url);

    /**
     * 上传截图
     *
     * @param url
     * @param base64
     */
    void uploadScreen(String url, String base64);

    /**
     * 绑定设备
     *
     * @param clientId
     */
    void addDevice(String clientId);

    void release();

    void initData();

}
