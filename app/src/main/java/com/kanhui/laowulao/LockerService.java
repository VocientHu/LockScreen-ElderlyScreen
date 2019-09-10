package com.kanhui.laowulao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class LockerService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        initBroardcast();
    }

    void initBroardcast(){
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerBroardcast(filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Intent.ACTION_SCREEN_OFF.equals(action)){
                Intent mIntent = new Intent(LockerService.this,LockerActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
            }
        }
    };

    private void registerBroardcast(IntentFilter filter) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
