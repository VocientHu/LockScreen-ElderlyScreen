package com.kanhui.laowulao.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.about.AboutActivity;
import com.kanhui.laowulao.about.UseBookActivity;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.service.LockerService;
import com.kanhui.laowulao.utils.PermissionUtils;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.utils.ToastUtils;
import com.kanhui.laowulao.widget.EditPhonePopupWindow;
import com.kyleduo.switchbutton.SwitchButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import static com.kanhui.laowulao.utils.PermissionUtils.dealwithPermiss;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    public static final int PERMISSION_CODE_READ_CONTACT = 1;

    private static String[] permissions = {Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS};

    private SwitchButton sbAutoStart,sbRemoteStart;
    private TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

        initData();
    }

    void initView(){
        sbAutoStart = findViewById(R.id.sb_auto_start);
        sbRemoteStart = findViewById(R.id.sb_remote_start);
        tvPhone = findViewById(R.id.tv_bind_phone);
        findViewById(R.id.iv_call_phone).setOnClickListener(this);
        findViewById(R.id.tv_setting).setOnClickListener(this);
        findViewById(R.id.rl_bind_phone).setOnClickListener(this);
        findViewById(R.id.rl_use_book).setOnClickListener(this);
        findViewById(R.id.rl_version).setOnClickListener(this);
    }

    void initData(){
        requsetPermission();

        boolean isAutoStart = SharedUtils.getInstance().getAutoStart();
        boolean isRemoteStart = SharedUtils.getInstance().getRemoteStart();
        sbAutoStart.setChecked(isAutoStart);
        sbRemoteStart.setChecked(isRemoteStart);
        sbAutoStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedUtils.getInstance().setAutoStart(isChecked);
            }
        });
        sbRemoteStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedUtils.getInstance().setRemoteStart(isChecked);
            }
        });

        String bindPhone = SharedUtils.getInstance().getBindPhones();
        tvPhone.setText(bindPhone);

    }


    void requsetPermission(){
        ActivityCompat.requestPermissions(
                SettingActivity.this,permissions,PERMISSION_CODE_READ_CONTACT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:// 开启服务
                toStart();
                break;
            case R.id.iv_call_phone:// 关于
                toAbout();
                break;
            case R.id.tv_setting:// 返回
                finish();
                break;
            case R.id.rl_bind_phone:// 绑定手机
                bindPhone(view);
                break;
            case R.id.rl_use_book://使用说明
                startActivity(UseBookActivity.class);
                break;
            case R.id.rl_version:// 关于
                startActivity(AboutActivity.class);
                break;
                default:
                    break;
        }
    }

    void bindPhone(View v){
        String bindPhone = SharedUtils.getInstance().getBindPhones();
        EditPhonePopupWindow window = new EditPhonePopupWindow(SettingActivity.this, bindPhone,
                new EditPhonePopupWindow.PhoneChagnedListener() {
                    @Override
                    public void onChanged(String phone) {
                        tvPhone.setText(phone);
                        SharedUtils.getInstance().setBindPhones(phone);
                    }
                });
        window.showAsDropDown(v);
    }


    void toAbout(){
        startActivity(UseBookActivity.class);
    }

    private void toStop(){
        stopService(new Intent(SettingActivity.this,LockerService.class));
        ToastUtils.showToast(SettingActivity.this,"服务已停止");
    }

    private void toStart(){
        if(!PermissionUtils.hasPermission(SettingActivity.this,permissions)){
            dealwithPermiss(SettingActivity.this);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, LockerService.class));
        } else {
            startService(new Intent(this, LockerService.class));
        }
        ToastUtils.showToast(SettingActivity.this,"服务已开启");
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
                    dealwithPermiss(SettingActivity.this);
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
