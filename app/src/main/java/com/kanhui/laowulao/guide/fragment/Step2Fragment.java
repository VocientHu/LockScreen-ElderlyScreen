package com.kanhui.laowulao.guide.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kanhui.laowulao.MainActivity;
import com.kanhui.laowulao.setting.SettingActivity;
import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseFragment;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.splash.SplashActivity;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.utils.StringUtils;
import com.kanhui.laowulao.utils.ToastUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Step2Fragment extends BaseFragment implements View.OnClickListener{

    EditText etPhone;

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
        etPhone = rootView.findViewById(R.id.et_phone);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                toNext();
                break;
        }
    }

    private void toNext() {
        String phones = etPhone.getText().toString();
        SharedUtils.getInstance().putBoolean(SplashActivity.SHARED_GUIDE_STATUS,true);
        if(!StringUtils.isEmpty(phones)){
            String[] arr = phones.split(";");
            for(String phone : arr){
                if(!StringUtils.isPhoneNumber(phone)){
                    ToastUtils.showToast(context,"请填写正确的手机号");
                    return;
                }
            }
            SharedUtils.getInstance().putString(Config.SHARED_BIND_PHONES,phones);
            startActivity(MainActivity.class);
            getActivity().finish();
        } else {
            startActivity(MainActivity.class);
        }

    }
}
