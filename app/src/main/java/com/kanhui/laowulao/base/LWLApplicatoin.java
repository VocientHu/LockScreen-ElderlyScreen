package com.kanhui.laowulao.base;

import android.app.Application;

import com.kanhui.laowulao.config.Constants;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

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
