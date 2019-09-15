package com.kanhui.laowulao.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class IconView extends AppCompatTextView {
    public IconView(Context context) {
        this(context,null);
    }

    public IconView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context){
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"icons/iconfont.ttf"));
    }
}
