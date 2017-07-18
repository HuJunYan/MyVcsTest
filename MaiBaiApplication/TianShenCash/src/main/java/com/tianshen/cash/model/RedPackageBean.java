package com.tianshen.cash.model;

import java.util.ArrayList;

public class RedPackageBean {

    private int code;
    private String msg;
    private ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
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

        private String withdrawals_money;
        private String min_withdrawals;
        private String max_withdrawals;
        private String all_income;
        private String already_withdrawals_money;
        private ArrayList<WithDrawalsListBean> withdrawals_list;

        public String getWithdrawals_money() {
            return withdrawals_money;
        }

        public void setWithdrawals_money(String withdrawals_money) {
            this.withdrawals_money = withdrawals_money;
        }

        public String getMin_withdrawals() {
            return min_withdrawals;
        }

        public void setMin_withdrawals(String min_withdrawals) {
            this.min_withdrawals = min_withdrawals;
        }

        public String getMax_withdrawals() {
            return max_withdrawals;
        }

        public void setMax_withdrawals(String max_withdrawals) {
            this.max_withdrawals = max_withdrawals;
        }

        public String getAll_income() {
            return all_income;
        }

        public void setAll_income(String all_income) {
            this.all_income = all_income;
        }

        public String getAlready_withdrawals_money() {
            return already_withdrawals_money;
        }

        public void setAlready_withdrawals_money(String already_withdrawals_money) {
            this.already_withdrawals_money = already_withdrawals_money;
        }

        public ArrayList<WithDrawalsListBean> getWithdrawals_list() {
            return withdrawals_list;
        }

        public void setWithdrawals_list(ArrayList<WithDrawalsListBean> withdrawals_list) {
            this.withdrawals_list = withdrawals_list;
        }

    }


}
