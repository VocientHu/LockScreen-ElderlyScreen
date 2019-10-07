package com.kanhui.laowulao.setting.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.base.BaseFragment;
import com.kanhui.laowulao.locker.model.ContactModel;
import com.kanhui.laowulao.setting.SettingActivity;
import com.kanhui.laowulao.setting.adapter.SettingContactAdapter;
import com.kanhui.laowulao.setting.config.ContactConfig;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.utils.ToastUtils;
import com.kanhui.laowulao.widget.SizePopupWindow;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ContactFragment extends BaseFragment implements View.OnClickListener {
    public static final int REQUEST_SELECT_PHONE_NUMBER = 1;// 选择联系人

    private View rootView;

    private RecyclerView rvContacts;
    private TextView tvNameSize;

    private SettingContactAdapter contactAdapter;
    private List<ContactModel> contactList = new ArrayList<>();

    private ContactConfig config;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_app_config,null);
        initView();
        return rootView;
    }

    private void initView() {
        rootView.findViewById(R.id.rl_contact_size).setOnClickListener(this);
        rvContacts = rootView.findViewById(R.id.rv_contacts);
        tvNameSize = rootView.findViewById(R.id.tv_name_size);

        contactAdapter = new SettingContactAdapter(context);
        GridLayoutManager manager = new GridLayoutManager(context,2);
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
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
                }
            }

            @Override
            public void onDelete(final int position) {
                contactList.remove(position);
                contactAdapter.setData(contactList);
            }
        });

        initData();
    }

    private void initData() {
        config = SharedUtils.getInstance().getContactConfig();

        contactList = config.getContacts();
        contactAdapter.setData(contactList);
        tvNameSize.setText(config.getNameSize() + "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri contactUri = data.getData();
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(contactUri, projection,
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
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                config.setContacts(contactList);
                SharedUtils.getInstance().setContactConfig(config);
                ToastUtils.showToast(context,"保存成功");
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
            case R.id.rl_contact_size:
                modifyNameSize(view);
                break;
        }
    }

    private void modifyNameSize(View v){
        SizePopupWindow window = new SizePopupWindow(context, config.getNameSize(),
                new SizePopupWindow.SizeChagnedListener() {
                    @Override
                    public void onChanged(int size) {
                        tvNameSize.setText(size + "");
                        config.setNameSize(size);
                        contactAdapter.refreshSize(config);
                    }
                });
    }
}
