package com.kanhui.laowulao.setting;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.setting.adapter.AppSelectAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppSelectActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOAD_SUCCESS = 0;

    private RecyclerView rvList;
    private AppSelectAdapter adapter;
    private TextView tvTitle;

    private List<AppSelectAdapter.AppEntity> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_apps);

        initView();

        initData();
    }

    private void initData() {
        tvTitle.setText("正在加载...");
        new Thread(){
            @Override
            public void run() {
                super.run();
                PackageManager pm = getPackageManager();
                List<ApplicationInfo> pakageinfos = pm.getInstalledApplications(0);
                List<String> appNames = new ArrayList<>();
                if(selectedResult != null){
                    for (AppSelectAdapter.AppEntity en : selectedResult){
                        appNames.add(en.getName());
                    }
                }
                for(int i = 0 ; i < pakageinfos.size() ; i++){
                    AppSelectAdapter.AppEntity entity = new AppSelectAdapter.AppEntity();
                    ApplicationInfo info = pakageinfos.get(i);
                    entity.setIcon(info.loadIcon(pm));
                    entity.setPackageName(info.packageName);
                    String name = info.loadLabel(pm).toString();
                    entity.setName(name);
                    if(appNames.contains(name)){
                        entity.setChecked(true);
                    }
                    list.add(entity);
                }
                handler.sendEmptyMessage(LOAD_SUCCESS);
            }
        }.start();


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == LOAD_SUCCESS){
                adapter.setData(list);
                tvTitle.setText(R.string.app_select);
            }

        }
    };

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_finish).setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_main);

        rvList = findViewById(R.id.rv_list);
        LinearLayoutManager manager = new LinearLayoutManager(AppSelectActivity.this);
        rvList.setLayoutManager(manager);
        rvList.setItemAnimator (new DefaultItemAnimator());

        adapter = new AppSelectAdapter(AppSelectActivity.this);
        rvList.setAdapter(adapter);

        adapter.setListener(new AppSelectAdapter.OnClickListner() {
            @Override
            public void onClick(int index) {
                list.get(index).setChecked(!list.get(index).isChecked());
                adapter.setData(list);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_finish:
                onComplete();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void onComplete(){
        List<AppSelectAdapter.AppEntity> selected = new ArrayList<>();
        for(AppSelectAdapter.AppEntity entity : list){
            if(entity.isChecked()){
                selected.add(entity);
            }
        }
        selectedResult = selected;
        setResult(Activity.RESULT_OK);
        finish();
    }

    public static List<AppSelectAdapter.AppEntity> selectedResult = null;
}
