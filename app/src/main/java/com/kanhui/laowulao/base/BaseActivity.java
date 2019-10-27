package com.kanhui.laowulao.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.githang.statusbar.StatusBarCompat;
import com.kanhui.laowulao.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayout());
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white), true);
        initFragment();
    }

    protected abstract int getLayout();

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
