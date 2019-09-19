package com.kanhui.laowulao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.kanhui.laowulao.R;
import com.kanhui.laowulao.utils.Lunar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class WeatherView extends LinearLayout {


    private Context context;

    private TextView tvCity,tvTodayDate,tvToday,tvTodayWeather;

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
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        this.addView(view,params);

        tvCity = findViewById(R.id.tv_city);
        tvTodayDate = findViewById(R.id.tv_today_date);
        tvToday = findViewById(R.id.tv_today);
        tvTodayWeather = findViewById(R.id.tv_today_weather_detail);
        initLocation();
        initDate();
    }

    private void initDate() {
        tvToday.setText(getSysDate());
        Calendar today = Calendar.getInstance();
        tvTodayDate.setText("农历" + new Lunar(today).toString());
    }

    public void setTodayWeather(String weather){
        tvTodayWeather.setText(weather);
    }

    public void setCity(String city){
        tvCity.setText(city);
    }

    public static String getSysDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return sdf.format(date);
    }


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener(){

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(aMapLocation != null){
                String city = aMapLocation.getCity();
                setCity(city);
                initWeather(city);
            }
        }
    };

    private void initLocation(){
        setCity("初始化中...");
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        AMapLocationClientOption option = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        option.setOnceLocation(true);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

    }

    private void initWeather(String city){
        HeWeather.getWeatherForecast(context, city, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(Forecast forecast) {
                List<ForecastBase> listForecast = forecast.getDaily_forecast();
                if(listForecast != null){
                    ForecastBase today = listForecast.get(0);
                    setTodayWeather(today);
                }
            }
        });
    }

    private void setTodayWeather(ForecastBase today){

        setTodayWeather(today.getCond_txt_d());
    }

}
