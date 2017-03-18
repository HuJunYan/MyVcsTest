package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by sbyh on 16/6/29.
 */

public class ConsumerHistoryItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int customer_id; //用户ID
    private int merchant_id; //商户ID
    private int type; //还款方式  1：次月还款 2：分期还款
    private double amount; //金额
    private String add_time; //添加时间

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
