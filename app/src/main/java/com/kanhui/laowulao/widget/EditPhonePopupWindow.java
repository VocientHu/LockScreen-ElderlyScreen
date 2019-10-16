package com.kanhui.laowulao.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.utils.StringUtils;
import com.kanhui.laowulao.utils.ToastUtils;

public class EditPhonePopupWindow extends PopupWindow implements View.OnClickListener{

    private Context context;

    private String phone = "";
    private EditText etPhone;

    public EditPhonePopupWindow(Context c, String oldPhone, EditPhonePopupWindow.PhoneChagnedListener listener){
        this.context = c;
        this.listener = listener;
        this.phone = oldPhone;
        init();
    }

    private void init() {

        View view = LayoutInflater.from(context).inflate(R.layout.widget_pop_bind_phone,null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x66000000));

        setOutsideTouchable(true);
        setTouchable(true);
        setAnimationStyle(R.style.popwindow_anim_style);

        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_sure).setOnClickListener(this);
        etPhone = view.findViewById(R.id.et_bind_phone);
        etPhone.setText(phone);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_sure:
                phone = etPhone.getText().toString();
                if(!StringUtils.isPhoneNumber(phone)){
                    ToastUtils.showToast(context,"请填写正确的手机号");
                    return;
                }
                if(listener != null){
                    listener.onChanged(phone);
                }
                dismiss();
                break;
        }
    }

    private PhoneChagnedListener listener;

    public interface PhoneChagnedListener{
        void onChanged(String phone);
    }
}
