package com.kanhui.laowulao.setting.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseFragment;
import com.kanhui.laowulao.locker.model.AppsModel;
import com.kanhui.laowulao.setting.AppSelectActivity;
import com.kanhui.laowulao.setting.adapter.AppSelectAdapter;
import com.kanhui.laowulao.setting.adapter.AppsAdapter;
import com.kanhui.laowulao.setting.config.AppConfig;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.utils.ToastUtils;
import com.kanhui.laowulao.widget.SizePopupWindow;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AppConfigFragment extends BaseFragment implements View.OnClickListener {

    public static final int REQUEST_SELECT_APPS = 2;// 选择app
    private List<AppsModel> appsList = new ArrayList<>();

    private View rootView;

    private AppsAdapter appsAdapter;
    private RecyclerView rvApps;

    private AppConfig config;

    private TextView tvAppName,tvAppImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_app_config,null);
        initView();
        return rootView;
    }

    private void initView() {
        rootView.findViewById(R.id.btn_save).setOnClickListener(this);
        rootView.findViewById(R.id.rl_app_img_size).setOnClickListener(this);
        rootView.findViewById(R.id.rl_app_name_size).setOnClickListener(this);
        tvAppName = rootView.findViewById(R.id.tv_app_name_size);
        tvAppImg = rootView.findViewById(R.id.tv_app_img_size);

        rvApps = rootView.findViewById(R.id.rv_apps);
        appsAdapter = new AppsAdapter(context,true);
        GridLayoutManager appsManager = new GridLayoutManager(context,3);
        appsManager.setOrientation(GridLayoutManager.VERTICAL);
        rvApps.setLayoutManager (appsManager);
        rvApps.setAdapter (appsAdapter);
        rvApps.setItemAnimator (new DefaultItemAnimator());
        rvApps.setHasFixedSize(true);
        rvApps.setNestedScrollingEnabled(false);

        appsAdapter.setListener(new AppsAdapter.OnItemClickListener() {
            @Override
            public void onDelete(final int position) {
                appsList.remove(position);
                appsAdapter.setData(appsList);
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
                startActivityForResult(new Intent(context,AppSelectActivity.class),REQUEST_SELECT_APPS);
            }
        });
        initAppData();
    }

    private void initAppData(){
        config = SharedUtils.getInstance().getAppConfig();
        if(config == null){
            // TODO
        }
        List<String> appNames = config.getApps();
        for(String name : appNames){
            AppsModel model = new AppsModel();
            model.setAppName(name);
            appsList.add(model);
        }
        appsAdapter.setData(appsList);
        tvAppName.setText(config.getNameSize() + "");
        tvAppImg.setText(config.getIconSize() + "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(AppSelectActivity.selectedResult != null){
            appsList.clear();
            for(AppSelectAdapter.AppEntity entity : AppSelectActivity.selectedResult){
                AppsModel model = new AppsModel();
                model.setAppIcon(null);
                model.setAppName(entity.getName());
                model.setPackageName(entity.getPackageName());
                appsList.add(model);
            }
            appsAdapter.setData(appsList);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                List<String> list = new ArrayList<>();
                for(AppsModel model : appsList){
                    list.add(model.getAppName());
                }
                config.setApps(list);
                SharedUtils.getInstance().setAppConfig(config);
                ToastUtils.showToast(context,"保存成功");
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
            case R.id.rl_app_img_size:
                modifyImgSize(view);
                break;
            case R.id.rl_app_name_size:
                modifyNameSize(view);
                break;
        }
    }

    private void modifyNameSize(View view) {
        SizePopupWindow window = new SizePopupWindow(context, config.getNameSize(),
                new SizePopupWindow.SizeChagnedListener() {
                    @Override
                    public void onChanged(int size) {
                        config.setNameSize(size);
                        appsAdapter.refreshSize(config);
                        tvAppName.setText(size + "");
                    }
                });
        window.showAsDropDown(view,0,0, Gravity.BOTTOM);
    }

    private void modifyImgSize(View view) {
        SizePopupWindow window = new SizePopupWindow(context, config.getIconSize(),
                new SizePopupWindow.SizeChagnedListener() {
                    @Override
                    public void onChanged(int size) {
                        config.setIconSize(size);
                        appsAdapter.refreshSize(config);
                        tvAppImg.setText(size + "");
                    }
                });
        window.showAsDropDown(view,0,0, Gravity.BOTTOM);
    }
}
