package com.kanhui.laowulao.setting.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseFragment;
import com.kanhui.laowulao.setting.config.WeatherConfig;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.utils.ToastUtils;
import com.kanhui.laowulao.widget.SizePopupWindow;
import com.kanhui.laowulao.widget.WeatherView;

public class WeatherConfigFragment extends BaseFragment implements View.OnClickListener {

    private WeatherView weatherView;
    private View rootView;

    private WeatherConfig config;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_weather_config,null);
        initView();
        return rootView;
    }

    private void initView() {
        weatherView = rootView.findViewById(R.id.wv_setting);
        weatherView.findViewById(R.id.tv_today).setOnClickListener(this);
        weatherView.findViewById(R.id.tv_today_date).setOnClickListener(this);
        weatherView.findViewById(R.id.tv_today_weather_detail).setOnClickListener(this);
        weatherView.findViewById(R.id.tv_city).setOnClickListener(this);
        weatherView.findViewById(R.id.iv_weather).setOnClickListener(this);
        rootView.findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_today:// 日期时间
                setDateTime(view);
                break;
            case R.id.tv_today_date:// 农历时间
                setWeek(view);
                break;
            case R.id.tv_today_weather_detail://天气
                setWeatherSize(view);
                break;
            case R.id.tv_city:// 城市
                setCity(view);
                break;
            case R.id.iv_weather:// 天气图片
                setWeatherImgSize(view);
                break;
            case R.id.btn_save:
                SharedUtils.getInstance().setWeatherConfig(config);
                ToastUtils.showToast(context,"保存成功");
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
        }
    }

    void setDateTime(View view){
        SizePopupWindow window = new SizePopupWindow(context, config.getDateTimeSize(),
                new SizePopupWindow.SizeChagnedListener() {
                    @Override
                    public void onChanged(int size) {
                        config.setDateTimeSize(size);
                        weatherView.refreshSize(config);
                    }
                });
        window.showAsDropDown(view,0,0, Gravity.BOTTOM);
    }

    void setWeek(View view){
        SizePopupWindow window = new SizePopupWindow(context, config.getWeekSize(),
                new SizePopupWindow.SizeChagnedListener() {
                    @Override
                    public void onChanged(int size) {
                        config.setWeekSize(size);
                        weatherView.refreshSize(config);
                    }
                });
        window.showAsDropDown(view,0,0, Gravity.BOTTOM);
    }

    void setCity(View view){
        SizePopupWindow window = new SizePopupWindow(context, config.getCitySize(),
                new SizePopupWindow.SizeChagnedListener() {
                    @Override
                    public void onChanged(int size) {
                        config.setCitySize(size);
                        weatherView.refreshSize(config);
                    }
                });
        window.showAsDropDown(view,0,0, Gravity.BOTTOM);
    }

    void setWeatherSize(View view){
        SizePopupWindow window = new SizePopupWindow(context, config.getWeatherSize(),
                new SizePopupWindow.SizeChagnedListener() {
                    @Override
                    public void onChanged(int size) {
                        config.setWeatherSize(size);
                        weatherView.refreshSize(config);
                    }
                });
        window.showAsDropDown(view,0,0, Gravity.BOTTOM);
    }
    void setWeatherImgSize(View view){
        SizePopupWindow window = new SizePopupWindow(context, config.getWeatherImgSize(),
                new SizePopupWindow.SizeChagnedListener() {
                    @Override
                    public void onChanged(int size) {
                        config.setWeatherImgSize(size);
                        weatherView.refreshSize(config);
                    }
                });
        window.showAsDropDown(view,0,0, Gravity.BOTTOM);
    }





}
