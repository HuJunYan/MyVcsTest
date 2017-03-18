package com.maibai.user.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenrongshang on 16/10/15.
 */
public class ConsumeDataBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private String consume_id; // 消费id
    private String repay_date; // 还款日期
    private String amount;//还款金额
    private String overdue_amount;//逾期金额
    private String type;//消费方式
    private List<InstallmentHistoryBean> installment_history;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<InstallmentHistoryBean> getInstallment_history() {
        return installment_history;
    }

    public void setInstallment_history(List<InstallmentHistoryBean> installment_history) {
        this.installment_history = installment_history;
    }

    public String getOverdue_amount() {
        return overdue_amount;
    }

    public void setOverdue_amount(String overdue_amount) {
        this.overdue_amount = overdue_amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getConsume_id() {
        return consume_id;
    }

    public void setConsume_id(String consume_id) {
        this.consume_id = consume_id;
    }

    public String getRepay_date() {
        return repay_date;
    }

    public void setRepay_date(String repay_date) {
        this.repay_date = repay_date;
    }

}
