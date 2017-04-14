package com.tianshen.cash.model;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 存储当前登录的用户
 */

@Table("user")
public class User {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private long id; //本地表里面的ID

    private String customer_id; //天神用户ID
    private String name;
    private String age;
    private String phone;
    private String sex;
    private String token;
    private String jpush_id;//激光推送的id
    private String id_num; //身份证号
    private String repay_id; //选择产品的id (借款)
    private String consume_amount; //用户借款的金额(单位分)(借款)
    private String is_payway; //是否是自营的产品，自己的产品为0
    private String location;//坐标
    private String province; //省
    private String city;//城市
    private String country;//区域
    private String address;//详细地址
    private boolean isClickedHomeGetMoneyButton;//用户是否点过放款确认按钮
    private boolean isClickedHomeRePayMoneyButton;//用户是否点过还款确认按钮

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public boolean isClickedHomeGetMoneyButton() {
        return isClickedHomeGetMoneyButton;
    }

    public void setClickedHomeGetMoneyButton(boolean clickedHomeGetMoneyButton) {
        isClickedHomeGetMoneyButton = clickedHomeGetMoneyButton;
    }

    public boolean isClickedHomeRePayMoneyButton() {
        return isClickedHomeRePayMoneyButton;
    }

    public void setClickedHomeRePayMoneyButton(boolean clickedHomeRePayMoneyButton) {
        isClickedHomeRePayMoneyButton = clickedHomeRePayMoneyButton;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getIs_payway() {
        return is_payway;
    }

    public void setIs_payway(String is_payway) {
        this.is_payway = is_payway;
    }

    public String getRepay_id() {
        return repay_id;
    }

    public void setRepay_id(String repay_id) {
        this.repay_id = repay_id;
    }

    public String getConsume_amount() {
        return consume_amount;
    }

    public void setConsume_amount(String consume_amount) {
        this.consume_amount = consume_amount;
    }

    public String getId_num() {
        return id_num;
    }

    public void setId_num(String id_num) {
        this.id_num = id_num;
    }

    public String getJpush_id() {
        return jpush_id;
    }

    public void setJpush_id(String jpush_id) {
        this.jpush_id = jpush_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}