package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by 14658 on 2016/7/28.
 */
public class OrderBean implements Serializable{

    private String time;
    private String amount;

    public OrderBean(String time, String amount) {
        this.time = time;
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
