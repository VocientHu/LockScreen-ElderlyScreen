package com.kanhui.laowulao.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.kanhui.laowulao.R;

public class UseWarningDialog extends Dialog implements View.OnClickListener{

    public UseWarningDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_use_warning);

        findViewById(R.id.btn_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close:
                dismiss();
                break;
        }
    }
}
