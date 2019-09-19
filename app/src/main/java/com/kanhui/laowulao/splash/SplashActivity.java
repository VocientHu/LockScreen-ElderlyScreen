package com.kanhui.laowulao.splash;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kanhui.laowulao.MainActivity;
import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.guide.GuideActivity;
import com.kanhui.laowulao.utils.SharedUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity {

    public static final String SHARED_GUIDE_STATUS = "shared_guide_status";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        init();
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
