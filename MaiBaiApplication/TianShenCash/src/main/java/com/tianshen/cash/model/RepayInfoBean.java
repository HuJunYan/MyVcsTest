package com.tianshen.cash.model;

public class RepayInfoBean {

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

        private String consume_amount;
        private String overdue_amount;
        private String bank_name;
        private String bank_card_num;
        private String consume_id;
        private String id;
        private String repay_date;

        public String getConsume_amount() {
            return consume_amount;
        }

        public void setConsume_amount(String consume_amount) {
            this.consume_amount = consume_amount;
        }

        public String getOverdue_amount() {
            return overdue_amount;
        }

        public void setOverdue_amount(String overdue_amount) {
            this.overdue_amount = overdue_amount;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getBank_card_num() {
            return bank_card_num;
        }

        public void setBank_card_num(String bank_card_num) {
            this.bank_card_num = bank_card_num;
        }

        public String getConsume_id() {
            return consume_id;
        }

        public void setConsume_id(String consume_id) {
            this.consume_id = consume_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRepay_date() {
            return repay_date;
        }

        public void setRepay_date(String repay_date) {
            this.repay_date = repay_date;
        }

    }

}
