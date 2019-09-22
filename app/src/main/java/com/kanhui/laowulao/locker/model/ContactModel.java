package com.kanhui.laowulao.locker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ContactModel  extends RealmObject {
    public static final int CALL_IN = 1;
    public static final int CALL_OUT = 2;
    public static final int CALL_UNKNOWN= 3;
    // 姓名
    String name;
    // 手机号
    @PrimaryKey
    String phone;
    // 上次通话时间
    String lastTime;
    // 头像地址
    String header;
    // 拨号时间，显示str
    String dateStr;
    // 拨号时间，时间戳
    long datetime = 0;
    // 通话时长
    String callTime;
    // 是否在通话记录表里，默认不在
    boolean isInLog = false;

    public boolean isInLog() {
        return isInLog;
    }

    public void setInLog(boolean inLog) {
        isInLog = inLog;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    int callType;

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
