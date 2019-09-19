package com.kanhui.laowulao.setting;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.locker.model.ContactModel;
import com.kanhui.laowulao.setting.adapter.SettingContactAdapter;
import com.kanhui.laowulao.widget.WeatherView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_SELECT_PHONE_NUMBER = 1;// 选择联系人

    private RecyclerView rvContacts;
    private SettingContactAdapter contactAdapter;
    private List<ContactModel> contactList = new ArrayList<>();

    private WeatherView weatherView;

    private String city = "深圳市";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_save).setOnClickListener(this);
        weatherView = findViewById(R.id.wv_setting);
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
        });
        initWeather();
    }

    private void initWeather(){
        HeWeather.getWeatherForecast(SettingActivity.this, "深圳市", new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(Forecast forecast) {
                List<ForecastBase> listForecast = forecast.getDaily_forecast();
                if(listForecast != null){
                    ForecastBase today = listForecast.get(0);
                    ForecastBase tomorrow = listForecast.get(1);
                    setTodayWeather(today);
                    setTomorrowWeather(tomorrow);
                }

            }
        });
    }

    private void setTodayWeather(ForecastBase today){

        weatherView.setTodayWeather(today.getDate(),today.getCond_txt_d());
    }

    private void setTomorrowWeather(ForecastBase tomorrow){

        weatherView.setTodayWeather(tomorrow.getDate(),tomorrow.getCond_txt_d());
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {

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

                cursor.close();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:

                break;
        }
    }
}
