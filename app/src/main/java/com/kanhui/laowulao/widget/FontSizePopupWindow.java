package com.kanhui.laowulao.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.kanhui.laowulao.R;

public class FontSizePopupWindow extends PopupWindow implements View.OnClickListener{

    private Context context;

    private int big,middle,small;

    public FontSizePopupWindow(Context c,FontSizeClickListener listener){
        this.context = c;
        this.listener = listener;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_pop_menu,null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(view);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x66000000));

        setOutsideTouchable(true);
        setTouchable(true);
        setAnimationStyle(R.style.popwindow_anim_style);

        view.findViewById(R.id.tv_big).setOnClickListener(this);
        view.findViewById(R.id.tv_middle).setOnClickListener(this);
        view.findViewById(R.id.tv_small).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    public void setValue(int big,int middle,int small){
        this.big = big;
        this.middle = middle;
        this.small = small;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_big:
                fontSelected(big);
                break;
            case R.id.tv_middle:
                fontSelected(middle);
                break;
            case R.id.tv_small:
                fontSelected(small);
                break;
            case R.id.tv_cancel:
                break;
                default:
                    break;
        }
        dismiss();
    }

    private void fontSelected(int size){
        if(listener != null){
            listener.onSizeChanged(size);
        }
    }

    private FontSizeClickListener listener;

    public FontSizeClickListener getListener() {
        return listener;
    }

    public void setListener(FontSizeClickListener listener) {
        this.listener = listener;
    }

    public interface FontSizeClickListener{
        void onSizeChanged(int size);
    }
}
