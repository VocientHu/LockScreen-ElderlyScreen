package com.kanhui.laowulao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kanhui.laowulao.about.AboutActivity;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.locker.model.Config;
import com.kanhui.laowulao.service.LockerService;
import com.kanhui.laowulao.utils.PermissionUtils;
import com.kanhui.laowulao.utils.ToastUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import static com.kanhui.laowulao.utils.PermissionUtils.dealwithPermiss;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final int PERMISSION_CODE_READ_CONTACT = 1;

    private Button btnStart,btnStop,btnSave,btnSend;
    private CheckBox cbBig,cbMiddle,cbSmall,cbList,cbGride;
    private TextView tvDes,tvFeatures,tvNotice;

    private String[] permissions = {Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE,Manifest.permission.READ_CALL_LOG};

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
        cbBig = findViewById(R.id.cb_big);
        cbMiddle = findViewById(R.id.cb_middle);
        cbSmall = findViewById(R.id.cb_small);
        cbList = findViewById(R.id.cb_list);
        cbGride = findViewById(R.id.cb_gride);
        tvDes = findViewById(R.id.tv_des);
        tvFeatures = findViewById(R.id.tv_features);
        tvNotice = findViewById(R.id.tv_notice);

        btnStart.setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.iv_call_phone).setOnClickListener(this);

    }

    void initData(){
        requsetPermission();
        config = Config.getConfig();
        cbBig.setChecked(false);
        cbMiddle.setChecked(false);
        cbSmall.setChecked(false);
        switch (config.getScaleSize()){
            case Config.SCALE_BIG:
                cbBig.setChecked(true);
                break;
            case Config.SCALE_MIDDLE:
                cbMiddle.setChecked(true);
                break;
            case Config.SCALE_SMALL:
                cbSmall.setChecked(true);
                break;

        }
        cbList.setChecked(false);
        cbGride.setChecked(false);
        switch (config.getListType()){
            case Config.TYPE_LIST:
                cbList.setChecked(true);
                break;
            case Config.TYPE_GRIDE:
                cbGride.setChecked(true);
                break;
        }
        initSizeCheckBox();
    }

    void initSizeCheckBox(){
        cbBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbMiddle.setChecked(false);
                cbSmall.setChecked(false);
                cbBig.setChecked(true);
                config.setScaleSize(Config.SCALE_BIG);
            }
        });
        cbMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbMiddle.setChecked(true);
                cbSmall.setChecked(false);
                cbBig.setChecked(false);
                config.setScaleSize(Config.SCALE_MIDDLE);
            }
        });
        cbSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbMiddle.setChecked(false);
                cbSmall.setChecked(true);
                cbBig.setChecked(false);
                config.setScaleSize(Config.SCALE_SMALL);
            }
        });
        cbGride.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                cbGride.setChecked(true);
                cbList.setChecked(false);
                config.setListType(Config.TYPE_GRIDE);
            }
        });
        cbList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                cbGride.setChecked(false);
                cbList.setChecked(true);
                config.setListType(Config.TYPE_LIST);
            }
        });

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
                default:
                    break;
        }
    }

    void toAbout(){
        startActivity(AboutActivity.class);
    }

    void saveConfig(){
        Config.setConfig(config);
        ToastUtils.showToast(MainActivity.this,"保存成功");
    }

    void sendConfig(){
        ToastUtils.showToast(MainActivity.this,"建设中...");
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
//        setStartBtnStatus(false);
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
