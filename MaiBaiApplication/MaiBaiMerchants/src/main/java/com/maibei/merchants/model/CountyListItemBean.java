package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by m on 16-11-2.
 */
public class CountyListItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String county_id; // 州县id1
    private String county_name; // 州县名称1

    public String getCounty_id() {
        return county_id;
    }

    public void setCounty_id(String county_id) {
        this.county_id = county_id;
    }

    public String getCounty_name() {
        return county_name;
    }

    public void setCounty_name(String county_name) {
        this.county_name = county_name;
    }
}