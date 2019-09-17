package com.kanhui.laowulao.locker.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;

import com.kanhui.laowulao.utils.LogUtils;
import com.kanhui.laowulao.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.core.content.ContextCompat;

public class ContactEngine {

    private Context context;

    private static ContactEngine Instance;

    public static ContactEngine getInstance(Context context) {
        if (Instance == null) {
            Instance = new ContactEngine(context);
        }
        return Instance;
    }

    private ContactEngine(Context c) {
        this.context = c;
    }

    public List<ContactModel> getContactsWidthLog(){
        List<ContactModel> list = getContacts();
        HashMap<String,ContactModel> map = getCallHistoryList();

        for(int i = 0; i < list.size(); i++){
            ContactModel model = list.get(i);
            ContactModel logModel = map.get(model.getPhone());
            if(logModel != null){
                model.setCallTime(logModel.getCallTime());
                model.setCallType(logModel.getCallType());
                model.setDateStr(logModel.getDateStr());
                model.setDatetime(logModel.getDatetime());
            }
            LogUtils.elog("Model","上次通话时间戳" + model.getDatetime());
        }
        Collections.sort(list, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel contactModel, ContactModel t1) {
                long c = contactModel.getDatetime() - t1.getDatetime();
                int result = c > 0 ? -1 : c == 0 ? 0 : 1;
                return result;
            }
        });
        return list;
    }

    private List<ContactModel> getContacts() {
        List<ContactModel> list = new ArrayList<>();
        Cursor cursor = null;
        try {

            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null,
                    ContactsContract.CommonDataKinds.Phone._ID + " desc");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    ContactModel model = new ContactModel();
                    String displayName = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (StringUtils.isEmpty(number) || StringUtils.isEmpty(displayName)) {
                        continue;
                    }
                    number = number.replace("+86 ", "");
                    number = number.replace("+86", "");
                    if (!StringUtils.isPhoneNumber(number)) {
                        continue;
                    }
                    if (displayName.equals(number)) {
                        continue;
                    }
                    String photo = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                    model.setName(displayName);
                    model.setPhone(number);
                    model.setHeader(photo);
                    list.add(model);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }



    private HashMap<String,ContactModel> getCallHistoryList() {
        HashMap map = new HashMap();
        Cursor cs;
        if (ContextCompat.checkSelfPermission(context,Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return map;
        }
        cs = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                new String[]{
                        CallLog.Calls.CACHED_NAME,  //姓名
                        CallLog.Calls.NUMBER,    //号码
                        CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                        CallLog.Calls.DATE,  //拨打时间
                        CallLog.Calls.DURATION,   //通话时长
                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        int i = 0;
        if (cs != null && cs.getCount() > 0) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date_today = simpleDateFormat.format(date);
            for (cs.moveToFirst(); (!cs.isAfterLast()) && i < 500; cs.moveToNext(), i++) {
                String callName = cs.getString(0);  //名称
                String callNumber = cs.getString(1);  //号码

                if(StringUtils.isEmpty(callNumber)){
                    continue;
                }

                //通话类型
                int callType = Integer.parseInt(cs.getString(2));
                //拨打时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                long callTimestamp = Long.parseLong(cs.getString(3));
                Date callDate = new Date(callTimestamp);
                String callDateStr = sdf.format(callDate);
                if (callDateStr.equals(date_today)) { //判断是否为今天
                    sdf = new SimpleDateFormat("HH:mm");
                    callDateStr = sdf.format(callDate);
                } else if (date_today.contains(callDateStr.substring(0, 7))) { //判断是否为当月
                    sdf = new SimpleDateFormat("dd");
                    int callDay = Integer.valueOf(sdf.format(callDate));

                    int day = Integer.valueOf(sdf.format(date));
                    if (day - callDay == 1) {
                        callDateStr = "昨天";
                    } else {
                        sdf = new SimpleDateFormat("MM-dd");
                        callDateStr = sdf.format(callDate);
                    }
                } else if (date_today.contains(callDateStr.substring(0, 4))) { //判断是否为当年
                    sdf = new SimpleDateFormat("MM-dd");
                    callDateStr = sdf.format(callDate);
                }

                //通话时长
                int callDuration = Integer.parseInt(cs.getString(4));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String callDurationStr = "";
                if (sec > 0) {
                    if (min > 0) {
                        callDurationStr = min + "分" + sec + "秒";
                    } else {
                        callDurationStr = sec + "秒";
                    }
                }
                ContactModel model = new ContactModel();
                model.setName(callName);
                model.setPhone(callNumber);
                model.setDateStr(callDateStr);
                model.setDatetime(callTimestamp);
                model.setCallTime(callDurationStr);
                map.put(callNumber,model);
            }
        }

        return map;
    }

}
