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
        private String message_count; //右上角消息
        private String status;//0:新用户，没有提交过订单；1:订单待审核；2:审核通过待放款；3:放款成功（钱已经到银行卡）；4:审核失败；5:放款失败
        private String overdue_days;
        private String overdue_amount;//金额
        private String amount;//还款金额
        private String whethe_review_status;
        private String is_payway;
        private String consume_amount;
        private String repayment_time_year;
        private String repayment_time_month;
        private String repayment_time_day;
        private String remaining_days;
        private String max_cash;
        private String is_show_service_telephone;
        private String is_need_contacts;
        private ArrayList<Consume> consume_status_list;
        private String sjd_url;
        private String flow_supermarket_id;
        private String flow_supermarket_url;
        private String flow_supermarket_num;
        private String zzOrderMark;
        private int diff_rate; //"是不是差异化费率的订单，0：不是， 1：是（为1的时候，需要展示确认借款的页面）"
        private String is_show_cash_amount_dialog;

        public String getIs_show_cash_amount_dialog() {
            return is_show_cash_amount_dialog;
        }

        public void setIs_show_cash_amount_dialog(String is_show_cash_amount_dialog) {
            this.is_show_cash_amount_dialog = is_show_cash_amount_dialog;
        }


        public String getZzOrderMark() {
            return zzOrderMark;
        }

        public void setZzOrderMark(String zzOrderMark) {
            this.zzOrderMark = zzOrderMark;
        }

        public String getFlow_supermarket_id() {
            return flow_supermarket_id;
        }

        public void setFlow_supermarket_id(String flow_supermarket_id) {
            this.flow_supermarket_id = flow_supermarket_id;
        }

        public String getFlow_supermarket_url() {
            return flow_supermarket_url;
        }

        public void setFlow_supermarket_url(String flow_supermarket_url) {
            this.flow_supermarket_url = flow_supermarket_url;
        }

        public String getFlow_supermarket_num() {
            return flow_supermarket_num;
        }

        public void setFlow_supermarket_num(String flow_supermarket_num) {
            this.flow_supermarket_num = flow_supermarket_num;
        }

        public String getIs_need_contacts() {
            return is_need_contacts;
        }

        public void setIs_need_contacts(String is_need_contacts) {
            this.is_need_contacts = is_need_contacts;
        }

        public String getSjd_url() {
            return sjd_url;
        }

        public void setSjd_url(String sjd_url) {
            this.sjd_url = sjd_url;
        }

        public String getIs_show_service_telephone() {
            return is_show_service_telephone;
        }

        public void setIs_show_service_telephone(String is_show_service_telephone) {
            this.is_show_service_telephone = is_show_service_telephone;
        }

        public String getMax_cash() {
            return max_cash;
        }

        public void setMax_cash(String max_cash) {
            this.max_cash = max_cash;
        }

        public String getWhethe_review_status() {
            return whethe_review_status;
        }

        public void setWhethe_review_status(String whethe_review_status) {
            this.whethe_review_status = whethe_review_status;
        }

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

        public String getRepayment_time_year() {
            return repayment_time_year;
        }

        public void setRepayment_time_year(String repayment_time_year) {
            this.repayment_time_year = repayment_time_year;
        }

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

        public int getDiff_rate() {
            return diff_rate;
        }

        public void setDiff_rate(int diff_rate) {
            this.diff_rate = diff_rate;
        }

        public String getMessage_count() {
            return message_count;
        }

        public void setMessage_count(String message_count) {
            this.message_count = message_count;
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
