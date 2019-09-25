package com.kanhui.laowulao.setting;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.locker.model.AppsModel;
import com.kanhui.laowulao.locker.model.ContactModel;
import com.kanhui.laowulao.setting.adapter.AppSelectAdapter;
import com.kanhui.laowulao.setting.adapter.AppsAdapter;
import com.kanhui.laowulao.setting.adapter.SettingContactAdapter;
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
    public static final int REQUEST_SELECT_APPS = 2;// 选择联系人

    private RecyclerView rvContacts,rvApps;
    private SettingContactAdapter contactAdapter;
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
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        weatherView = findViewById(R.id.wv_setting);

        // 联系人相关
        rvContacts = findViewById(R.id.rv_contacts);
        contactAdapter = new SettingContactAdapter(SettingActivity.this);
        GridLayoutManager manager = new GridLayoutManager(SettingActivity.this,2);
        rvContacts.setLayoutManager (manager);
        rvContacts.setAdapter (contactAdapter);
        rvContacts.setItemAnimator (new DefaultItemAnimator());
        rvContacts.setHasFixedSize(true);
        rvContacts.setNestedScrollingEnabled(false);
        contactAdapter.setData(contactList);
        contactAdapter.setListener(new SettingContactAdapter.SettingContactAddListener() {
            @Override
            public void onAdd() {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
                }
            }

            @Override
            public void onDelete(final int position) {
                ContactModel model = contactList.get(position);
                final RealmResults<ContactModel> list = Realm.getDefaultInstance().where(ContactModel.class)
                        .like("name",model.getName()).findAllAsync();
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm) {
                        list.get(0).deleteFromRealm();
                        contactList.remove(position);
                        contactAdapter.setData(contactList);
                    }
                });

            }
        });

        // app相关
        rvApps = findViewById(R.id.rv_apps);
        appsAdapter = new AppsAdapter(SettingActivity.this,true);
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
        List<ContactModel> oldData = Realm.getDefaultInstance().where(ContactModel.class).findAllAsync();
        contactList.addAll(oldData);
        contactAdapter.setData(contactList);
    }

    // 读取应用配置数据
    private void initAppsData(){
        List<AppsModel> oldData = Realm.getDefaultInstance().where(AppsModel.class).findAllAsync();
        appsList.addAll(oldData);
        appsAdapter.setData(appsList);
    }




    static final String TAG = "SettingActivity";
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER) {
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String name = cursor.getString(nameIndex);      //联系人姓名
                String number = cursor.getString(numberIndex);  //联系人号码
                ContactModel model = new ContactModel();
                model.setName(name);
                model.setPhone(number);

                contactList.add(model);
                contactAdapter.setData(contactList);
                cursor.close();
            }
        } else if(requestCode == REQUEST_SELECT_APPS){
            if(AppSelectActivity.selectedResult != null){
                for(AppSelectAdapter.AppEntity entity : AppSelectActivity.selectedResult){
                    AppsModel model = new AppsModel();
                    model.setAppIcon(entity.getName());
                    model.setAppName(entity.getName());
                    model.setPackageName(entity.getPackageName());
                    appsList.add(model);
                }
                appsAdapter.setData(appsList);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                for(ContactModel model : contactList){
                    Realm  mRealm=Realm.getDefaultInstance();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(model);
                    mRealm.commitTransaction();
                }
                for(AppsModel model : appsList){
                    Realm  mRealm=Realm.getDefaultInstance();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(model);
                    mRealm.commitTransaction();
                }

                ToastUtils.showToast(SettingActivity.this,"保存成功");
                break;
        }
    }
}
