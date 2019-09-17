package com.kanhui.laowulao.utils;

import android.telephony.PhoneNumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isPhoneNumber(String phoneNo) {
        if (StringUtils.isEmpty(phoneNo)) {
            return false;
        }
        if (phoneNo.length() == 11) {
            for (int i = 0; i < 11; i++) {
                if (!PhoneNumberUtils.isISODigit(phoneNo.charAt(i))) {
                    return false;
                }
            }
            Pattern p = Pattern.compile("^((13[^4,\\D])" + "|(134[^9,\\D])" +
                    "|(14[5,7])" +
                    "|(15[^4,\\D])" +
                    "|(17[3,6-8])" +
                    "|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(phoneNo);
            return m.matches();
        }
        return false;
    }
}
