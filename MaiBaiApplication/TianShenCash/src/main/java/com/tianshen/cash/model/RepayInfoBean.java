package com.tianshen.cash.model;

import java.util.ArrayList;

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
        private String is_payway;
        public MoneyDetail money_detail;
        public ArrayList<CompositeDetail> composite_detail;
        public RepayMentStyle repayment_style;

        public String getIs_payway() {
            return is_payway;
        }

        public void setIs_payway(String is_payway) {
            this.is_payway = is_payway;
        }

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

    public class MoneyDetail {
        public String consume_amount_str; //还款总额
        public String consume_capital_amount_str; //应还本金
        public String consume_interest_amount_str; //代收利息
    }

    public class CompositeDetail {
        public String composite_amount_title;
        public String composite_amount_str;
    }

    public class RepayMentStyle {
        public ArrayList<BankList> bank_list;
        public AliPay alipay;
    }

    public class BankList {
        public String bank_name;
        public String bank_card_num;
        public String bank_gate_id;
    }

    public class AliPay {
        public String title;
        public String description;
        public String alipay_url;
    }
}
