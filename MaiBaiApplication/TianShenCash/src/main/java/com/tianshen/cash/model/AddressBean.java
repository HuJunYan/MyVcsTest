package com.tianshen.cash.model;

import java.util.ArrayList;

/**
 *
 */

public class AddressBean {

    private int code;  // 返回码
    private String msg;  // 消息内容
    private ArrayList<Data> data;


    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
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

    public class Data {

        private String provice_name;
        private String provice_id;

        private String city_id;
        private String city_name;

        private String county_id;
        private String county_name;

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


        public String getProvice_id() {
            return provice_id;
        }

        public void setProvice_id(String provice_id) {
            this.provice_id = provice_id;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getProvice_name() {
            return provice_name;
        }

        public void setProvice_name(String provice_name) {
            this.provice_name = provice_name;
        }


        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

    }
}
