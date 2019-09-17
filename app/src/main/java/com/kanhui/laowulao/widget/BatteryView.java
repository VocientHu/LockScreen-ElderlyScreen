package com.kanhui.laowulao.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.kanhui.laowulao.R;

import androidx.annotation.Nullable;

/**
 * 电池电量显示View
 */
public class BatteryView extends View {

    //电量
    private int percent = 0;

    private int width,height;//控件高宽

    private int color;// 颜色;

    private Context context;

    public BatteryView(Context context) {
        this(context,null);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;

        color = context.getResources().getColor(R.color.main_green);

        resetSize();
    }

    private void resetSize(){
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        resetSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBattery(canvas);
    }

    void drawBattery(Canvas canvas){
        Paint paint = new Paint();
        // 空心
        paint.setStyle(Paint.Style.STROKE);

        float sWidth = width/20.f;//左右边框
        float sWidth2 = sWidth/2;//上下边框

        paint.setStrokeWidth(sWidth);

        // 绘制矩形框
        RectF rect = new RectF(sWidth2,sWidth2,width-sWidth-sWidth2,height-sWidth2);
        paint.setColor(color);
        canvas.drawRect(rect,paint);

        // 绘制电量
        float offset = (width-sWidth*2)*percent/100.f;
        RectF rectPower = new RectF(sWidth,sWidth,offset,height-sWidth);
        if(percent <= 20){
            paint.setColor(Color.RED);
        } else if(percent > 20 && percent <=50){
            paint.setColor(Color.YELLOW);
        } else {
            paint.setColor(color);
        }
        paint.setStrokeWidth(0);
        //实心
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectPower,paint);
        // 电池头部

        RectF rectHead = new RectF(width-sWidth,height*0.25f,width,height*0.75f);
        paint.setColor(color);
        canvas.drawRect(rectHead,paint);
    }

    public void setPercent(int progress){
        this.percent = progress < 0 || progress > 100 ? 100 : progress;
        invalidate();
    }
}
