package com.tianshen.cash.model;

/**
 * Created by Administrator on 2016/8/11.
 */
public class SMSMessageBean {
    private String address;//发送人号码
    private String person;//联系人姓名列表
    private String date;//接收时间
    private String body;//短信内容
    private int type;//短信类型 1为接收 2为发送

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
