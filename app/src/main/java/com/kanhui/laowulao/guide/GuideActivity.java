package com.kanhui.laowulao.guide;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.guide.fragment.Step1Fragment;

import androidx.annotation.Nullable;

/**
 * 引导
 */
public class GuideActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        turnToFragment(Step1Fragment.class,null,R.id.framelayout);
    }
}
