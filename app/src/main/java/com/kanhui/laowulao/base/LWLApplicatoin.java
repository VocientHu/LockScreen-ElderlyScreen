package com.kanhui.laowulao.base;

import android.app.Application;

import com.kanhui.laowulao.config.Constants;
import com.kanhui.laowulao.utils.AppUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LWLApplicatoin extends Application {

    private static LWLApplicatoin application;

    private List<BaseActivity> activitiStack = new ArrayList<>();

    public static LWLApplicatoin getInstance(){
        return application;
    };



    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        // init bugly
        CrashReport.initCrashReport(getApplicationContext(), Constants.BuglyAppId, false);

        // init realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("laowulao.realm").build();
        Realm.setDefaultConfiguration(config);
        // init hefeng weather
        HeConfig.init("HE1909181806551611","67571b85940e498ea3e8d076745a00f7");
        // 免费域名
        HeConfig.switchToFreeServerNode();
        // 付费域名,默认
        //HeConfig.switchToCNBusinessServerNode();

        AppUtils.getInstance(this).init();
    }

    public void addActivity(BaseActivity activity){
        activitiStack.add(activity);
    }

    public void popActivity(){
        if(activitiStack.size() > 0){
            return;
        }
        BaseActivity act = activitiStack.get(activitiStack.size() - 1);
        act.finish();
        activitiStack.remove(act);
    }

    public boolean hasLockerActivity(){
        return activitiStack.size() > 0;
    }

    public void clearActivity(){
        for(BaseActivity act : activitiStack){
            act.finish();
        }
        activitiStack.clear();
    }
}
