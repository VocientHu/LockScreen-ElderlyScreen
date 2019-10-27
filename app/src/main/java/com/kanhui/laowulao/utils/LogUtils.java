package com.kanhui.laowulao.utils;

import android.util.Log;

import static com.kanhui.laowulao.config.Variants.isDebug;

public class LogUtils {

    public static void elog(String tag,String log){
        if(isDebug){
            Log.e(tag,log);
        }
    }
    public static void log(String tag,String log){
        if(isDebug){
            Log.d(tag,log);
        }
    }


}
