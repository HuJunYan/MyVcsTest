package com.maibai.cash.model;

import java.io.Serializable;

/**
 * Created by chunpengguo on 2017/1/14.
 */

public class StatisticsRollDataBean implements Serializable {
    private String mobile;
    private String money;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
