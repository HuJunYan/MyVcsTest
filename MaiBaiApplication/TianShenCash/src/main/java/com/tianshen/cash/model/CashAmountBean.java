package com.tianshen.cash.model;

public class CashAmountBean {

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

        private String cash_amount;
        private String cash_amount_status;
        private String joke_url;

        public String getCash_amount() {
            return cash_amount;
        }

        public void setCash_amount(String cash_amount) {
            this.cash_amount = cash_amount;
        }

        public String getCash_amount_status() {
            return cash_amount_status;
        }

        public void setCash_amount_status(String cash_amount_status) {
            this.cash_amount_status = cash_amount_status;
        }

        public String getJoke_url() {
            return joke_url;
        }

        public void setJoke_url(String joke_url) {
            this.joke_url = joke_url;
        }

    }
}