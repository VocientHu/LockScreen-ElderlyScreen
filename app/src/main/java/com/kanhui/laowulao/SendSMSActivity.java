package com.kanhui.laowulao;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kanhui.laowulao.base.BaseActivity;
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

    public static final int REQUEST_SELECT_PHONE_NUMBER = 1;// 选择联系人

    private static final String SHARED_SEND_SMS_PHONE = "shared_send_sms_phone";

    public static final int SEND_WEATHER = 1;
    public static final int SEND_APP = 2;
    public static final int SEND_CONTACT = 3;
    public static final int SEND_ALL = 4;
    public static final int SEND_CMD = 5;

    public static final String EXTRA_SMS_TYPE = "extra_sms_type";
    public static final String EXTRA_SMS_CMD = "extra_sms_cmd";

    private int type = 0;


    EditText etPhone;

    TextView tvDes,tvContact;

    String cmd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sendsms;
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.iv_contact).setOnClickListener(this);
        etPhone = findViewById(R.id.et_phone);
        tvContact = findViewById(R.id.tv_contact);
        tvDes = findViewById(R.id.tv_des);
    }

    private void initData(){
        String phone = SharedUtils.getInstance().getString(SHARED_SEND_SMS_PHONE,"");
        etPhone.setText(phone);

        type = getIntent().getIntExtra(EXTRA_SMS_TYPE,0);
        String text = "";
        switch (type){
            case SEND_ALL:
                text = "将修改目标手机锁屏页面的字体大小，联系人，应用";
                break;
            case SEND_APP:
                text = "将修改目标手机锁屏界面的常用APP大小及其大小";
                break;
            case SEND_CONTACT:
                text = "将修改目标手机锁屏界面的常用联系人及其大小";
                break;
            case SEND_WEATHER:
                text = "将修改目标手机锁屏界面的日期，天气等字体大小";
                break;
            case SEND_CMD:
                text = "将远程启动目标手机的锁屏服务";
                cmd = getIntent().getStringExtra(EXTRA_SMS_CMD);
                break;
        }

        tvDes.setText(text);
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
            case R.id.iv_contact:
                selectContact();
                break;
        }
    }

    void selectContact(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(SendSMSActivity.this.getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            return;
        }
        Uri contactUri = data.getData();
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(contactUri, projection,
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String name = cursor.getString(nameIndex);      //联系人姓名
            String number = cursor.getString(numberIndex);  //联系人号码
            etPhone.setText(number);
            tvContact.setText(name);
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
            case SEND_CMD:
                manager.sendTextMessage(phone,null,getCMDStr(),null,null);
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

    private String getCMDStr(){
        StringBuffer smsBuffer = new StringBuffer();
        smsBuffer.append(LockerService.SMS_FLAG);
        smsBuffer.append(SmsReceiver.SMS_TYPE_CMD);
        smsBuffer.append(cmd);
        return smsBuffer.toString();
    }


}
