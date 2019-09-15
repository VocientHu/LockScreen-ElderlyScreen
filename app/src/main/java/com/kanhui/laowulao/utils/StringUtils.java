package com.kanhui.laowulao.utils;

public class StringUtils {

    public static boolean isEmpty(String str){
        if(str == null){
            return true;
        }
        if("".equals(str)){
            return true;
        }
        return false;
    }
}
