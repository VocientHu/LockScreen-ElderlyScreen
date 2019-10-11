package com.kanhui.laowulao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;

import com.google.gson.Gson;
import com.kanhui.laowulao.config.CMDModel;
import com.kanhui.laowulao.locker.model.SMSModel;
import com.kanhui.laowulao.service.LockerService;
import com.kanhui.laowulao.setting.config.AppConfig;
import com.kanhui.laowulao.setting.config.ContactConfig;
import com.kanhui.laowulao.setting.config.PhoneModel;
import com.kanhui.laowulao.setting.config.WeatherConfig;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.utils.StringUtils;

import androidx.annotation.NonNull;

public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_FLAG = "[easycall]";

    public static final String SMS_TYPE_WEATHER = "[weather]";
    public static final String SMS_TYPE_APP = "[app]";
    public static final String SMS_TYPE_CONTACT = "[contact]";
    public static final String SMS_TYPE_CMD = "[cmd]";

    private static final int RECEIVERED_MSG = 1;

    Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        dealMessage(intent);


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
            PhoneModel bindPhoneModel = SharedUtils.getInstance().getPhoneModel();
            if(bindPhoneModel == null){
                return;
            }
            String bindPhone = bindPhoneModel.getPhone();
            if(StringUtils.isEmpty(bindPhone)){
                return;
            }
            bindPhone = bindPhone.replace(" ", "");// 去掉空格
            bindPhone = bindPhone.replace("+86","");// 去掉+86
            // 去掉86
            phone = phone.replace("+86","");
            if(phone.startsWith("86")){
                phone = phone.substring(2);
            }
            //只处理绑定手机发来的短信
            if(bindPhone.contains(phone)){
                if(!StringUtils.isEmpty(content) && content.startsWith(SMS_FLAG)){
                    // 配置文件
                    String configStr = content.replace(SMS_FLAG,"");
                    if(configStr.contains(SMS_TYPE_WEATHER)){
                        configStr = configStr.replace(SMS_TYPE_WEATHER,"");
                        WeatherConfig config = new Gson().fromJson(configStr,WeatherConfig.class);
                        SharedUtils.getInstance().setWeatherConfig(config);
                    } else if(configStr.contains(SMS_TYPE_APP)){
                        configStr = configStr.replace(SMS_TYPE_APP,"");
                        AppConfig config = new Gson().fromJson(configStr,AppConfig.class);
                        SharedUtils.getInstance().setAppConfig(config);
                    } else if(configStr.contains(SMS_TYPE_CONTACT)){
                        configStr = configStr.replace(SMS_TYPE_CONTACT,"");
                        ContactConfig config = new Gson().fromJson(configStr,ContactConfig.class);
                        SharedUtils.getInstance().setContactConfig(config);
                    } else if(configStr.contains(SMS_TYPE_CMD)){
                        configStr = configStr.replace(SMS_TYPE_CMD,"");
                        // 启动服务
                        if(CMDModel.CMD_START_SERVICE.equals(configStr)){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(new Intent(context, LockerService.class));
                            }
                        }
                    }
                }
            }
        }
    };
}
