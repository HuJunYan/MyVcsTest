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

        public List<CashSubItemBean> getCash_data() {
            return cash_data;
        }

        public void setCash_data(List<CashSubItemBean> cash_data) {
            this.cash_data = cash_data;
        }

    }
}
