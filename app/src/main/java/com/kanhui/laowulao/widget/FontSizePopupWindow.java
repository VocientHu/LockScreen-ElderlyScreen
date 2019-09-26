package com.kanhui.laowulao.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.config.Config;

public class FontSizePopupWindow extends PopupWindow implements View.OnClickListener{

    private Context context;

    public FontSizePopupWindow(Context c){
        this.context = c;

        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_pop_menu,null);
        setContentView(view);
        setFocusable(true);
//        setBackgroundDrawable(new BitmapDrawable());

        setOutsideTouchable(true);
        setTouchable(true);
        setAnimationStyle(R.style.popwindow_anim_style);

        view.findViewById(R.id.tv_big).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_big:
                fontSelected(Config.SCALE_BIG);
                break;
            case R.id.tv_middle:
                fontSelected(Config.SCALE_MIDDLE);
                break;
            case R.id.tv_small:
                fontSelected(Config.SCALE_SMALL);
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
