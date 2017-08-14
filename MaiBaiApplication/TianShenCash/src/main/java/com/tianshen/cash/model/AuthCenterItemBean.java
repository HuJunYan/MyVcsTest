package com.tianshen.cash.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class AuthCenterItemBean implements MultiItemEntity {

    public static final int NORMAL_TYPE = 1;
    public static final int TXT_TYPE = 2;

    private int drawable_id;
    private String name;
    private String status;
    private int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDrawable_id() {
        return drawable_id;
    }

    public void setDrawable_id(int drawable_id) {
        this.drawable_id = drawable_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int getItemType() {
        return itemType;
    }
}
