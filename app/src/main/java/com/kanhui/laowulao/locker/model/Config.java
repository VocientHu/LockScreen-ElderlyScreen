package com.kanhui.laowulao.locker.model;

import com.kanhui.laowulao.utils.SharedUtils;

public class Config {

    //锁屏界面联系人列表类型
    public static final int TYPE_GRIDE = 10;// 网格类型，两列
    public static final int TYPE_LIST = 11;// 列表类型

    public static final int SCALE_BIG = 32;// 字体大小，大
    public static final int SCALE_MIDDLE = 28;// 字体大小，中
    public static final int SCALE_SMALL = 24;// 字体大小，小

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



    private int listType = TYPE_LIST;

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
