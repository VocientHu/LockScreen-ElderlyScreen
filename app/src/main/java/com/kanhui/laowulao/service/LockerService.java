package com.kanhui.laowulao.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.gson.Gson;
import com.kanhui.laowulao.MainActivity;
import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.LWLApplicatoin;
import com.kanhui.laowulao.locker.LockerActivity;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.locker.model.SMSModel;
import com.kanhui.laowulao.utils.StringUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 锁屏服务
 * 注册屏幕打开广播监听，并保活。确保锁屏能正常使用
 * 可开机启动，可短信激活。可以设置开关
 */
public class LockerService extends Service {
    private static final String TAG = "LockerService";

    private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_FLAG = "[easycall]";

    private static final String CHANNEL_ONE_ID = "com.kanhui.laowulao";
    private static final String CHANNEL_ONE_NAME = "Channel One";

    private static final int RECEIVERED_MSG = 1;

    private Intent serviceIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        initBroadcast();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
        // TODO user can select
        addNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void addNotification(){


        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }

        Notification.Builder builder = new Notification.Builder(LWLApplicatoin.getInstance());
        Intent mIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this,0,mIntent,0))

                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("锁屏服务已开启")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("屏幕开启后即可选择联系人拨打电话")
                .setWhen(System.currentTimeMillis());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ONE_ID);
        }
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;
        startForeground(110, notification);
    }



    private void initBroadcast(){
        IntentFilter filter = new IntentFilter();
        // 屏幕关闭
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver,filter);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Intent.ACTION_SCREEN_OFF.equals(action)){
                lockScreen();
            }
        }
    };

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


    private void lockScreen(){
        Log.e("service","start activity");
        Intent mIntent = new Intent(LockerService.this, LockerActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        stopForeground(true);
        // 保活
        if(serviceIntent != null){
            startService(serviceIntent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
