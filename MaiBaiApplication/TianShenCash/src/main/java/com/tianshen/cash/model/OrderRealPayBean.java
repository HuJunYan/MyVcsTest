package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by m on 16-9-18.
 */
public class OrderRealPayBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public OrderRealPayBean() {
        data = new Data();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data; // 订单数据

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

    public class Data implements Serializable{
        private static final long serialVersionUID = 1L;
        private String state; // 下单状态，1：成功状态，2：等待状态，服务器当前无法判断是否成功，需要过段时间重新查询
        private String amount;  //  消费金额,单位分
        private String repay_type;  //  分期类型1:次月还款,2:分期还款
        private String repay_date;  //  还款日期 2016-07-10
        private String repay_times;  //  分期次数
        private String repay_principal;  //  每期还款本金,单位分
        private String repay_interest;  //  每期还款利息,单位分
        private String repay_total;  //  每期还款总数,单位分
        private String credit;  //  当前用户的授信额度(customer表中的amount字段)，单位分
        private String balance_amount;  //  可用授信额度,单位分
        private String max_amount;  // 最高可用额度,单位分
        private String discount;  //  优惠金额：单位分
        private String real_pay;  //  实际付款金额：单位分
        private String down_payment;//首付金额

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDown_payment() {
            return down_payment;
        }

        public void setDown_payment(String down_payment) {
            this.down_payment = down_payment;
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

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getReal_pay() {
            return real_pay;
        }

        public void setReal_pay(String real_pay) {
            this.real_pay = real_pay;
        }
    }
}
