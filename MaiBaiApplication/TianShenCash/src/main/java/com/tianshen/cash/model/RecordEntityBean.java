package com.tianshen.cash.model;

public class RecordEntityBean {
    private String contact_name;// 姓名
    private String phone_number;// 电话
    private String in_out;// 类型 1是接听 3是未接听 2是拨打（需要验证）
    private String call_time; // 通话记录时间
    private String duration_time;// 通话时间

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getIn_out() {
        return in_out;
    }

    public void setIn_out(String in_out) {
        this.in_out = in_out;
    }

    public String getCall_time() {
        return call_time;
    }

    public void setCall_time(String call_time) {
        this.call_time = call_time;
    }

    public String getDuration_time() {
        return duration_time;
    }

    public void setDuration_time(String duration_time) {
        this.duration_time = duration_time;
    }
}
