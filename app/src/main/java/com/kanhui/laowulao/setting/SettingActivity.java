package com.kanhui.laowulao.setting;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.locker.adapter.ContactAdapter;
import com.kanhui.laowulao.locker.model.AppsModel;
import com.kanhui.laowulao.locker.model.ContactModel;
import com.kanhui.laowulao.setting.adapter.AppSelectAdapter;
import com.kanhui.laowulao.setting.adapter.AppsAdapter;
import com.kanhui.laowulao.setting.adapter.SettingContactAdapter;
import com.kanhui.laowulao.setting.config.AppConfig;
import com.kanhui.laowulao.setting.config.ContactConfig;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.utils.ToastUtils;
import com.kanhui.laowulao.widget.WeatherView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_SELECT_PHONE_NUMBER = 1;// 选择联系人
    public static final int REQUEST_SELECT_APPS = 2;// 选择app
    public static final int REQUEST_SELECT_WEATHER = 3;// 天气配置
    private static final String TAG = "SettingActivity";

    private RecyclerView rvContacts,rvApps;
    private ContactAdapter contactAdapter;
    private AppsAdapter appsAdapter;

    private List<ContactModel> contactList = new ArrayList<>();
    private List<AppsModel> appsList = new ArrayList<>();

    private WeatherView weatherView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.iv_contact_config).setOnClickListener(this);
        findViewById(R.id.iv_weather_config).setOnClickListener(this);
        findViewById(R.id.iv_contact_config).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        weatherView = findViewById(R.id.wv_setting);

        // 联系人相关
        rvContacts = findViewById(R.id.rv_contacts);
        contactAdapter = new ContactAdapter(SettingActivity.this, Config.TYPE_GRIDE);
        GridLayoutManager manager = new GridLayoutManager(SettingActivity.this,2);
        rvContacts.setLayoutManager (manager);
        rvContacts.setAdapter (contactAdapter);
        rvContacts.setItemAnimator (new DefaultItemAnimator());
        rvContacts.setHasFixedSize(true);
        rvContacts.setNestedScrollingEnabled(false);
        contactAdapter.setData(contactList);


        // app相关
        rvApps = findViewById(R.id.rv_apps);
        appsAdapter = new AppsAdapter(SettingActivity.this,false);
        GridLayoutManager appsManager = new GridLayoutManager(SettingActivity.this,3);
        appsManager.setOrientation(GridLayoutManager.VERTICAL);
        rvApps.setLayoutManager (appsManager);
        rvApps.setAdapter (appsAdapter);
        rvApps.setItemAnimator (new DefaultItemAnimator());
        rvApps.setHasFixedSize(true);
        rvApps.setNestedScrollingEnabled(false);
        appsAdapter.setListener(new AppsAdapter.OnItemClickListener() {
            @Override
            public void onDelete(final int position) {
                AppsModel model = appsList.get(position);
                final RealmResults<AppsModel> list = Realm.getDefaultInstance().where(AppsModel.class)
                        .like("appName",model.getAppName()).findAllAsync();
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm) {
                        list.get(0).deleteFromRealm();
                        appsList.remove(position);
                        appsAdapter.setData(appsList);
                    }
                });
            }

            @Override
            public void onClick(int position, AppsModel model) {
                // do nothing
            }

            @Override
            public void onAdd() {
                List<AppSelectAdapter.AppEntity> list = new ArrayList<>();
                for(AppsModel model : appsList){
                    AppSelectAdapter.AppEntity entity = new AppSelectAdapter.AppEntity();
                    entity.setName(model.getAppName());
                    list.add(entity);
                }
                AppSelectActivity.selectedResult = list;
                startActivityForResult(new Intent(SettingActivity.this,AppSelectActivity.class),REQUEST_SELECT_APPS);
            }
        });

        initAppsData();

        initContactData();
    }

    // 读取联系人配置数据
    private void initContactData(){
        ContactConfig config = SharedUtils.getInstance().getContactConfig();
        contactList.addAll(config.getContacts());
        contactAdapter.setData(contactList);
        contactAdapter.refreshSize(config);
    }

    // 读取应用配置数据
    private void initAppsData(){
        AppConfig config = SharedUtils.getInstance().getAppConfig();
        List<String> appNames = config.getApps();
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
                intent = new Intent(SettingActivity.this,ConfigActivity.class);
                intent.putExtra(ConfigActivity.EXTRA_TYPE,ConfigActivity.EXTRA_CONTACT);
                startActivityForResult(intent,REQUEST_SELECT_PHONE_NUMBER);
                break;
            case R.id.iv_app_config:
                intent = new Intent(SettingActivity.this,ConfigActivity.class);
                intent.putExtra(ConfigActivity.EXTRA_TYPE,ConfigActivity.EXTRA_APP);
                startActivityForResult(intent,REQUEST_SELECT_APPS);
                break;
            case R.id.iv_weather_config:
                intent = new Intent(SettingActivity.this,ConfigActivity.class);
                intent.putExtra(ConfigActivity.EXTRA_TYPE,ConfigActivity.EXTRA_WEATHER);
                startActivityForResult(intent,REQUEST_SELECT_WEATHER);
                break;

        }
    }

}
