package com.kanhui.laowulao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;

import com.google.gson.Gson;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.locker.model.SMSModel;
import com.kanhui.laowulao.service.LockerService;
import com.kanhui.laowulao.utils.StringUtils;

import androidx.annotation.NonNull;

public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_FLAG = "[easycall]";

    private static final int RECEIVERED_MSG = 1;


    @Override
    public void onReceive(Context context, Intent intent) {
        dealMessage(intent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, LockerService.class));
        }
    }

    void dealMessage(Intent intent){
        Object[] object=(Object[]) intent.getExtras().get("pdus");
        SMSModel model = new SMSModel();
        for (Object pdus : object) {
            byte[] pdusMsg=(byte[]) pdus;
            SmsMessage sms= SmsMessage.createFromPdu(pdusMsg);
            String mobile=sms.getOriginatingAddress();//发送短信的手机号
            String content=sms.getMessageBody();//短信内容
            model.setPhone(mobile);
            model.setContent(content);
            break;

        }
        Message msg=new Message();
        msg.what = RECEIVERED_MSG;
        msg.obj = model;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            SMSModel model = (SMSModel) msg.obj;
            String phone = model.getPhone();
            String content = model.getContent();
            String bindPhons = Config.getConfig().getBindPhones();
            //只处理绑定手机发来的短信
            if(!StringUtils.isEmpty(bindPhons) && bindPhons.contains(phone)){
                if(!StringUtils.isEmpty(content) && content.startsWith(SMS_FLAG)){
                    // 配置文件
                    String configStr = content.replace(SMS_FLAG,"");
                    Config newConfig = new Gson().fromJson(configStr,Config.class);
                    Config oldConfig = Config.getConfig();
                    newConfig.setBindPhones(oldConfig.getBindPhones());
                    Config.setConfig(newConfig);
                }
            }
        }
    };
}
