package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/8.
 */

public class CalculateInstallmentItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;//分期期数的id
    private String repay_times;  // 分期期数
    private String repay_date;  // 还款日期 2016-09-10
    private String repay_total;  // 每期应还金额,单位分
    private String pay_principal;  // 每期还款本金,单位分
    private String pay_interest;  // 每期还款利息, 单位分
    private boolean isChecked=false;//是否选中

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getRepay_times() {
        return repay_times;
    }

    public void setRepay_times(String repay_times) {
        this.repay_times = repay_times;
    }

    public String getRepay_date() {
        return repay_date;
    }

    public void setRepay_date(String repay_date) {
        this.repay_date = repay_date;
    }

    public String getRepay_total() {
        return repay_total;
    }

    public void setRepay_total(String repay_total) {
        this.repay_total = repay_total;
    }

    public String getPay_principal() {
        return pay_principal;
    }

    public void setPay_principal(String pay_principal) {
        this.pay_principal = pay_principal;
    }

    public String getPay_interest() {
        return pay_interest;
    }

    public void setPay_interest(String pay_interest) {
        this.pay_interest = pay_interest;
    }
}
