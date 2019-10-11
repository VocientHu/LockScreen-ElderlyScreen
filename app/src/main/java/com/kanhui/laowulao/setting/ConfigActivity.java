package com.kanhui.laowulao.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.SendSMSActivity;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.setting.fragment.AppConfigFragment;
import com.kanhui.laowulao.setting.fragment.ContactFragment;
import com.kanhui.laowulao.setting.fragment.WeatherConfigFragment;

import androidx.annotation.Nullable;

public class ConfigActivity extends BaseActivity implements View.OnClickListener{

    public static final int EXTRA_WEATHER = 1;
    public static final int EXTRA_APP = 2;
    public static final int EXTRA_CONTACT = 3;

    public static final String EXTRA_TYPE = "extra_type";

    private TextView tvTitle;

    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_config;
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_send).setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_main);
        type = getIntent().getIntExtra(EXTRA_TYPE,0);
        switch (type){
            case EXTRA_APP:
                configApp();
                break;
            case EXTRA_CONTACT:
                configContact();
                break;
            case EXTRA_WEATHER:
                configWeather();
                break;
        }
    }

    private void configApp(){
        tvTitle.setText("应用配置");
        turnToFragment(AppConfigFragment.class,null,R.id.fl_content);
    }

    private void configContact(){
        tvTitle.setText("联系人配置");
        turnToFragment(ContactFragment.class,null,R.id.fl_content);
    }
    private void configWeather(){
        tvTitle.setText("日期天气配置");
        turnToFragment(WeatherConfigFragment.class,null,R.id.fl_content);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_send:
                Intent intent = new Intent(ConfigActivity.this, SendSMSActivity.class);
                intent.putExtra(SendSMSActivity.EXTRA_SMS_TYPE,type);
                startActivity(intent);
                break;
        }
    }
}
