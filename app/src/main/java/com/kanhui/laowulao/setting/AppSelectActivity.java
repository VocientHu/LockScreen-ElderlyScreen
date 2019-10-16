package com.kanhui.laowulao.setting;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.setting.adapter.AppSelectAdapter;
import com.kanhui.laowulao.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AppSelectActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOAD_SUCCESS = 0;

    private RecyclerView rvList;
    private AppSelectAdapter adapter;
    private TextView tvTitle;
    private SearchView searchView;

    private List<AppSelectAdapter.AppEntity> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_select_apps;
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

    private void resetSearchViewBorder(SearchView svCustomer) {
        try {
            Class<?> argClass = svCustomer.getClass();
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(svCustomer);
            mView.setBackgroundResource(R.drawable.searchview_line);
        } catch (Exception e) {

        }
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
        searchView = findViewById(R.id.searchView);
        resetSearchViewBorder(searchView);
        rvList = findViewById(R.id.rv_list);
        LinearLayoutManager manager = new LinearLayoutManager(AppSelectActivity.this);
        rvList.setLayoutManager(manager);
        rvList.setItemAnimator (new DefaultItemAnimator());

        adapter = new AppSelectAdapter(AppSelectActivity.this);
        rvList.setAdapter(adapter);

        adapter.setListener(new AppSelectAdapter.OnClickListner() {
            @Override
            public void onClick(int index, AppSelectAdapter.AppEntity model) {
                model.setChecked(!model.isChecked());
                adapter.notifyDataSetChanged();
                //list.get(index).setChecked(!list.get(index).isChecked());
                adapter.setData(list);
            }
        });

        searchView.setIconified(false);// icon不要显示在框内
        searchView.setImeOptions(3);//设置键盘搜索键，默认为3
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                startSearch();
                return false;
            }
        });
//        setUnderLinetransparent(searchView);
    }

    /**设置SearchView下划线透明**/
//    private void setUnderLinetransparent(SearchView searchView){
//        try {
//            Class<?> argClass = searchView.getClass();
//            // ll_search_view是SearchView父布局的名字
//            Field ownField = argClass.getDeclaredField("ll_search_view");
//            ownField.setAccessible(true);
//            View mView = (View) ownField.get(searchView);
//            mView.setBackgroundColor(Color.TRANSPARENT);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    void startSearch(){
        String keywords = searchView.getQuery().toString();
        List<AppSelectAdapter.AppEntity> temp = new ArrayList<>();
        if(list == null || list.size() == 0){
            return;
        }
        if(StringUtils.isEmpty(keywords)){
            adapter.setData(list);
            return;
        }
        for(AppSelectAdapter.AppEntity entity : list){
            if(entity.getName().contains(keywords)){
                temp.add(entity);
            }
        }
        adapter.setData(temp);
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
