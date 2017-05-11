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

        private ArrayList<String> marital_status_conf;
        private ArrayList<String> educational_background_conf;
        private ArrayList<String> income_per_month_conf;
        private ArrayList<String> occupational_identity_conf;
        private String position;

        private String marital_status;
        private String educational;
        private String income_per_month;
        private String occupational_identity;

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

        public ArrayList<String> getMarital_status_conf() {
            return marital_status_conf;
        }

        public void setMarital_status_conf(ArrayList<String> marital_status_conf) {
            this.marital_status_conf = marital_status_conf;
        }

        public ArrayList<String> getEducational_background_conf() {
            return educational_background_conf;
        }

        public void setEducational_background_conf(ArrayList<String> educational_background_conf) {
            this.educational_background_conf = educational_background_conf;
        }

        public ArrayList<String> getIncome_per_month_conf() {
            return income_per_month_conf;
        }

        public void setIncome_per_month_conf(ArrayList<String> income_per_month_conf) {
            this.income_per_month_conf = income_per_month_conf;
        }

        public ArrayList<String> getOccupational_identity_conf() {
            return occupational_identity_conf;
        }

        public void setOccupational_identity_conf(ArrayList<String> occupational_identity_conf) {
            this.occupational_identity_conf = occupational_identity_conf;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getMarital_status() {
            return marital_status;
        }

        public void setMarital_status(String marital_status) {
            this.marital_status = marital_status;
        }

        public String getEducational() {
            return educational;
        }

        public void setEducational(String educational) {
            this.educational = educational;
        }

        public String getIncome_per_month() {
            return income_per_month;
        }

        public void setIncome_per_month(String income_per_month) {
            this.income_per_month = income_per_month;
        }

        public String getOccupational_identity() {
            return occupational_identity;
        }

        public void setOccupational_identity(String occupational_identity) {
            this.occupational_identity = occupational_identity;
        }

    }
}
