package com.kanhui.laowulao.setting;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.SendSMSActivity;
import com.kanhui.laowulao.about.AboutActivity;
import com.kanhui.laowulao.about.UseBookActivity;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.config.CMDModel;
import com.kanhui.laowulao.setting.config.PhoneModel;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kyleduo.switchbutton.SwitchButton;


public class SettingActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_SELECT_PHONE_NUMBER = 1;// 选择联系人



    private SwitchButton sbAutoStart,sbRemoteStart;
    private TextView tvPhone,tvName;

    private PhoneModel phoneModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        initData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    void initView(){
        sbAutoStart = findViewById(R.id.sb_auto_start);
        sbRemoteStart = findViewById(R.id.sb_remote_start);
        tvPhone = findViewById(R.id.tv_bind_phone);
        tvName = findViewById(R.id.tv_bind_phone_name);
        findViewById(R.id.iv_call_phone).setOnClickListener(this);
        findViewById(R.id.tv_setting).setOnClickListener(this);
        findViewById(R.id.rl_bind_phone).setOnClickListener(this);
        findViewById(R.id.rl_use_book).setOnClickListener(this);
        findViewById(R.id.rl_version).setOnClickListener(this);
        findViewById(R.id.rl_author).setOnClickListener(this);
        findViewById(R.id.rl_remote_contral).setOnClickListener(this);
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

        phoneModel = SharedUtils.getInstance().getPhoneModel();
        if(phoneModel != null){
            setBindPhone();
        }
    }


    void setBindPhone(){
        tvPhone.setText(phoneModel.getPhone());
        tvName.setText(phoneModel.getName());
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_setting:// 返回
                finish();
                break;
            case R.id.rl_bind_phone:// 绑定手机
                bindPhone();
                break;
            case R.id.rl_use_book://使用说明
                startActivity(UseBookActivity.class);
                break;
            case R.id.rl_version:// 关于
                startActivity(AboutActivity.class);
                break;
            case R.id.rl_remote_contral:// 远程启动
                Intent intent = new Intent(SettingActivity.this, SendSMSActivity.class);
                intent.putExtra(SendSMSActivity.EXTRA_SMS_TYPE,SendSMSActivity.SEND_CMD);
                intent.putExtra(SendSMSActivity.EXTRA_SMS_CMD, CMDModel.CMD_START_SERVICE);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    void bindPhone(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(SettingActivity.this.getPackageManager()) != null) {
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
            number = number.replace("+86","");
            number = number.replace(" ","");
            phoneModel = new PhoneModel();
            phoneModel.setName(name);
            phoneModel.setPhone(number);
            setBindPhone();
            SharedUtils.getInstance().setPhoneModel(phoneModel);
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
