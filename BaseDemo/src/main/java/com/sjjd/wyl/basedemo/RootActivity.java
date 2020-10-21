package com.sjjd.wyl.basedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;

public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);



    }

    public void hardwareTest(View view) {
        startActivity(new Intent(this, HardwareActivity.class));
    }

    public void toMain(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void systemSetting(View view) {
        startActivity(new Intent(this, SettingActivity.class));
    }
}
