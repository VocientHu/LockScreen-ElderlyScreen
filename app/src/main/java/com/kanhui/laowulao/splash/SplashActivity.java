package com.kanhui.laowulao.splash;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kanhui.laowulao.MainActivity;
import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.guide.GuideActivity;
import com.kanhui.laowulao.locker.model.ContactEngine;
import com.kanhui.laowulao.locker.model.ContactModel;
import com.kanhui.laowulao.setting.config.AppConfig;
import com.kanhui.laowulao.setting.config.ContactConfig;
import com.kanhui.laowulao.setting.config.WeatherConfig;
import com.kanhui.laowulao.utils.SharedUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity {

    public static final String SHARED_GUIDE_STATUS = "shared_guide_status";// 是否已经进入过向导
    public static final String IS_FIRST_OPEN = "is_first_open";// 是否为第一次打开，需做初始化内容

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    void init(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
        //第一次打开，初始化数据
        boolean isFirstOpen = SharedUtils.getInstance().getBoolean(IS_FIRST_OPEN,true);
        if(isFirstOpen){
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    // 天气
                    WeatherConfig weatherConfig = new WeatherConfig();
                    weatherConfig.setCitySize(Config.SS_SCALE_SMALL);
                    weatherConfig.setDateTimeSize(Config.S_SCALE_SMALL);
                    weatherConfig.setWeekSize(Config.SS_SCALE_SMALL);
                    weatherConfig.setWeatherSize(Config.S_SCALE_SMALL);
                    weatherConfig.setWeatherImgSize(Config.APP_IMG_MIDDLE);
                    SharedUtils.getInstance().setWeatherConfig(weatherConfig);

                    // 应用
                    AppConfig appConfig = new AppConfig();
                    List<String> appNames = new ArrayList<>();
                    appNames.add("微信");
                    appNames.add("联系人");
                    appNames.add("相机");

                    appConfig.setApps(appNames);
                    appConfig.setIconSize(Config.APP_IMG_MIDDLE);
                    appConfig.setNameSize(Config.APP_NAME_SMALL);
                    SharedUtils.getInstance().setAppConfig(appConfig);

                    // 联系人
                    ContactConfig contactConfig = new ContactConfig();
                    contactConfig.setNameSize(Config.SCALE_SMALL);
                    contactConfig.setPhoneSize(Config.SS_SCALE_SMALL);
                    List<ContactModel> list = ContactEngine.getInstance(SplashActivity.this).getContacts();
                    List<ContactModel> temp = new ArrayList<>();
                    if(list == null){

                    } else if(list.size() > 2){
                        temp.add(list.get(0));
                        temp.add(list.get(1));
                    } else {
                        temp.addAll(list);
                    }
                    contactConfig.setContacts(temp);
                    SharedUtils.getInstance().setContactConfig(contactConfig);
                }
            }.start();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            boolean isGuided = SharedUtils.getInstance().getBoolean(SHARED_GUIDE_STATUS,false);

            if(isGuided){
                startActivity(MainActivity.class);
            } else {
                startActivity(GuideActivity.class);
            }
            finish();
        }
    };


}
