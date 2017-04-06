package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by sbyh on 16/7/4.
 */

public class NearByMerchantItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public NearByMerchantItemBean() {
        promotion = new Promotion();
    }
    private String id; // 商户ID
    private String name;  //商户名称
    private String category;        //商户类型
    private String province;  //商户所在的省 (客户端暂未使用)
    private String city;     //商户所在的市 (客户端暂未使用)
    private String country;    //商户所在的区县 (客户端暂未使用)
    private String address;    //商户所在的详细地址 (客户端暂未使用)
    private String coordinate;  //商户所在的坐标位置
    private String distance;     //距离用户的距离，单位米
    private String logo;   // url链接地址
    private Promotion promotion;
    private String create_time;  //unix timestamp", //商户添加时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public class Promotion implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;   // 新用户首单直减50元
        private String icon;   // http://www.9maibei.com/Public/Upload/jian.jpg

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
