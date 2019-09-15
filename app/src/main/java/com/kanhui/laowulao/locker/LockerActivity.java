package com.kanhui.laowulao.locker;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kanhui.laowulao.MainActivity;
import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseActivity;
import com.kanhui.laowulao.base.LWLApplicatoin;
import com.kanhui.laowulao.locker.adapter.ContactAdapter;
import com.kanhui.laowulao.locker.model.Config;
import com.kanhui.laowulao.locker.model.ContactEngin;
import com.kanhui.laowulao.locker.model.ContactModel;
import com.kanhui.laowulao.utils.PermissionUtils;
import com.kanhui.laowulao.utils.ToastUtils;

import java.util.List;

import static com.kanhui.laowulao.utils.PermissionUtils.dealwithPermiss;

public class LockerActivity extends BaseActivity {

    private static final int LOAD_DATA = 1;

    private RecyclerView recyclerView;
    private TextView tvTitle;

    private ContactAdapter adapter;

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
        recyclerView = findViewById(R.id.rv_list);
        tvTitle = findViewById(R.id.tv_title);
        Config config = Config.getConfig();
        adapter = new ContactAdapter(LockerActivity.this,config.getListType());
        if(config.getListType() == ContactAdapter.TYPE_GRIDE){
            GridLayoutManager manager = new GridLayoutManager(this, 2);
            manager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
        } else {
            LinearLayoutManager manager = new LinearLayoutManager(LockerActivity.this);
            recyclerView.setLayoutManager(manager);
        }
        recyclerView.setAdapter(adapter);

        adapter.setListener(new ContactAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ContactModel model, int position,int type) {
                if(type == ContactAdapter.CALL_PHONE){
                    String phone = model.getPhone();
                    diallPhone(phone);
                } else if(type == ContactAdapter.CALL_VIDEO){
                    if(isWeixinAvilible(LockerActivity.this)){
                        openWeiXin();
                    } else {
                        ToastUtils.showToast(LockerActivity.this,"设备没有安装微信");
                    }
                }


            }
        });
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
        new Thread(){
            @Override
            public void run() {
                super.run();
                List<ContactModel> list =  ContactEngin.getInstance(LockerActivity.this).getContactsWidthLog();
                Message msg = new Message();
                msg.what = LOAD_DATA;
                msg.obj = list;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<ContactModel> list = (List<ContactModel>) msg.obj;
            adapter.setData(list);
            int count = list.size();
            String text = getResources().getString(R.string.constact_people_count);
            text = String.format(text,count+"");
            tvTitle.setText(text);

        }
    };

    private void openWeiXin(){
        Intent intent = new Intent();
        ComponentName cpn = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cpn);
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
