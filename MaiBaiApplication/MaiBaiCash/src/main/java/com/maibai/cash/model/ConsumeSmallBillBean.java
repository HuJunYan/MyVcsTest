package com.maibai.cash.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by m on 16-11-7.
 */
public class ConsumeSmallBillBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String consume_id; // 消费ID,外层大id
    private String merchant_name; // 商户名称
    private String merchant_logo; // 商户图标
    private String repay_type; // 还款类型1:次月还款,2:分期付款
    private String consume_date; // 消费日期：2016-08-01 18:22
    private String amount; // 消费总金额，单位分
    private String down_payment; // 首付金额，单位分
    private String borrow_amount; // 趣提钱代付金额，单位分
    private String repay_amount; // 带还总金额：单位分
    private String have_repay; // 已还金额，单位分
    private String total_times;//总期数

    public String getTotal_times() {
        return total_times;
    }

    public void setTotal_times(String total_times) {
        this.total_times = total_times;
    }

    private List<SmallOrderItemBean> small_order_list; //小单列表数据

    public String getConsume_id() {
        return consume_id;
    }

    public void setConsume_id(String consume_id) {
        this.consume_id = consume_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_logo() {
        return merchant_logo;
    }

    public void setMerchant_logo(String merchant_logo) {
        this.merchant_logo = merchant_logo;
    }

    public String getRepay_type() {
        return repay_type;
    }

    public void setRepay_type(String repay_type) {
        this.repay_type = repay_type;
    }

    public String getConsume_date() {
        return consume_date;
    }

    public void setConsume_date(String consume_date) {
        this.consume_date = consume_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDown_payment() {
        return down_payment;
    }

    public void setDown_payment(String down_payment) {
        this.down_payment = down_payment;
    }

    public String getBorrow_amount() {
        return borrow_amount;
    }

    public void setBorrow_amount(String borrow_amount) {
        this.borrow_amount = borrow_amount;
    }

    public String getRepay_amount() {
        return repay_amount;
    }

    public void setRepay_amount(String repay_amount) {
        this.repay_amount = repay_amount;
    }

    public String getHave_repay() {
        return have_repay;
    }

    public void setHave_repay(String have_repay) {
        this.have_repay = have_repay;
    }

    public List<SmallOrderItemBean> getSmall_order_list() {
        return small_order_list;
    }

    public void setSmall_order_list(List<SmallOrderItemBean> small_order_list) {
        this.small_order_list = small_order_list;
    }
}
