package com.kanhui.laowulao.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.kanhui.laowulao.locker.model.AppsModel;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    private static AppUtils Instance;

    private Context context;

    public static AppUtils getInstance(Context context){
        if(Instance == null){
            Instance = new AppUtils(context);
        }
        return Instance;
    }

    private AppUtils(Context context){
        this.context = context;
    }

    public void init(){
        pm = context.getPackageManager();
        pakageinfos = pm.getInstalledApplications(0);
    }

    private List<ApplicationInfo> pakageinfos;
    private PackageManager pm ;
    private Drawable getIcon(String name){

        if(pakageinfos == null || pm == null){
            pm = context.getPackageManager();
            pakageinfos = pm.getInstalledApplications(0);
        }
        for(int i = 0 ; i < pakageinfos.size() ; i++){
            ApplicationInfo info = pakageinfos.get(i);
            String label = info.loadLabel(pm).toString();
            if(label.equals(name)){
                Drawable d = info.loadIcon(pm);
                return d;
            }
        }
        return null;
    }


    public void setIconToImageView(final String name, final ImageView iv){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Drawable d = (Drawable) msg.obj;
                if(iv != null && name.equals(iv.getTag())){
                    iv.setImageDrawable(d);
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                super.run();
                Message msg = new Message();
                msg.obj = getIcon(name);
                handler.sendMessage(msg);
            }
        }.start();
    }


    public AppsModel getModelByName(String name){
        AppsModel model = new AppsModel();
        if(pakageinfos == null || pm == null){
            pm = context.getPackageManager();
            pakageinfos = pm.getInstalledApplications(0);
        }
        for(int i = 0 ; i < pakageinfos.size() ; i++){
            ApplicationInfo info = pakageinfos.get(i);
            String label = info.loadLabel(pm).toString();
            if(label.equals(name)){
                Drawable d = info.loadIcon(pm);
                model.setAppName(name);
                model.setPackageName(info.packageName);
                model.setAppIcon(d);
                return model;
            }
        }
        return model;
    }

    // 判断service是否存活
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName()
                    .contains(ServiceName)) {
                return true;
            }
        }
        return false;
    }
}
