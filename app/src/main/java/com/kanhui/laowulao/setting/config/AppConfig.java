package com.kanhui.laowulao.setting.config;

import android.util.SparseArray;

import java.util.List;

public class AppConfig {

    private int iconSize;
    private int nameSize;
    private List<String> apps;

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    public int getNameSize() {
        return nameSize;
    }

    public void setNameSize(int nameSize) {
        this.nameSize = nameSize;
    }

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }
}
