package com.maibai.cash.model;

import java.util.ArrayList;

/**
 * Created by cuiyue on 2017/4/1.
 */

public class PostExtroContactsBean {

    private String customer_id;
    private ArrayList<Data> data;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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
        private String contact_mobile;

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

        public String getContact_mobile() {
            return contact_mobile;
        }

        public void setContact_mobile(String contact_mobile) {
            this.contact_mobile = contact_mobile;
        }

    }
}
