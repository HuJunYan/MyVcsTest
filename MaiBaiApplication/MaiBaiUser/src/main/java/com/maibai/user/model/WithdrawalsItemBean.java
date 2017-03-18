package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-10-17.
 */
public class WithdrawalsItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public WithdrawalsItemBean() {
        cash_data = new ArrayList<CashSubItemBean>();
    }
    private String repay_times; // 分期期数:3
    private String id; // 分期id值
    private List<CashSubItemBean> cash_data;
    private boolean isCheck=false;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getRepay_times() {
        return repay_times;
    }

    public void setRepay_times(String repay_times) {
        this.repay_times = repay_times;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CashSubItemBean> getCash_data() {
        return cash_data;
    }

    public void setCash_data(List<CashSubItemBean> cash_data) {
        this.cash_data = cash_data;
    }
}
