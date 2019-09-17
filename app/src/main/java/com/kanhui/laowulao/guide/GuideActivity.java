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
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        turnToFragment(Step1Fragment.class,null,R.id.framelayout);
    }
}
