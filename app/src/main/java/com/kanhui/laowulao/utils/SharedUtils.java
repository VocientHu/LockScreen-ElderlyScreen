package com.kanhui.laowulao.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kanhui.laowulao.base.LWLApplicatoin;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.setting.config.AppConfig;
import com.kanhui.laowulao.setting.config.ContactConfig;
import com.kanhui.laowulao.setting.config.PhoneModel;
import com.kanhui.laowulao.setting.config.WeatherConfig;

public class SharedUtils {

    private static SharedUtils Instance;

    private Context context;

    private SharedPreferences sharedPreferences;

    private SharedUtils(Context context){
        this.context = context;
        init();
    }

    void init(){
        sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }

    public static SharedUtils getInstance(){
        if(Instance == null){
            Instance = new SharedUtils(LWLApplicatoin.getInstance());
        }
        return Instance;
    }

    public void putInt(String name,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name,value);
        editor.commit();
    }

    public void putString(String name,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name,value);
        editor.commit();
    }

    public void putBoolean(String name,boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name,value);
        editor.commit();
    }


    public int getInt(String name,int defaultValue){
        return sharedPreferences.getInt(name,defaultValue);
    }

    public boolean getBoolean(String name,boolean defaultValue){
        return sharedPreferences.getBoolean(name,defaultValue);
    }


    public String getString(String name,String defaultValue){
        return sharedPreferences.getString(name,defaultValue);
    }

    private static final String LOCK_LIST_TYPE = "lock_list_type";
    private static final String LOCK_SCALE_SIZE = "lock_scale_size";
    private static final String LOCK_SHARE_URL = "lock_share_url";
    public Config getConfig(){
        Config config = new Config();
        config.setListType(getInt(LOCK_LIST_TYPE, Config.TYPE_LIST));
        config.setScaleSize(getInt(LOCK_SCALE_SIZE,Config.SCALE_MIDDLE));
        config.setBindPhones(getString(Config.SHARED_BIND_PHONES,""));
        config.setShareUrl(getString(LOCK_SHARE_URL,""));

        return config;
    }

    public void saveConfig(Config config){
        putInt(LOCK_LIST_TYPE,config.getListType());
        putInt(LOCK_SCALE_SIZE,config.getScaleSize());
        putString(Config.SHARED_BIND_PHONES,config.getBindPhones());
        putString(LOCK_SHARE_URL,config.getShareUrl());
    }

    public void setBindPhones(String phone){
        putString(Config.SHARED_BIND_PHONES,phone);
    }

    public String getBindPhones(){
        return getString(Config.SHARED_BIND_PHONES,"");
    }

    private static final String WEATHER_CONFIG = "weather_config";
    public WeatherConfig getWeatherConfig(){
        String json = getString(WEATHER_CONFIG,null);
        if(StringUtils.isEmpty(json)){
            return null;
        }
        WeatherConfig config = new Gson().fromJson(json,WeatherConfig.class);
        return config;
    }

    public void setWeatherConfig(WeatherConfig config){
        String json = config == null ? "" : new Gson().toJson(config);
        putString(WEATHER_CONFIG,json);
    }

    private static final String APPS_CONFIG = "apps_config";
    public AppConfig getAppConfig(){
        String json = getString(APPS_CONFIG,null);
        if(StringUtils.isEmpty(json)){
            AppConfig config = new AppConfig();
            config.setIconSize(Config.APP_IMG_MIDDLE);
            config.setNameSize(Config.APP_NAME_SMALL);
            return config;
        }
        AppConfig config = new Gson().fromJson(json,AppConfig.class);
        return config;
    }

    public void setAppConfig(AppConfig config){
        String json = config == null ? "" : new Gson().toJson(config);
        putString(APPS_CONFIG,json);
    }

    private static final String CONTACT_CONFIG = "contact_config";
    public ContactConfig getContactConfig(){
        String json = getString(CONTACT_CONFIG,null);
        if(StringUtils.isEmpty(json)){
            ContactConfig config = new ContactConfig();
            config.setNameSize(Config.SCALE_SMALL);
            return config;
        }
        ContactConfig config = new Gson().fromJson(json,ContactConfig.class);
        return config;
    }

    public PhoneModel getPhoneModel(){
        PhoneModel model = null;
        String json = getString(Config.SHARED_BIND_PHONES,null);
        if(!StringUtils.isEmpty(json)){
           model = new Gson().fromJson(json,PhoneModel.class);
        }
        return model;
    }

    public void setPhoneModel(PhoneModel model){
        String json = new Gson().toJson(model);
        putString(Config.SHARED_BIND_PHONES,json);
    }

    public void setContactConfig(ContactConfig config){
        String json = config == null ? "" : new Gson().toJson(config);
        putString(CONTACT_CONFIG,json);
    }

    private static final String AUTO_START = "app_auto_start";
    public boolean getAutoStart(){
        return getBoolean(AUTO_START,true);
    }
    public void setAutoStart(boolean status){
        putBoolean(AUTO_START,status);
    }
    private static final String REMOTE_START = "app_remote_start";
    public boolean getRemoteStart(){
        return getBoolean(REMOTE_START,true);
    }
    public void setRemoteStart(boolean status){
        putBoolean(REMOTE_START,status);
    }


}
