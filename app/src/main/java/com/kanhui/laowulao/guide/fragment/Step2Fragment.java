package com.kanhui.laowulao.guide.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kanhui.laowulao.MainActivity;
import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseFragment;
import com.kanhui.laowulao.setting.config.PhoneModel;
import com.kanhui.laowulao.splash.SplashActivity;
import com.kanhui.laowulao.utils.SharedUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Step2Fragment extends BaseFragment implements View.OnClickListener{

    public static final int REQUEST_SELECT_PHONE_NUMBER = 1;// 选择联系人

    PhoneModel model;

    TextView tvPhone,tvName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_step2,null);
        }
        initView();
        return rootView;
    }

    private void initView() {
        rootView.findViewById(R.id.btn_next).setOnClickListener(this);
        rootView.findViewById(R.id.rl_bind_phone).setOnClickListener(this);
        tvPhone = rootView.findViewById(R.id.tv_bind_phone);
        tvName = rootView.findViewById(R.id.tv_bind_phone_name);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                toNext();
                break;
            case R.id.rl_bind_phone:
                selectContact();
                break;
        }
    }

    void selectContact(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
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
        Cursor cursor = context.getContentResolver().query(contactUri, projection,
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String name = cursor.getString(nameIndex);      //联系人姓名
            String number = cursor.getString(numberIndex);  //联系人号码
            model = new PhoneModel();
            model.setName(name);
            model.setPhone(number);
            setData();
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setData(){
        tvName.setText(model.getName());
        tvPhone.setText(model.getPhone());
    }

    private void toNext() {

        SharedUtils.getInstance().putBoolean(SplashActivity.SHARED_GUIDE_STATUS,true);
        if(model != null){
            SharedUtils.getInstance().setPhoneModel(model);
            startActivity(MainActivity.class);
            getActivity().finish();
        } else {
            startActivity(MainActivity.class);
        }

    }
}
