package com.kanhui.laowulao.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.kanhui.laowulao.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        // 处理状态栏
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_green));
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(option);

        initFragment();
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
    }

    public void turnToFragment(Class<? extends BaseFragment> fragment,Bundle bundle,int layoutId){
        String tag = fragment.getName();
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
