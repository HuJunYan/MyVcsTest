package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/20.
 */

public class InstallmentHistoryBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id; // id
    private String repay_date; // 还款日期
    private String amount;//还款金额
    private String overdue_amount;//逾期金额

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepay_date() {
        return repay_date;
    }

    public void setRepay_date(String repay_date) {
        this.repay_date = repay_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOverdue_amount() {
        return overdue_amount;
    }

    public void setOverdue_amount(String overdue_amount) {
        this.overdue_amount = overdue_amount;
    }
}
