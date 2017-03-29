package com.maibai.cash.model;


public class UserAuthCenterBean {

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

        private int id_num;//身份证认证0:没有认证；1:已经认证”,
        private int userdetail_pass;//用户详情认证
        private int contacts_pass;//联系人认证
        private int bankcard_pass;//银行认证
        private int china_mobile;//运营商认证
        private int china_mobile_url;//运营商认证地址,如果is_china_mobile_credit＝1，返回空”

        public int getId_num() {
            return id_num;
        }

        public void setId_num(int id_num) {
            this.id_num = id_num;
        }

        public int getUserdetail_pass() {
            return userdetail_pass;
        }

        public void setUserdetail_pass(int userdetail_pass) {
            this.userdetail_pass = userdetail_pass;
        }

        public int getContacts_pass() {
            return contacts_pass;
        }

        public void setContacts_pass(int contacts_pass) {
            this.contacts_pass = contacts_pass;
        }

        public int getBankcard_pass() {
            return bankcard_pass;
        }

        public void setBankcard_pass(int bankcard_pass) {
            this.bankcard_pass = bankcard_pass;
        }

        public int getChina_mobile() {
            return china_mobile;
        }

        public void setChina_mobile(int china_mobile) {
            this.china_mobile = china_mobile;
        }

        public int getChina_mobile_url() {
            return china_mobile_url;
        }

        public void setChina_mobile_url(int china_mobile_url) {
            this.china_mobile_url = china_mobile_url;
        }

    }
}
