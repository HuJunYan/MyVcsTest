package com.tianshen.cash.model;

import java.util.ArrayList;

/**
 * Created by cuiyue on 2017/4/1.
 */

public class ExtroContactsBean {

    private int code;
    private String msg;
    private ArrayList<Data> data;

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

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data {

        private String relavtion_type; //关系类型，1: 父母，2: 配偶，3: 直亲，4: 朋友，5: 同事”
        private String contact_name;
        private String contact_phone;

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public String getRelavtion_type() {
            return relavtion_type;
        }

        public void setRelavtion_type(String relavtion_type) {
            this.relavtion_type = relavtion_type;
        }

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

    }

}
