package com.tianshen.cash.model;

import java.util.ArrayList;

public class UserConfig {

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

        private String consume_id; //如果没有待完成的订单，取值为0
        private String virtual_card_num;//虚拟银行卡号
        private String virtual_card_type; // 0:绿卡，1:银卡，2:金卡，3:钻石卡”
        private String cur_credit_step;//当前的认证步数
        private String total_credit_step; //总的认证步数
        private String status;//0:新用户，没有提交过订单；1:订单待审核；2:审核通过待放款；3:放款成功（钱已经到银行卡）；4:审核失败；5:放款失败
        private String overdue_days;
        private String overdue_amount;//金额
        private String amount;//还款金额
        private String repayment_time_month;
        private String remaining_days;
        private String repayment_time_day;
        private ArrayList<Consume> consume_status_list;

        public String getOverdue_days() {
            return overdue_days;
        }

        public void setOverdue_days(String overdue_days) {
            this.overdue_days = overdue_days;
        }

        public String getOverdue_amount() {
            return overdue_amount;
        }

        public void setOverdue_amount(String overdue_amount) {
            this.overdue_amount = overdue_amount;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRepayment_time_month() {
            return repayment_time_month;
        }

        public void setRepayment_time_month(String repayment_time_month) {
            this.repayment_time_month = repayment_time_month;
        }

        public String getRemaining_days() {
            return remaining_days;
        }

        public void setRemaining_days(String remaining_days) {
            this.remaining_days = remaining_days;
        }

        public String getRepayment_time_day() {
            return repayment_time_day;
        }

        public void setRepayment_time_day(String repayment_time_day) {
            this.repayment_time_day = repayment_time_day;
        }

        public ArrayList<Consume> getConsume_status_list() {
            return consume_status_list;
        }

        public void setConsume_status_list(ArrayList<Consume> consume_status_list) {
            this.consume_status_list = consume_status_list;
        }

        public String getConsume_id() {
            return consume_id;
        }

        public void setConsume_id(String consume_id) {
            this.consume_id = consume_id;
        }

        public String getVirtual_card_num() {
            return virtual_card_num;
        }

        public void setVirtual_card_num(String virtual_card_num) {
            this.virtual_card_num = virtual_card_num;
        }

        public String getVirtual_card_type() {
            return virtual_card_type;
        }

        public void setVirtual_card_type(String virtual_card_type) {
            this.virtual_card_type = virtual_card_type;
        }

        public String getCur_credit_step() {
            return cur_credit_step;
        }

        public void setCur_credit_step(String cur_credit_step) {
            this.cur_credit_step = cur_credit_step;
        }

        public String getTotal_credit_step() {
            return total_credit_step;
        }

        public void setTotal_credit_step(String total_credit_step) {
            this.total_credit_step = total_credit_step;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        public class Consume {

            private String consume_status_title;
            private String consume_status_description;
            private String consume_status_time;

            public String getConsume_status_title() {
                return consume_status_title;
            }

            public void setConsume_status_title(String consume_status_title) {
                this.consume_status_title = consume_status_title;
            }

            public String getConsume_status_description() {
                return consume_status_description;
            }

            public void setConsume_status_description(String consume_status_description) {
                this.consume_status_description = consume_status_description;
            }

            public String getConsume_status_time() {
                return consume_status_time;
            }

            public void setConsume_status_time(String consume_status_time) {
                this.consume_status_time = consume_status_time;
            }

        }

    }


}
