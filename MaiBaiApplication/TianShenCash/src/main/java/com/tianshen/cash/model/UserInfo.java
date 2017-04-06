package com.tianshen.cash.model;

/**
 * Created by cuiyue on 2017/4/1.
 */

public class UserInfo {

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
        private String user_address;
        private String company_name;
        private String company_phone;
        private String company_address;

        public String getQq_num() {
            return qq_num;
        }

        public void setQq_num(String qq_num) {
            this.qq_num = qq_num;
        }

        public String getUser_address() {
            return user_address;
        }

        public void setUser_address(String user_address) {
            this.user_address = user_address;
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

        public String getCompany_address() {
            return company_address;
        }

        public void setCompany_address(String company_address) {
            this.company_address = company_address;
        }

    }

}
