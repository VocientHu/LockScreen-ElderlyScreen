package com.kanhui.laowulao.base;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

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
