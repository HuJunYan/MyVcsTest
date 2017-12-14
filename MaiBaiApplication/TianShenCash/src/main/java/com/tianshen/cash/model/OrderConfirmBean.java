package com.tianshen.cash.model;

import java.util.List;

public class OrderConfirmBean {

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
        private String timer;
        private String poundage;
        private String amount;
        private String repayment_amout;
        private String bank_name;
        private String card_num;
        private String detail;
        private String repay_id;
        private String msg_last_time;
        private String call_last_time;
        private String interest;

        private String second_party;
        private String id_num;
        private String unit_address;
        private String common_address;
        private String phone;
        private String date_time;
        private String expire;
        private String days;
        private String total;
        private String protocol_num;
        private String overdue;
        private List<SpendListBean> spend_list;

        public String getBank_credit_investigation_url() {
            return bank_credit_investigation_url;
        }

        public void setBank_credit_investigation_url(String bank_credit_investigation_url) {
            this.bank_credit_investigation_url = bank_credit_investigation_url;
        }

        private String bank_credit_investigation_url;

        public String getConsume_amount() {
            return consume_amount;
        }

        public void setConsume_amount(String consume_amount) {
            this.consume_amount = consume_amount;
        }

        public String getTimer() {
            return timer;
        }

        public void setTimer(String timer) {
            this.timer = timer;
        }

        public String getPoundage() {
            return poundage;
        }

        public void setPoundage(String poundage) {
            this.poundage = poundage;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRepayment_amout() {
            return repayment_amout;
        }

        public void setRepayment_amout(String repayment_amout) {
            this.repayment_amout = repayment_amout;
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

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getRepay_id() {
            return repay_id;
        }

        public void setRepay_id(String repay_id) {
            this.repay_id = repay_id;
        }

        public String getSecond_party() {
            return second_party;
        }

        public void setSecond_party(String second_party) {
            this.second_party = second_party;
        }

        public String getId_num() {
            return id_num;
        }

        public void setId_num(String id_num) {
            this.id_num = id_num;
        }

        public String getUnit_address() {
            return unit_address;
        }

        public void setUnit_address(String unit_address) {
            this.unit_address = unit_address;
        }

        public String getCommon_address() {
            return common_address;
        }

        public void setCommon_address(String common_address) {
            this.common_address = common_address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDate_time() {
            return date_time;
        }

        public void setDate_time(String date_time) {
            this.date_time = date_time;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getProtocol_num() {
            return protocol_num;
        }

        public void setProtocol_num(String protocol_num) {
            this.protocol_num = protocol_num;
        }

        public String getOverdue() {
            return overdue;
        }

        public void setOverdue(String overdue) {
            this.overdue = overdue;
        }

        public String getMsg_last_time() {
            return msg_last_time;
        }

        public void setMsg_last_time(String msg_last_time) {
            this.msg_last_time = msg_last_time;
        }

        public String getCall_last_time() {
            return call_last_time;
        }

        public void setCall_last_time(String call_last_time) {
            this.call_last_time = call_last_time;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public List<SpendListBean> getSpend_list() {
            return spend_list;
        }

        public void setSpend_list(List<SpendListBean> spend_list) {
            this.spend_list = spend_list;
        }

        public  class SpendListBean {
            /**
             * spend_way : 旅游
             * spend_way_id : 1
             */

            private String spend_way;
            private String spend_way_id;

            public String getSpend_way() {
                return spend_way;
            }

            public void setSpend_way(String spend_way) {
                this.spend_way = spend_way;
            }

            public String getSpend_way_id() {
                return spend_way_id;
            }

            public void setSpend_way_id(String spend_way_id) {
                this.spend_way_id = spend_way_id;
            }
        }


    }




}
