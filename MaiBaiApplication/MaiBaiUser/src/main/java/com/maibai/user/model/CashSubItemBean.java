package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by m on 16-10-17.
 */
public class CashSubItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String withdrawal_amount; // 提现金额单位分
    private String repay_total; // 每期还款数，单位分
    private String transfer_amount;//到账金额,单位分

    public String getTransfer_amount() {
        return transfer_amount;
    }

    public void setTransfer_amount(String transfer_amount) {
        this.transfer_amount = transfer_amount;
    }

    public String getWithdrawal_amount() {
        return withdrawal_amount;
    }

    public void setWithdrawal_amount(String withdrawal_amount) {
        this.withdrawal_amount = withdrawal_amount;
    }

    public String getRepay_total() {
        return repay_total;
    }

    public void setRepay_total(String repay_total) {
        this.repay_total = repay_total;
    }
}
