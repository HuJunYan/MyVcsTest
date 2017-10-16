package com.tianshen.cash.model;

public class BankCardInfoBean {

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

        private String card_user_name;
        private String bank_name;
        private String card_num;

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getProvince_code() {
            return province_code;
        }

        public void setProvince_code(String province_code) {
            this.province_code = province_code;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        private String reserved_mobile;
        private String bank_id;
        private String city_code;
        private String city_name;
        private String province_code;
        private String province_name;
        private String identity_code;

        public String getBank_id() {
            return bank_id;
        }

        public void setBank_id(String bank_id) {
            this.bank_id = bank_id;
        }

        public String getCard_user_name() {
            return card_user_name;
        }

        public void setCard_user_name(String card_user_name) {
            this.card_user_name = card_user_name;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getCard_num() {
            return card_num;
        }

        public void setCard_num(String card_num) {
            this.card_num = card_num;
        }

        public String getReserved_mobile() {
            return reserved_mobile;
        }

        public void setReserved_mobile(String reserved_mobile) {
            this.reserved_mobile = reserved_mobile;
        }

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public String getIdentity_code() {
            return identity_code;
        }

        public void setIdentity_code(String identity_code) {
            this.identity_code = identity_code;
        }
    }


}
