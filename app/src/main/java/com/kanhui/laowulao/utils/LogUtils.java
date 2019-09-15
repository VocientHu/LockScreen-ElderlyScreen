package com.kanhui.laowulao.utils;

import android.util.Log;

public class LogUtils {
    private static final boolean isDebug = true;

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
