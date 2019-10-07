package com.kanhui.laowulao.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.kanhui.laowulao.R;

public class SizePopupWindow extends PopupWindow implements View.OnClickListener{

    private Context context;

    private int big,middle,small;

    private int originSize = 16;

    public SizePopupWindow(Context c,int oldSize, SizePopupWindow.SizeChagnedListener listener){
        this.context = c;
        this.listener = listener;
        this.originSize = oldSize;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_pop_seekbar,null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(view);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x66000000));

        setOutsideTouchable(true);
        setTouchable(true);
        setAnimationStyle(R.style.popwindow_anim_style);

        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_sure).setOnClickListener(this);
        SeekBar seekBar = view.findViewById(R.id.seekbar);
        seekBar.setProgress(originSize);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(listener != null){
                    listener.onChanged(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setValue(int big,int middle,int small){
        this.big = big;
        this.middle = middle;
        this.small = small;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                if(listener != null){
                    listener.onChanged(originSize);
                }
                dismiss();
                break;
            case R.id.btn_sure:
                dismiss();
                break;
        }
    }

    private SizeChagnedListener listener;

    public interface SizeChagnedListener{
        void onChanged(int size);
    }
}
