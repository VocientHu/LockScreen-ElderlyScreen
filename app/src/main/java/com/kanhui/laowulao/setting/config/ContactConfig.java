package com.kanhui.laowulao.setting.config;

import android.util.SparseArray;

import com.kanhui.laowulao.locker.model.ContactModel;

import java.util.List;

public class ContactConfig {

    private int nameSize;
    private int phoneSize;
    private List<ContactModel> contacts;

    public int getNameSize() {
        return nameSize;
    }

    public void setNameSize(int nameSize) {
        this.nameSize = nameSize;
    }

    public int getPhoneSize() {
        return phoneSize;
    }

    public void setPhoneSize(int phoneSize) {
        this.phoneSize = phoneSize;
    }

    public List<ContactModel> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactModel> contacts) {
        this.contacts = contacts;
    }
}
