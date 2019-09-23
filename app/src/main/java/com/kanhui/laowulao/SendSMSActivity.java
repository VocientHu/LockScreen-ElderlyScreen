package com.kanhui.laowulao;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.service.LockerService;
import com.kanhui.laowulao.utils.StringUtils;
import com.kanhui.laowulao.utils.ToastUtils;

import androidx.annotation.Nullable;

public class SendSMSActivity extends BaseActivity implements View.OnClickListener{


    EditText etPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendsms);

        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        etPhone = findViewById(R.id.et_phone);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_send:
                sendSMS();
                break;
        }
    }

    private void sendSMS() {
        String phone = etPhone.getText().toString();
        if(StringUtils.isEmpty(phone)){
            ToastUtils.showToast(this,"请填写手机号");
            return;
        }
        if(!StringUtils.isPhoneNumber(phone)){
            ToastUtils.showToast(this,"请填写正确的手机号");
            return;
        }

        String jsonString = new Gson().toJson(Config.getConfig());
        String sms = LockerService.SMS_FLAG + jsonString;
        SmsManager manager = SmsManager.getDefault();

        manager.sendTextMessage(phone,null,sms,null,null);
        ToastUtils.showToast(this,"发送成功");
        etPhone.setText("");
    }
}
