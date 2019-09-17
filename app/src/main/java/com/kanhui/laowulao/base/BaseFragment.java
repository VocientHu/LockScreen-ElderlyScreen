package com.kanhui.laowulao.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected View rootView;

    protected Context context;

    protected BaseActivity activity;

    public static BaseFragment getInstance(){
        return new BaseFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (BaseActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        this.activity = (BaseActivity) getActivity();
    }

    public void turnToFragment(Class<? extends BaseFragment> fragment,Bundle bundle,int layoutId){
        activity.turnToFragment(fragment,bundle,layoutId);
    }


    public void startActivity(Class activity){
        context.startActivity(new Intent(context,activity));
    }

}
