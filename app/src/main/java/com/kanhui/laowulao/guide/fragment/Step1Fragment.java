package com.kanhui.laowulao.guide.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.kanhui.laowulao.setting.SettingActivity;
import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseFragment;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.splash.SplashActivity;
import com.kanhui.laowulao.utils.SharedUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Step1Fragment extends BaseFragment implements View.OnClickListener {

    public static final int ANWSER_OLD_MAN = 1;
    public static final int ANWSER_YONG_MAN = 2;

    private CheckBox answer1,answer2;

    private int answer = ANWSER_OLD_MAN;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_step1,null);
        }

        initView();
        return rootView;
    }

    private void initView() {
        answer1 = rootView.findViewById(R.id.cb_answer1);
        answer2 = rootView.findViewById(R.id.cb_answer2);
        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        rootView.findViewById(R.id.btn_next).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cb_answer1:
                answer1.setChecked(true);
                answer2.setChecked(false);
                answer = ANWSER_OLD_MAN;
                break;
            case R.id.cb_answer2:
                answer = ANWSER_YONG_MAN;
                answer1.setChecked(false);
                answer2.setChecked(true);
                break;
            case R.id.btn_next:
                if(answer == ANWSER_OLD_MAN){
                    SharedUtils.getInstance().putInt(Config.SHARED_USER_TYPE,Config.USER_OLD);
                    turnToFragment(Step2Fragment.class,null,R.id.framelayout);
                } else {
                    SharedUtils.getInstance().putInt(Config.SHARED_USER_TYPE,Config.USER_YOUNG);
                    startActivity(SettingActivity.class);
                    SharedUtils.getInstance().putBoolean(SplashActivity.SHARED_GUIDE_STATUS,true);
                    getActivity().finish();
                }

                break;
        }
    }
}
