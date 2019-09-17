package com.kanhui.laowulao.about;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;

import androidx.annotation.Nullable;

public class AboutActivity extends BaseActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_about);

    }
}
