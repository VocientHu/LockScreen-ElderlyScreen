package com.kanhui.laowulao.locker;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.base.LWLApplicatoin;
import com.kanhui.laowulao.locker.adapter.ContactAdapter;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.locker.model.AppsModel;
import com.kanhui.laowulao.locker.model.ContactModel;
import com.kanhui.laowulao.setting.adapter.AppsAdapter;
import com.kanhui.laowulao.utils.StringUtils;
import com.kanhui.laowulao.utils.ToastUtils;
import com.kanhui.laowulao.widget.BatteryView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;

import static com.kanhui.laowulao.utils.PermissionUtils.dealwithPermiss;

public class LockerActivity extends BaseActivity {

    private static final int LOAD_DATA = 1;

    // 联系人，app列表
    private RecyclerView recyclerView,rvApps;
    private TextView tvTitle;

    private ContactAdapter adapter;// 联系人
    private AppsAdapter appsAdapter;// apps
    private BatteryView bvBattery;// 电源

    private WebView wvShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //替换系统锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_locker);
        // 避免多个activity
        LWLApplicatoin.getInstance().clearActivity();
        LWLApplicatoin.getInstance().addActivity(this);

        initView();
        initData();
    }

    void initView() {
        initBaterry();
        initApps();
        initContact();
        initWebView();
    }

    private void initWebView() {
        wvShare = findViewById(R.id.wv_share);
        if(!StringUtils.isEmpty(Config.getConfig().getShareUrl())){
            wvShare.loadUrl(Config.getConfig().getShareUrl());
            wvShare.setVisibility(View.VISIBLE);
        } else {
            wvShare.setVisibility(View.GONE);
        }
    }

    private void initApps(){
        rvApps = findViewById(R.id.rv_app_list);
        //Config config = Config.getConfig();
        appsAdapter = new AppsAdapter(LockerActivity.this);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        rvApps.setLayoutManager(manager);
        rvApps.setAdapter(appsAdapter);

        appsAdapter.setListener(new AppsAdapter.OnItemClickListener() {
            @Override
            public void onDelete(int position) {
                // nothing
            }

            @Override
            public void onClick(int position, AppsModel model) {
                // open app
                openApp(model);
            }

            @Override
            public void onAdd() {
                // nothing
            }
        });
    }

    private void initContact(){
        // 联系人相关
        recyclerView = findViewById(R.id.rv_list);
        tvTitle = findViewById(R.id.tv_contact_title);
        Config config = Config.getConfig();
        adapter = new ContactAdapter(LockerActivity.this,Config.TYPE_GRIDE);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new ContactAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ContactModel model, int position,int type) {
                if(type == ContactAdapter.CALL_PHONE){
                    String phone = model.getPhone();
                    diallPhone(phone);
                } else if(type == ContactAdapter.CALL_VIDEO){
                    if(isWeixinAvilible(LockerActivity.this)){
                        //openWeiXin();
                    } else {
                        ToastUtils.showToast(LockerActivity.this,"设备没有安装微信");
                    }
                }


            }
        });

    }

    // 初始化电源状态
    private void initBaterry(){
        bvBattery = findViewById(R.id.bv_battery);
        // api > 5.0
        if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            bvBattery.setVisibility(View.VISIBLE);
            BatteryManager manager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            int percent = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            bvBattery.setPercent(percent);
            if(percent <= 20){
                findViewById(R.id.tv_battery).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.tv_battery).setVisibility(View.GONE);
            }
        } else {
            bvBattery.setVisibility(View.GONE);
        }

    }

    public void diallPhone(String phoneNum) {

        Intent intent = new Intent(Intent.ACTION_CALL);

        Uri data = Uri.parse("tel:" + phoneNum);

        intent.setData(data);
        String[] permissions = {Manifest.permission.CALL_PHONE};

        if (ContextCompat.checkSelfPermission(LockerActivity.this,Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            dealwithPermiss(LockerActivity.this);
            return;
        }
        startActivity(intent);
        finish();
    }



    void initData(){
        tvTitle.setText(R.string.loading);
        List<ContactModel> list = new ArrayList<>();
        List<ContactModel> oldData = Realm.getDefaultInstance().where(ContactModel.class).findAllAsync();
        list.addAll(oldData);
        adapter.setData(list);
        int count = list.size();
        String text = getResources().getString(R.string.constact_people_count);
        text = String.format(text,count+"");
        tvTitle.setText(text);

        // app
        List<AppsModel> apps = new ArrayList<>();
        List<AppsModel> oldApps = Realm.getDefaultInstance().where(AppsModel.class).findAllAsync();
        apps.addAll(oldApps);
        appsAdapter.setData(apps);
    }


    private void openApp(AppsModel model){
        Intent intent = getPackageManager().getLaunchIntentForPackage(model.getPackageName());
        startActivity(intent);
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

}
