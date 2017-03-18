package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by m on 16-11-18.
 */
public class CommisionStagesItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String time; // 该笔总佣金
    private String money; // 100.0元（单位为元）
    private String status; // 0总佣金1已经到账2待到账3逾期

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
