package com.kanhui.laowulao.locker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AppsModel extends RealmObject {

    @PrimaryKey
    String appName;

    String appIcon;

    String packageName;

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }
}
