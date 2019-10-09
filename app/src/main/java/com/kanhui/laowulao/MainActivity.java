package com.kanhui.laowulao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.service.LockerService;
import com.kanhui.laowulao.setting.InspectorActivity;
import com.kanhui.laowulao.setting.SettingActivity;
import com.kanhui.laowulao.utils.PermissionUtils;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.utils.ToastUtils;
import com.kanhui.laowulao.widget.IconView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import static com.kanhui.laowulao.utils.PermissionUtils.dealwithPermiss;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String SHARED_IS_FIRST_OPEN_MAIN = "shared_is_first_open_main";

    public static final int PERMISSION_CODE_READ_CONTACT = 1;

    private static String[] permissions = {Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS};

    IconView ivLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.iv_inspector).setOnClickListener(this);
        findViewById(R.id.iv_setting).setOnClickListener(this);
        findViewById(R.id.tv_start).setOnClickListener(this);
        findViewById(R.id.tv_stop).setOnClickListener(this);

        boolean isFirstOpen = SharedUtils.getInstance().getBoolean(SHARED_IS_FIRST_OPEN_MAIN,true);
        if(isFirstOpen){
            findViewById(R.id.tv_des).setVisibility(View.VISIBLE);
            SharedUtils.getInstance().putBoolean(SHARED_IS_FIRST_OPEN_MAIN,false);
        } else {
            findViewById(R.id.tv_des).setVisibility(View.GONE);
        }

        ivLight = findViewById(R.id.iv_light);
        if(LockerService.IsServiceStarted){
            onLight();
        } else {
            offLight();
        }
        requsetPermission();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_inspector:// 设计器
                startActivity(InspectorActivity.class);
                break;
            case R.id.iv_setting:// 系统配置
                startActivity(SettingActivity.class);
                break;
            case R.id.tv_start:
                toStart();
                break;
            case R.id.tv_stop:
                toStop();
                break;
        }
    }

    private void toStart(){
        if(!PermissionUtils.hasPermission(MainActivity.this,permissions)){
            dealwithPermiss(MainActivity.this);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, LockerService.class));
        } else {
            startService(new Intent(this, LockerService.class));
        }
        onLight();
        ToastUtils.showToast(MainActivity.this,"服务已开启");
    }

    private void onLight(){
        ivLight.setTextColor(getResources().getColor(R.color.main_green));
    }

    private void offLight(){
        ivLight.setTextColor(getResources().getColor(R.color.black_9));
    }

    void requsetPermission(){
        ActivityCompat.requestPermissions(
                MainActivity.this,permissions,PERMISSION_CODE_READ_CONTACT);
    }

    private void toStop(){
        offLight();
        stopService(new Intent(MainActivity.this,LockerService.class));
        ToastUtils.showToast(MainActivity.this,"服务已停止");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_CODE_READ_CONTACT:
                boolean hasAllGranted = true;
                //判断是否拒绝  拒绝后要怎么处理 以及取消再次提示的处理
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        hasAllGranted = false;
                        break;
                    }
                }
                if (!hasAllGranted) { //同意权限做的处理,开启服务提交通讯录
                    dealwithPermiss(MainActivity.this);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
