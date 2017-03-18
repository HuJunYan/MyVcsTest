package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/2.
 */

public class ProvinceBean implements Serializable{
    private String province_id;
    private String province_name;

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }
}
