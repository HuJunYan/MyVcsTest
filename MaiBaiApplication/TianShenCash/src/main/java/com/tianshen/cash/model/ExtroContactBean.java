package com.tianshen.cash.model;


public class ExtroContactBean {

    private String type; //关系类型，1: 父母，2: 配偶，3: 直亲，4: 朋友，5: 同事”
    private String contact_name;
    private String contact_phone;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

}
