package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by m on 16-11-2.
 */
public class CityListItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String city_id; // 城市id1
    private String city_name; // 城市名称1

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}