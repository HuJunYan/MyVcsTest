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

        public List<CashSubItemBean> getCash_data() {
            return cash_data;
        }

        public void setCash_data(List<CashSubItemBean> cash_data) {
            this.cash_data = cash_data;
        }

    }
}
