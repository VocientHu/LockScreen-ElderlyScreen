package com.kanhui.laowulao.config;

import android.content.Context;
import android.util.TypedValue;

import com.kanhui.laowulao.utils.SharedUtils;

public class Config {

    public static final String SHARED_USER_TYPE = "shared_user_type";
    public static final int USER_OLD = 1;
    public static final int USER_YOUNG = 2;

    public static final String SHARED_BIND_PHONES = "shared_bind_phones";
    // 最多添加6个app
    public static final int MAX_APPS = 6;
    // 最多添加4个联系人
    public static final int MAX_CONTACT = 4;

    public static final String BIG = "大";
    public static final String MIDDLE = "中";
    public static final String SMALL = "小";

    //锁屏界面联系人列表类型
    public static final int TYPE_GRIDE = 10;// 网格类型，两列
    public static final int TYPE_LIST = 11;// 列表类型

    public static final int SCALE_BIG = 32;// 字体大小，大
    public static final int SCALE_MIDDLE = 28;// 字体大小，中
    public static final int SCALE_SMALL = 24;// 字体大小，小
    public static final int S_SCALE_SMALL = 20;// 字体大小，小
    public static final int SS_SCALE_SMALL = 16;// 字体大小，小

    public static final int APP_IMG_BIG = 60;
    public static final int APP_IMG_MIDDLE = 50;
    public static final int APP_IMG_SMALL = 40;

    public static final int APP_NAME_BIG = 24;
    public static final int APP_NAME_MIDDLE = 20;
    public static final int APP_NAME_SMALL = 16;

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



    // 联系人列表类型，网格形和列表形
    private int listType = TYPE_LIST;

    // 联系人名字字体大小
    private int scaleSize = SCALE_MIDDLE;

    // 绑定的手机号
    private String bindPhones = "";

    // 分享的图片地址
    private String shareUrl = "";

    private int appImgSize = APP_IMG_MIDDLE;

    private int appNameSize = APP_NAME_SMALL;

    public int getAppImgSize() {
        return appImgSize;
    }

    public void setAppImgSize(int appImgSize) {
        this.appImgSize = appImgSize;
    }

    public int getAppNameSize() {
        return appNameSize;
    }

    public void setAppNameSize(int appNameSize) {
        this.appNameSize = appNameSize;
    }

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

    public String getBindPhones() {
        return bindPhones;
    }

    public void setBindPhones(String bindPhones) {
        this.bindPhones = bindPhones;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }




    /**
     * 根据配置字体大小返回大，中，小
     * @param size
     * @return
     */
    public static String getContactNameSize(int size){
        if(size == SCALE_BIG){
            return BIG;
        } else if(size == SCALE_MIDDLE){
            return MIDDLE;
        } else {
            return SMALL;
        }
    }

    public static String getAPPImgSize(int size){
        if(size == APP_IMG_BIG){
            return BIG;
        } else if(size == APP_IMG_MIDDLE){
            return MIDDLE;
        } else {
            return SMALL;
        }
    }

    public static String getAPPNameSize(int size){
        if(size == APP_NAME_BIG){
            return BIG;
        } else if(size == APP_NAME_MIDDLE){
            return MIDDLE;
        } else {
            return SMALL;
        }
    }

    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int dp2px(Context context,int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


}
