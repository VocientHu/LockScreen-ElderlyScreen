package com.kanhui.laowulao.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Md5HeaderView extends AppCompatTextView {

    private static final int[] colors = {R.drawable.header_bg1,R.drawable.header_bg2,R.drawable.header_bg3,
                    R.drawable.header_bg4,R.drawable.header_bg5,R.drawable.header_bg6,R.drawable.header_bg7,R.drawable.header_bg8};


    public Md5HeaderView(Context context) {
        this(context,null);
    }

    public Md5HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Md5HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setText(String text){
        if(StringUtils.isEmpty(text)){
            return;
        }
        String subName = text.length() > 2 ? text.substring(text.length()-2) : text;
        super.setText(subName);

        setBackgroundResource(getMd5Color(text));
    }

    public static int getMd5Color(String text){
        int number = getMd5Index(text);
        int color = getColor(number);
        return color;
    }

    public static int getMd5Index(String text){
        String md5Text = md5Encode(text);
        // 截取第一个字符
        String firstCode = md5Text.substring(0,1);
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(firstCode);
        int number = 0;
        // 是数字
        if(m.matches() ){
            number = Integer.parseInt(firstCode);
        } else {
            char[] cs = firstCode.toCharArray();
            number = cs[0];
        }
        return number;
    }

    private static int getColor(int index){
        int color = colors[0];
        int i = index%colors.length;
        color = colors[i];
        return color;
    }

    private static String md5Encode(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException",e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
