package com.kanhui.laowulao;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.receiver.SmsReceiver;
import com.kanhui.laowulao.service.LockerService;
import com.kanhui.laowulao.setting.config.AppConfig;
import com.kanhui.laowulao.setting.config.ContactConfig;
import com.kanhui.laowulao.setting.config.WeatherConfig;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.utils.StringUtils;
import com.kanhui.laowulao.utils.ToastUtils;

import androidx.annotation.Nullable;

public class SendSMSActivity extends BaseActivity implements View.OnClickListener{

    private static final String SHARED_SEND_SMS_PHONE = "shared_send_sms_phone";

    public static final int SEND_WEATHER = 1;
    public static final int SEND_APP = 2;
    public static final int SEND_CONTACT = 3;
    public static final int SEND_ALL = 4;

    public static final String EXTRA_SMS_TYPE = "extra_sms_type";

    private int type = 0;


    EditText etPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendsms);

        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        etPhone = findViewById(R.id.et_phone);

    }

    private void initData(){
        String phone = SharedUtils.getInstance().getString(SHARED_SEND_SMS_PHONE,"");
        etPhone.setText(phone);

        type = getIntent().getIntExtra(EXTRA_SMS_TYPE,0);

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
        SharedUtils.getInstance().putString(SHARED_SEND_SMS_PHONE,phone);
        SmsManager manager = SmsManager.getDefault();


        switch (type){
            case SEND_WEATHER:
                manager.sendTextMessage(phone,null,getWeatherStr(),null,null);
                break;
            case SEND_APP:
                manager.sendTextMessage(phone,null,getAppStr(),null,null);
                break;
            case SEND_CONTACT:
                manager.sendTextMessage(phone,null,getContactStr(),null,null);
                break;
            case SEND_ALL:
                manager.sendTextMessage(phone,null,getWeatherStr(),null,null);
                manager.sendTextMessage(phone,null,getAppStr(),null,null);
                manager.sendTextMessage(phone,null,getContactStr(),null,null);
                break;
        }
        ToastUtils.showToast(this,"发送成功");
        finish();
    }

    private String getWeatherStr(){
        StringBuffer smsBuffer = new StringBuffer();
        smsBuffer.append(LockerService.SMS_FLAG);
        smsBuffer.append(SmsReceiver.SMS_TYPE_WEATHER);
        WeatherConfig weatherConfig = SharedUtils.getInstance().getWeatherConfig();
        smsBuffer.append(new Gson().toJson(weatherConfig));
        return smsBuffer.toString();
    }
    private String getAppStr(){
        StringBuffer smsBuffer = new StringBuffer();
        smsBuffer.append(LockerService.SMS_FLAG);
        smsBuffer.append(SmsReceiver.SMS_TYPE_APP);
         AppConfig appConfig = SharedUtils.getInstance().getAppConfig();
         smsBuffer.append(new Gson().toJson(appConfig));
        return smsBuffer.toString();
    }
    private String getContactStr(){
        StringBuffer smsBuffer = new StringBuffer();
        smsBuffer.append(LockerService.SMS_FLAG);
        smsBuffer.append(SmsReceiver.SMS_TYPE_CONTACT);
         ContactConfig contactConfig = SharedUtils.getInstance().getContactConfig();
         smsBuffer.append(new Gson().toJson(contactConfig));
        return smsBuffer.toString();
    }


}
