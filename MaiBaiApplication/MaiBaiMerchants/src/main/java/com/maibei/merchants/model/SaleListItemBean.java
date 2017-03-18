package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by chenrongshang on 16/7/3.
 */
public class SaleListItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String customer_name; // 用户名称
    private String consume_time; // 消费时间，unix 时间戳,单位秒
    private String consume_amount; // 消费金额,单位分

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getConsume_time() {
        return consume_time;
    }

    public void setConsume_time(String consume_time) {
        this.consume_time = consume_time;
    }

    public String getConsume_amount() {
        return consume_amount;
    }

    public void setConsume_amount(String consume_amount) {
        this.consume_amount = consume_amount;
    }
}
