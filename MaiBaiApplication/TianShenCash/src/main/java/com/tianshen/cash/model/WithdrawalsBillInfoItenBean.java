package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by m on 16-10-19.
 */
public class WithdrawalsBillInfoItenBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id; // 账单id，内层小id
    private String principal; // 应还本金
    private String late_fee; // 逾期金额，没有逾期返回0
    private String time; // 还款时间：已经还款的格式：08-10已还，逾期的格式：逾期6天，未到期的格式：11-10待还"
    private String state; // 账单状态，0：已还，1：逾期，2：待还
    private String repay_date; // 还款日期，eg: 2016-10-10
    private String repay_amount;//本期应还总额


    public String getRepay_amount() {
        return repay_amount;
    }

    public void setRepay_amount(String repay_amount) {
        this.repay_amount = repay_amount;
    }

    private boolean isChecked = false; // 是否选中

    public String getRepay_date() {
        return repay_date;
    }

    public void setRepay_date(String repay_date) {
        this.repay_date = repay_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getLate_fee() {
        return late_fee;
    }

    public void setLate_fee(String late_fee) {
        this.late_fee = late_fee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
