package com.kanhui.laowulao.about;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;

import androidx.annotation.Nullable;

public class AboutActivity extends BaseActivity implements View.OnClickListener{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.iv_back).setOnClickListener(this);

        TextView tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText("当前版本：" + getAppVersionName(this));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_about;
    }

    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return appVersionName;
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
