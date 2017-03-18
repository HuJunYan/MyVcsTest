package com.maibai.cash.model;

/**
 * Created by zhangchi on 2016/6/24.
 */
public class BillDetailItemBean {
    public String name;
    public int type;
    public int total_money;
    public int repay_money;
    public String repay_date;
    public String consume_date;

    public BillDetailItemBean(String name, int type, int total_money, int repay_money, String repay_date, String consume_date) {
        this.name = name;
        this.type = type;
        this.total_money = total_money;
        this.repay_money = repay_money;
        this.repay_date = repay_date;
        this.consume_date = consume_date;
    }
}
