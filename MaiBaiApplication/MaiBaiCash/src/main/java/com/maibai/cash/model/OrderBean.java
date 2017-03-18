package com.maibai.cash.model;


import java.io.Serializable;

/**
 * Created by sbyh on 16/7/4.
 */

public class OrderBean implements Serializable {
    private static final long serialVersionUID = 1L;

    public OrderBean() {
        data = new Data();
    }

    public static final int SUCCESS = 0; //成功
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data; // 商户id

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

    public class Data implements Serializable {
        private int is_need_verify;  //审核状态，1需要审核，0不需要审核
        private String is_need_fee; // 首付状态，1需要首付，0不需要首付
        private String amount;  // 消费金额,单位分
        private String repay_type;  // 分期类型1:次月还款,2:分期还款
        private String repay_date;  // 还款日期 日
        private String repay_times;  // 分期次数
        private String repay_principal;  // 每期还款本金,单位分
        private String repay_interest;  // 每期还款利息,单位分",
        private String repay_total;  //每期还款总数,单位分"
        private String credit;  //当前用户的授信额度，单位分
        private String balance_amount;  //可用授信额度,单位分
        private String max_amount;  //最高可用额度,单位分
        private String discount; //优惠金额：单位分
        private String real_pay;//实付款
        private String consume_id; // 消费id
        private String down_payment; // 首付款
        private String bind_card; // 是否绑定银行卡：0-未绑定 1-已绑定
        private String card_num; // 银行卡号
        private String bank_name; // 银行名称

        public String getReal_pay() {
            if (real_pay == null || "".equals(real_pay)) {
                real_pay = "0";
            }
            return real_pay;
        }

        public void setReal_pay(String real_pay) {
            this.real_pay = real_pay;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRepay_type() {
            return repay_type;
        }

        public void setRepay_type(String repay_type) {
            this.repay_type = repay_type;
        }

        public String getRepay_date() {
            return repay_date;
        }

        public void setRepay_date(String repay_date) {
            this.repay_date = repay_date;
        }

        public String getRepay_times() {
            return repay_times;
        }

        public void setRepay_times(String repay_times) {
            this.repay_times = repay_times;
        }

        public String getRepay_principal() {
            return repay_principal;
        }

        public void setRepay_principal(String repay_principal) {
            this.repay_principal = repay_principal;
        }

        public String getRepay_interest() {
            return repay_interest;
        }

        public void setRepay_interest(String repay_interest) {
            this.repay_interest = repay_interest;
        }

        public String getRepay_total() {
            return repay_total;
        }

        public void setRepay_total(String repay_total) {
            this.repay_total = repay_total;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getBalance_amount() {
            return balance_amount;
        }

        public void setBalance_amount(String balance_amount) {
            this.balance_amount = balance_amount;
        }

        public String getMax_amount() {
            return max_amount;
        }

        public void setMax_amount(String max_amount) {
            this.max_amount = max_amount;
        }

        public int getIs_need_verify() {
            return is_need_verify;
        }

        public void setIs_need_verify(int is_need_verify) {
            this.is_need_verify = is_need_verify;
        }

        public String getDiscount() {
            if (discount == null || "".equals(discount)) {
                discount = "0";
            }
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getIs_need_fee() {
            return is_need_fee;
        }

        public void setIs_need_fee(String is_need_fee) {
            this.is_need_fee = is_need_fee;
        }

        public String getConsume_id() {
            return consume_id;
        }

        public void setConsume_id(String consume_id) {
            this.consume_id = consume_id;
        }

        public String getDown_payment() {
            return down_payment;
        }

        public void setDown_payment(String down_payment) {
            this.down_payment = down_payment;
        }

        public String getBind_card() {
            return bind_card;
        }

        public void setBind_card(String bind_card) {
            this.bind_card = bind_card;
        }

        public String getCard_num() {
            return card_num;
        }

        public void setCard_num(String card_num) {
            this.card_num = card_num;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }
    }

}
