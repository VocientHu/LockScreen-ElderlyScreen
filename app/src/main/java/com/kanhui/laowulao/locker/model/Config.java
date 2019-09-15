package com.kanhui.laowulao.locker.model;

import com.kanhui.laowulao.locker.adapter.ContactAdapter;
import com.kanhui.laowulao.utils.SharedUtils;

public class Config {

    public static final int SCALE_BIG = 32;
    public static final int SCALE_MIDDLE = 28;
    public static final int SCALE_SMALL = 24;

    private static Config config;

    public static Config getConfig(){
        if(config == null){
            config = SharedUtils.getInstance().getConfig();
        }
        return config;
    }

    public static void setConfig(Config config){
        Config.config = config;
        SharedUtils.getInstance().saveConfig(config);
    }

    public Config(){

    }



    private int listType = ContactAdapter.TYPE_LIST;

    private int scaleSize = SCALE_MIDDLE;

    public int getScaleSize() {
        return scaleSize;
    }

    public void setScaleSize(int scaleSize) {
        this.scaleSize = scaleSize;
    }

    public int getListType() {
        return listType;
    }

    public void setListType(int listType) {
        this.listType = listType;
    }
}
