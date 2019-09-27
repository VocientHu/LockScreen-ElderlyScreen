package com.kanhui.laowulao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kanhui.laowulao.about.AboutActivity;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.service.LockerService;
import com.kanhui.laowulao.setting.SettingActivity;
import com.kanhui.laowulao.utils.PermissionUtils;
import com.kanhui.laowulao.utils.ToastUtils;
import com.kanhui.laowulao.widget.FontSizePopupWindow;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import static com.kanhui.laowulao.utils.PermissionUtils.dealwithPermiss;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final int PERMISSION_CODE_READ_CONTACT = 1;

    private Button btnStart;
    private EditText etPhone,etShare;

    private static String[] permissions = {Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS};

    private TextView tvContactSize,tvAppImgSize,tvAppNameSize;

    // 配置
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initData();
    }

    void initView(){
        btnStart = findViewById(R.id.btn_start);
        etPhone = findViewById(R.id.et_phone);
        etShare = findViewById(R.id.et_share);
        btnStart.setOnClickListener(this);
        tvContactSize = findViewById(R.id.tv_contact_size);
        tvAppImgSize = findViewById(R.id.tv_app_img_size);
        tvAppNameSize = findViewById(R.id.tv_app_name_size);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.iv_call_phone).setOnClickListener(this);
        findViewById(R.id.tv_setting).setOnClickListener(this);
        findViewById(R.id.rl_app_img_size).setOnClickListener(this);
        findViewById(R.id.rl_app_name_size).setOnClickListener(this);
        findViewById(R.id.rl_contact_size).setOnClickListener(this);
    }

    void initData(){
        requsetPermission();
        config = Config.getConfig();
        etPhone.setText(config.getBindPhones());
        etShare.setText(config.getShareUrl());
        tvContactSize.setText(Config.getContactNameSize(config.getScaleSize()));
        tvAppImgSize.setText(Config.getContactNameSize(config.getAppImgSize()));
        tvAppNameSize.setText(Config.getContactNameSize(config.getAppNameSize()));
    }


    void requsetPermission(){
        ActivityCompat.requestPermissions(
                MainActivity.this,permissions,PERMISSION_CODE_READ_CONTACT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:// 开启服务
                toStart();
                break;
            case R.id.btn_stop:// 停止服务
                toStop();
                break;
            case R.id.btn_save:// 保存配置
                saveConfig();
                break;
            case R.id.btn_send:// 发送配置给指定设备
                sendConfig();
                break;
            case R.id.iv_call_phone:// 关于
                toAbout();
                break;
            case R.id.tv_setting:// 设置
                toSetting();
                break;
            case R.id.rl_app_img_size:// app图标大小
                appIconSizeChanged(view);
                break;
            case R.id.rl_app_name_size:// app名称大小
                appNameSizeChanged(view);
                break;
            case R.id.rl_contact_size:// 联系人名称大小
                contactNameSizeChanged(view);
                break;
                default:
                    break;
        }
    }

    private void contactNameSizeChanged(View view) {
        FontSizePopupWindow popup = new FontSizePopupWindow(MainActivity.this,
                new FontSizePopupWindow.FontSizeClickListener() {
                    @Override
                    public void onSizeChanged(int size) {
                        config.setScaleSize(size);
                        tvContactSize.setText(Config.getContactNameSize(size));
                    }
                });
        popup.setValue(Config.SCALE_BIG,Config.SCALE_MIDDLE,Config.SCALE_SMALL);
        popup.showAtLocation(view, Gravity.BOTTOM,0,0);

    }

    private void appNameSizeChanged(View view) {
        FontSizePopupWindow popup = new FontSizePopupWindow(MainActivity.this,
                new FontSizePopupWindow.FontSizeClickListener() {
                    @Override
                    public void onSizeChanged(int size) {
                        config.setScaleSize(size);
                        tvAppNameSize.setText(Config.getAPPNameSize(size));
                    }
                });
        popup.setValue(Config.APP_NAME_BIG,Config.APP_NAME_MIDDLE,Config.APP_NAME_SMALL);
        popup.showAtLocation(view, Gravity.BOTTOM,0,0);

    }

    private void appIconSizeChanged(View view) {
        FontSizePopupWindow popup = new FontSizePopupWindow(MainActivity.this,
                new FontSizePopupWindow.FontSizeClickListener() {
                    @Override
                    public void onSizeChanged(int size) {
                        config.setScaleSize(size);
                        tvAppImgSize.setText(Config.getAPPImgSize(size));
                    }
                });
        popup.setValue(Config.APP_IMG_BIG,Config.APP_IMG_MIDDLE,Config.APP_IMG_SMALL);
        popup.showAtLocation(view, Gravity.BOTTOM,0,0);
    }


    private void toSetting() {
        startActivity(SettingActivity.class);
    }

    void toAbout(){
        startActivity(AboutActivity.class);
    }

    void commitConfig(){
        String phone = etPhone.getText().toString();
        config.setBindPhones(phone);
        String address = etShare.getText().toString();
        config.setShareUrl(address);
    }

    void saveConfig(){
        commitConfig();
        Config.setConfig(config);
        ToastUtils.showToast(MainActivity.this,"保存成功");
    }

    void sendConfig(){
        commitConfig();
        Config.setConfig(config);
        startActivity(SendSMSActivity.class);

    }

    private void toStop(){
        stopService(new Intent(MainActivity.this,LockerService.class));
        ToastUtils.showToast(MainActivity.this,"服务已停止");
    }

    private void toStart(){
        if(!PermissionUtils.hasPermission(MainActivity.this,permissions)){
            dealwithPermiss(MainActivity.this);
            return;
        }
//        startActivity(LockerActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, LockerService.class));
        } else {
            startService(new Intent(this, LockerService.class));
        }
        ToastUtils.showToast(MainActivity.this,"服务已开启");
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
