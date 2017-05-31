package com.tianshen.cash.model;

import java.util.ArrayList;

public class ConstantBean {

    private int code;
    private String msg;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        private String qq_num;
        private String user_address_provice;
        private String user_address_city;
        private String user_address_county;
        private String user_address_detail;
        private String company_name;
        private String company_phone;
        private String company_address_provice;
        private String company_address_city;
        private String company_address_county;
        private String company_address_detail;

//        private ArrayList<String> marital_status_conf;  婚姻状态选项
//        private ArrayList<String> educational_background_conf; 学历选项
//        private ArrayList<String> income_per_month_conf; 收入选项
//        private ArrayList<String> occupational_identity_conf; 职业身份选项
//        private String position; 职位
//        private String marital_status; 婚姻状态
//        private String educational; 学历
//        private String income_per_month; 收入
//        private String occupational_identity; 职业身份

        public String getQq_num() {
            return qq_num;
        }

        public void setQq_num(String qq_num) {
            this.qq_num = qq_num;
        }

        public String getUser_address_provice() {
            return user_address_provice;
        }

        public void setUser_address_provice(String user_address_provice) {
            this.user_address_provice = user_address_provice;
        }

        public String getUser_address_city() {
            return user_address_city;
        }

        public void setUser_address_city(String user_address_city) {
            this.user_address_city = user_address_city;
        }

        public String getUser_address_county() {
            return user_address_county;
        }

        public void setUser_address_county(String user_address_county) {
            this.user_address_county = user_address_county;
        }

        public String getUser_address_detail() {
            return user_address_detail;
        }

        public void setUser_address_detail(String user_address_detail) {
            this.user_address_detail = user_address_detail;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCompany_phone() {
            return company_phone;
        }

        public void setCompany_phone(String company_phone) {
            this.company_phone = company_phone;
        }

        public String getCompany_address_provice() {
            return company_address_provice;
        }

        public void setCompany_address_provice(String company_address_provice) {
            this.company_address_provice = company_address_provice;
        }

        public String getCompany_address_city() {
            return company_address_city;
        }

        public void setCompany_address_city(String company_address_city) {
            this.company_address_city = company_address_city;
        }

        public String getCompany_address_county() {
            return company_address_county;
        }

        public void setCompany_address_county(String company_address_county) {
            this.company_address_county = company_address_county;
        }

        public String getCompany_address_detail() {
            return company_address_detail;
        }

        public void setCompany_address_detail(String company_address_detail) {
            this.company_address_detail = company_address_detail;
        }

    }
}
