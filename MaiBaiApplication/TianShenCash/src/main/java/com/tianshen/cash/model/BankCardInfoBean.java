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
        private String reserved_mobile;

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

    }


}
