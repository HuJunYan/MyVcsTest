package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/3.
 */
public class WithdrawHistoryItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id; //提现项ID
    private String amount; //提现金额，单位：分
    private String withdraw_time; //提现时间，格式化字符串，直接展示

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWithdraw_time() {
        return withdraw_time;
    }

    public void setWithdraw_time(String withdraw_time) {
        this.withdraw_time = withdraw_time;
    }
}
