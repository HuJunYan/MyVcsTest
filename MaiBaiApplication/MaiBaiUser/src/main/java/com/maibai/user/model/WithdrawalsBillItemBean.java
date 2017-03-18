package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by m on 16-10-14.
 */
public class WithdrawalsBillItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String consume_id; // 消费id，外层大id
    private String bill_id; // 提现账单id
    private String total_amount; // 总体现金额，单位：分
    private String repay_money; // 本期还款额，单位：分
    private String total_times; // 还款总期数
    private String current_times; // 当前期数
    private String late_fee;//逾期金额
    private String is_overdue; // 是否逾期，0：未逾期，1：已逾期
    private String remainder_repay_days; // 剩余的还款天数，注：is_overdue=0表示账单距离到期的天数，is_overdue=1表示账单已过期的天数
    private String repay_date; // 还款日期，eg: 2016-10-10
    private String repay_amount;//本期应还总额
    private boolean isChecked=false; // 是否被选中

    public String getRepay_amount() {
        return repay_amount;
    }

    public void setRepay_amount(String repay_amount) {
        this.repay_amount = repay_amount;
    }

    public String getConsume_id() {
        return consume_id;
    }

    public void setConsume_id(String consume_id) {
        this.consume_id = consume_id;
    }

    public String getLate_fee() {
        return late_fee;
    }

    public void setLate_fee(String late_fee) {
        this.late_fee = late_fee;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getRepay_money() {
        return repay_money;
    }

    public void setRepay_money(String repay_money) {
        this.repay_money = repay_money;
    }

    public String getTotal_times() {
        return total_times;
    }

    public void setTotal_times(String total_times) {
        this.total_times = total_times;
    }

    public String getCurrent_times() {
        return current_times;
    }

    public void setCurrent_times(String current_times) {
        this.current_times = current_times;
    }

    public String getIs_overdue() {
        return is_overdue;
    }

    public void setIs_overdue(String is_overdue) {
        this.is_overdue = is_overdue;
    }

    public String getRemainder_repay_days() {
        return remainder_repay_days;
    }

    public void setRemainder_repay_days(String remainder_repay_days) {
        this.remainder_repay_days = remainder_repay_days;
    }

    public String getRepay_date() {
        return repay_date;
    }

    public void setRepay_date(String repay_date) {
        this.repay_date = repay_date;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
