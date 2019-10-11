package com.kanhui.laowulao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.kanhui.laowulao.service.LockerService;
import com.kanhui.laowulao.utils.SharedUtils;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
            if(!SharedUtils.getInstance().getAutoStart()){
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, LockerService.class));
            } else {
                context.startService(new Intent(context, LockerService.class));
            }
        }
    }
}
