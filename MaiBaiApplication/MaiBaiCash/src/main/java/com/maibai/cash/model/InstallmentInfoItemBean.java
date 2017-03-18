package com.maibai.cash.model;

import java.io.Serializable;

/**
 * Created by m on 16-9-27.
 */
public class InstallmentInfoItemBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private String repay_times; // 分期期数
    private String repay_total; // 每期还款数，单位分
    private boolean isFocus=false;//是否选中
    private String id; // 分期id值

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    public String getRepay_times() {
        return repay_times;
    }

    public void setRepay_times(String repay_times) {
        this.repay_times = repay_times;
    }

    public String getRepay_total() {
        return repay_total;
    }

    public void setRepay_total(String repay_total) {
        this.repay_total = repay_total;
    }
}
