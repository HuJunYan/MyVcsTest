package com.tianshen.cash.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-9-14.
 */
public class MerchantTypeItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type_id;  // 分类id1
    private String type_name;  // 分类名称1
    private String type_img; // 分类图片

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_img() {
        return type_img;
    }

    public void setType_img(String type_img) {
        this.type_img = type_img;
    }
}

