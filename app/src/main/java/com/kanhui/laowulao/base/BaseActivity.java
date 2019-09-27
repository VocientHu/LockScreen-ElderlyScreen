package com.kanhui.laowulao.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.locker.LockerActivity;
import com.kanhui.laowulao.utils.LogUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 锁屏界面才全屏，其他页面不全屏
        if(this instanceof LockerActivity){
            // 处理状态栏
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
        }
        initFragment();
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
    }

    public void turnToFragment(Class<? extends BaseFragment> fragment,Bundle bundle,int layoutId){
        String tag = fragment.getSimpleName();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentByTag(tag);
        if(oldFragment != null){
            if(bundle != null){
                oldFragment.setArguments(bundle);
            }
            if(oldFragment.isAdded()){
                transaction.show(oldFragment);
            }
        } else {
            try {
                Fragment newFragment = fragment.newInstance();
                if(bundle != null){
                    newFragment.setArguments(bundle);
                }
                transaction.replace(layoutId,newFragment,tag);
                transaction.addToBackStack(tag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        transaction.commitAllowingStateLoss();

    }


    public void startActivity(Class targetClass){
        startActivity(new Intent(this,targetClass));
    }
}
