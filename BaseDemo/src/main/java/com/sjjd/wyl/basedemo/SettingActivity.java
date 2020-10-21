package com.sjjd.wyl.basedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ReflectUtils;
import com.sjjd.wyl.baseandroidweb.adapter.CommonAdapter;
import com.sjjd.wyl.baseandroidweb.adapter.ViewHolder;
import com.sjjd.wyl.baseandroidweb.base.IDescription;
import com.sjjd.wyl.baseandroidweb.bean.BBaseSetting;
import com.sjjd.wyl.baseandroidweb.bean.ConfigSetting;
import com.sjjd.wyl.baseandroidweb.tools.IConfigs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = " SettingActivity ";
    @BindView(R.id.btnGetAll)
    Button mBtnGetAll;
    @BindView(R.id.rlvSetting)
    RecyclerView mRlvSetting;

    List<ConfigSetting> datas = new ArrayList<>();
    CommonAdapter<ConfigSetting> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        String mS = FileIOUtils.readFile2String(IConfigs.PATH_LOG + "/setting.txt");
        mSetting = JSON.parseObject(mS, BBaseSetting.class);

        mAdapter = new CommonAdapter<ConfigSetting>(this, R.layout.item_settings, datas) {
            @Override
            protected void convert(ViewHolder holder, ConfigSetting configSetting, int position) {
                holder.setText(R.id.tvDesc, configSetting.getDescription());
                EditText mView = (EditText) holder.getView(R.id.etContent);
                holder.setText(R.id.etContent, configSetting.getValue());
                if (!configSetting.getDescription().endsWith(":")) {
                    mView.setEnabled(false);
                } else {
                    if (mView.getTag() != null && mView.getTag() instanceof TextWatcher) {
                        mView.removeTextChangedListener((TextWatcher) mView.getTag());
                    }

                    TextWatcher intervalTextWatcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                           // datas.get(position).setValue(s.toString());
                            ReflectUtils.reflect(mSetting).field(configSetting.getName(), s.toString());

                        }
                    };

                    mView.addTextChangedListener(intervalTextWatcher);
                    mView.setTag(intervalTextWatcher);

                }
            }
        };

        mRlvSetting.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRlvSetting.setAdapter(mAdapter);


    }

    BBaseSetting mSetting;

    @OnClick(R.id.btnGetAll)
    public void onViewClicked() {

        Field[] mDeclaredFields = mSetting.getClass().getDeclaredFields();
        datas.clear();
        for (Field f : mDeclaredFields) {
            IDescription mAnnotation = f.getAnnotation(IDescription.class);
            f.setAccessible(true);
            try {
                ConfigSetting c = new ConfigSetting();
                c.setDescription(mAnnotation.value());
                c.setName(f.getName());
                c.setValue(f.get(mSetting) == null ? "" : f.get(mSetting).toString());
                datas.add(c);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}