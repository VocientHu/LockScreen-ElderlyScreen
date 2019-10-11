package com.kanhui.laowulao.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.SendSMSActivity;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.locker.adapter.ContactAdapter;
import com.kanhui.laowulao.locker.model.AppsModel;
import com.kanhui.laowulao.locker.model.ContactModel;
import com.kanhui.laowulao.setting.adapter.AppsAdapter;
import com.kanhui.laowulao.setting.config.AppConfig;
import com.kanhui.laowulao.setting.config.ContactConfig;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.widget.WeatherView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 锁屏设计页面
 */
public class InspectorActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_SELECT_PHONE_NUMBER = 1;// 选择联系人
    public static final int REQUEST_SELECT_APPS = 2;// 选择app
    public static final int REQUEST_SELECT_WEATHER = 3;// 天气配置

    private static final int APPS_DATA = 1;
    private static final int CONTACT_DATA = 2;

    private RecyclerView rvContacts,rvApps;
    private ContactAdapter contactAdapter;
    private AppsAdapter appsAdapter;

    private List<ContactModel> contactList = new ArrayList<>();
    private List<AppsModel> appsList = new ArrayList<>();

    private WeatherView weatherView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_inspector;
    }

    private void initView() {
        findViewById(R.id.iv_app_config).setOnClickListener(this);
        findViewById(R.id.iv_weather_config).setOnClickListener(this);
        findViewById(R.id.iv_contact_config).setOnClickListener(this);
        findViewById(R.id.iv_send).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        weatherView = findViewById(R.id.wv_setting);
        // 联系人相关
        rvContacts = findViewById(R.id.rv_contacts);
        contactAdapter = new ContactAdapter(InspectorActivity.this, Config.TYPE_GRIDE);
        GridLayoutManager manager = new GridLayoutManager(InspectorActivity.this,2);
        rvContacts.setLayoutManager (manager);
        rvContacts.setAdapter (contactAdapter);
        rvContacts.setItemAnimator (new DefaultItemAnimator());
        rvContacts.setHasFixedSize(true);
        rvContacts.setNestedScrollingEnabled(false);
        contactAdapter.setData(contactList);


        // app相关
        rvApps = findViewById(R.id.rv_apps);
        appsAdapter = new AppsAdapter(InspectorActivity.this,false);
        GridLayoutManager appsManager = new GridLayoutManager(InspectorActivity.this,3);
        appsManager.setOrientation(GridLayoutManager.VERTICAL);
        rvApps.setLayoutManager (appsManager);
        rvApps.setAdapter (appsAdapter);
        rvApps.setItemAnimator (new DefaultItemAnimator());
        rvApps.setHasFixedSize(true);
        rvApps.setNestedScrollingEnabled(false);

        initAppsData();

        initContactData();
    }

    // 读取联系人配置数据
    private void initContactData(){
        ContactConfig config = SharedUtils.getInstance().getContactConfig();
        contactList.clear();
        contactList.addAll(config.getContacts());
        contactAdapter.setData(contactList);
        contactAdapter.refreshSize(config);
    }

    // 读取应用配置数据
    private void initAppsData(){
        AppConfig config = SharedUtils.getInstance().getAppConfig();
        List<String> appNames = config.getApps();
        appsList.clear();
        for(String name : appNames){
            AppsModel model = new AppsModel();
            model.setAppName(name);
            appsList.add(model);
        }
        appsAdapter.setData(appsList);
        appsAdapter.refreshSize(config);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }
        initAppsData();
        initContactData();
        weatherView.refreshSize(null);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.iv_contact_config:
                intent = new Intent(InspectorActivity.this,ConfigActivity.class);
                intent.putExtra(ConfigActivity.EXTRA_TYPE,ConfigActivity.EXTRA_CONTACT);
                startActivityForResult(intent,REQUEST_SELECT_PHONE_NUMBER);
                break;
            case R.id.iv_app_config:
                intent = new Intent(InspectorActivity.this,ConfigActivity.class);
                intent.putExtra(ConfigActivity.EXTRA_TYPE,ConfigActivity.EXTRA_APP);
                startActivityForResult(intent,REQUEST_SELECT_APPS);
                break;
            case R.id.iv_weather_config:
                intent = new Intent(InspectorActivity.this,ConfigActivity.class);
                intent.putExtra(ConfigActivity.EXTRA_TYPE,ConfigActivity.EXTRA_WEATHER);
                startActivityForResult(intent,REQUEST_SELECT_WEATHER);
                break;
            case R.id.iv_send:
                intent = new Intent(InspectorActivity.this, SendSMSActivity.class);
                intent.putExtra(SendSMSActivity.EXTRA_SMS_TYPE,SendSMSActivity.SEND_ALL);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(weatherView != null){
            weatherView.onDestroy();
        }
    }
}
