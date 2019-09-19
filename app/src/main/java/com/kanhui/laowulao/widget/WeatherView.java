package com.kanhui.laowulao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kanhui.laowulao.R;

import androidx.annotation.Nullable;

public class WeatherView extends LinearLayout {

    private Context context;

    private TextView tvCity,tvTodayDate,tvTomorrowDate,tvTodayWeather,tvTomorrowWeather;

    public WeatherView(Context context) {
        this(context,null);
    }

    public WeatherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public WeatherView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initView();
    }


    void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.widget_weather,null);
        this.addView(view);

        tvCity = findViewById(R.id.tv_city);
        tvTodayDate = findViewById(R.id.tv_today_date);
        tvTomorrowDate = findViewById(R.id.tv_tomorrow_date);
        tvTodayWeather = findViewById(R.id.tv_today_weather_detail);
        tvTomorrowWeather = findViewById(R.id.tv_tomorrow_weather_detail);

    }

    public void setTodayWeather(String date,String weather){
        tvTodayDate.setText(date);
        tvTodayWeather.setText(weather);
    }

    public void setTomorrowWeather(String date,String weather){
        tvTomorrowDate.setText(date);
        tvTomorrowWeather.setText(weather);
    }

    public void setCity(String city){
        tvCity.setText(city);
    }

}
