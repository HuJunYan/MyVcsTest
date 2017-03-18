package com.maibai.cash.model;

import java.io.Serializable;

/**
 * Created by m on 16-10-18.
 */
public class WithdrawalsOrderBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public WithdrawalsOrderBean() {
        data = new Data();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
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

    public class Data implements Serializable{
        private static final long serialVersionUID = 1L;
        private String amount; // 借款金额，单位分
        private String transfer_amount; // 到帐金额，单位分
        private String consume_time; // 借款时间 2016-07-10 08:08:08
        private String transfer_time; // 到账时长（文字描述）
        private String card_num; // 银行卡号
        private String bank_name; // 银行名称
        private String repay_times; // 借款期次
        private String repay_amount; // 每月应还：单位分
        private String timer;//借款时长
        private String repay_unit;//借款单位


        public String getTimer() {
            return timer;
        }

        public void setTimer(String timer) {
            this.timer = timer;
        }

        public String getRepay_unit() {
            return repay_unit;
        }

        public void setRepay_unit(String repay_unit) {
            this.repay_unit = repay_unit;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTransfer_amount() {
            return transfer_amount;
        }

        public void setTransfer_amount(String transfer_amount) {
            this.transfer_amount = transfer_amount;
        }

        public String getConsume_time() {
            return consume_time;
        }

        public void setConsume_time(String consume_time) {
            this.consume_time = consume_time;
        }

        public String getTransfer_time() {
            return transfer_time;
        }

        public void setTransfer_time(String transfer_time) {
            this.transfer_time = transfer_time;
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

        public String getRepay_times() {
            return repay_times;
        }

        public void setRepay_times(String repay_times) {
            this.repay_times = repay_times;
        }

        public String getRepay_amount() {
            return repay_amount;
        }

        public void setRepay_amount(String repay_amount) {
            this.repay_amount = repay_amount;
        }
    }
}
