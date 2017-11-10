package com.tianshen.cash.model;

import java.io.Serializable;
import java.util.List;

public class OtherLoanBean implements Serializable {
    private int code;
    private String msg;
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

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


    public class Data {

        private String max_cash;
        private String min_cash;
        private String unit;
        private String bank_card_info_str;
        private String repay_times;
        private String repay_id;
        private String is_in_three_day;
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
        private String bank_credit_investigation_url;


        private List<CashSubItemBean> cash_data;

        public String getMax_cash() {
            return max_cash;
        }

        public void setMax_cash(String max_cash) {
            this.max_cash = max_cash;
        }

        public String getMin_cash() {
            return min_cash;
        }

        public void setMin_cash(String min_cash) {
            this.min_cash = min_cash;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getBank_card_info_str() {
            return bank_card_info_str;
        }

        public void setBank_card_info_str(String bank_card_info_str) {
            this.bank_card_info_str = bank_card_info_str;
        }

        public String getRepay_times() {
            return repay_times;
        }

        public void setRepay_times(String repay_times) {
            this.repay_times = repay_times;
        }

        public String getRepay_id() {
            return repay_id;
        }

        public void setRepay_id(String repay_id) {
            this.repay_id = repay_id;
        }

        public String getIs_in_three_day() {
            return is_in_three_day;
        }

        public void setIs_in_three_day(String is_in_three_day) {
            this.is_in_three_day = is_in_three_day;
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

        public String getBank_credit_investigation_url() {
            return bank_credit_investigation_url;
        }

        public void setBank_credit_investigation_url(String bank_credit_investigation_url) {
            this.bank_credit_investigation_url = bank_credit_investigation_url;
        }

        public List<CashSubItemBean> getCash_data() {
            return cash_data;
        }

        public void setCash_data(List<CashSubItemBean> cash_data) {
            this.cash_data = cash_data;
        }

    }
}
