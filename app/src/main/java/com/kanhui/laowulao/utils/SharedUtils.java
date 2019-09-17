package com.kanhui.laowulao.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kanhui.laowulao.base.LWLApplicatoin;
import com.kanhui.laowulao.locker.model.Config;

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
    private static final String LOCK_SCALE_SIZE = "lock_SCALE_SIZE";
    public Config getConfig(){
        Config config = new Config();
        config.setListType(getInt(LOCK_LIST_TYPE, Config.TYPE_LIST));
        config.setScaleSize(getInt(LOCK_SCALE_SIZE,Config.SCALE_MIDDLE));
        config.setBindPhones(getString(Config.SHARED_BIND_PHONES,""));
        return config;
    }

    public void saveConfig(Config config){
        putInt(LOCK_LIST_TYPE,config.getListType());
        putInt(LOCK_SCALE_SIZE,config.getScaleSize());
        putString(Config.SHARED_BIND_PHONES,config.getBindPhones());

    }




}
