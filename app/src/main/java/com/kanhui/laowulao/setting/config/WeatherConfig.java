package com.kanhui.laowulao.setting.config;

import io.realm.RealmObject;

public class WeatherConfig{

    private int dateTimeSize;
    private int weekSize;
    private int citySize;
    private int weatherSize;
    private int weatherImgSize;

    public int getDateTimeSize() {
        return dateTimeSize;
    }

    public void setDateTimeSize(int dateTimeSize) {
        this.dateTimeSize = dateTimeSize;
    }

    public int getWeekSize() {
        return weekSize;
    }

    public void setWeekSize(int weekSize) {
        this.weekSize = weekSize;
    }

    public int getCitySize() {
        return citySize;
    }

    public void setCitySize(int citySize) {
        this.citySize = citySize;
    }

    public int getWeatherSize() {
        return weatherSize;
    }

    public void setWeatherSize(int weatherSize) {
        this.weatherSize = weatherSize;
    }

    public int getWeatherImgSize() {
        return weatherImgSize;
    }

    public void setWeatherImgSize(int weatherImgSize) {
        this.weatherImgSize = weatherImgSize;
    }
}
