package com.kanhui.laowulao.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.base.BaseFragment;
import com.kanhui.laowulao.setting.fragment.AppConfigFragment;
import com.kanhui.laowulao.setting.fragment.ContactFragment;
import com.kanhui.laowulao.setting.fragment.WeatherConfigFragment;

public class ConfigActivity extends BaseActivity implements View.OnClickListener{

    public static final int EXTRA_APP = 1;
    public static final int EXTRA_CONTACT = 2;
    public static final int EXTRA_WEATHER = 3;

    public static final String EXTRA_TYPE = "extra_type";

    private BaseFragment weatherConfigFragment,appConfigFragment,contactFragment;

    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);
        initView();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_main);
        int type = getIntent().getIntExtra(EXTRA_TYPE,0);
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
        }
    }
}
