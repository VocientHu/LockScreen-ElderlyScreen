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
import com.kanhui.laowulao.about.AwiseActivity;
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
        findViewById(R.id.rl_author).setOnClickListener(this);
    }

    void initData(){
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




    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
            case R.id.rl_author:// 赞赏作者
                startActivity(AwiseActivity.class);
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


}
