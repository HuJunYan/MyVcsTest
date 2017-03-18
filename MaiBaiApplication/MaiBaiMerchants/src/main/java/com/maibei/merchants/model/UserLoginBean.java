package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by sbyh on 16/6/27.
 */

public class UserLoginBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public UserLoginBean() {
        data = new Data();
    }
    public static final int SUCCESS = 0; //成功
    private int code;  // 返回码
    private String msg;  // 消息内容

    public Data data;

    public class Data {
        private String id;  //商户id
        private String mobile;  //手机号
        private String balance;  // 余额,可提现金额
        private String name; // 商店名称
        private String category; // 商店分类
        private String province; // 商店所在省份
        private String city; // 商店所在城市
        private String country; // 商店所在城区
        private String address; // 商店所在详细地址
        private String coordinate; // 物理位置  纬度,经度
        private String lat; // 纬度
        private String lng; // 经度
        private String description; // 详细说明
        private String images; // 商店图片
        private String logo; // 商店头像
        private String owner_name; // 商店拥有者姓名
        private String operate_year; // 经营年限
        private String sales_per_month; // 销售额
        private String id_num; // 拥有者身份证
        private String status; // 商店状态 0未审核 1审核通过 2已删除 3待完善资料
        private String add_time; // 注册时间 年-月-日

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
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

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getOwner_name() {
            return owner_name;
        }

        public void setOwner_name(String owner_name) {
            this.owner_name = owner_name;
        }

        public String getOperate_year() {
            return operate_year;
        }

        public void setOperate_year(String operate_year) {
            this.operate_year = operate_year;
        }

        public String getSales_per_month() {
            return sales_per_month;
        }

        public void setSales_per_month(String sales_per_month) {
            this.sales_per_month = sales_per_month;
        }

        public String getId_num() {
            return id_num;
        }

        public void setId_num(String id_num) {
            this.id_num = id_num;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
