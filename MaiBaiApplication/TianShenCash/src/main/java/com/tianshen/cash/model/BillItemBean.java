package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by sbyh on 16/7/5.
 */

public class BillItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String consume_id;  // 账单ID1
    private String merchant_name;  // 商户名称
    private String merchant_logo;  // 商户图标
    private String repay_type;  // 还款类型1:次月还款,2:分期付款
    private String total_amount;  // 消费总金额,单位分
    private String repay_amount;  // 本月应还金额,单位分
    private String repay_date;  // 还款日期,时间戳
    private String consume_date;  // 消费日期, 时间戳
    private String real_pay;  // 实际付款：单位分
    private String discount; //优惠金额：单位分
    private String fine;  // 逾期金额
    private String total_times; // 分期总期数
    private String current_time; // 当前期数
    private String bill_id;

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

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

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getRepay_amount() {
        if(null==repay_amount||"".equals(repay_amount))
            repay_amount="0";
        return repay_amount;
    }

    public void setRepay_amount(String repay_amount) {
        this.repay_amount = repay_amount;
    }

    public String getRepay_date() {
        return repay_date;
    }

    public void setRepay_date(String repay_date) {
        this.repay_date = repay_date;
    }

    public String getConsume_date() {
        return consume_date;
    }

    public void setConsume_date(String consume_date) {
        this.consume_date = consume_date;
    }

    public String getReal_pay() {
        return real_pay;
    }

    public void setReal_pay(String real_pay) {
        this.real_pay = real_pay;
    }

    public String getFine() {
        if (fine == null || "".equals(fine)) {
            fine = "0";
        }
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getDiscount() {
        if (discount == null || "".equals(discount)) {
            discount = "0";
        }
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal_times() {
        return total_times;
    }

    public void setTotal_times(String total_times) {
        this.total_times = total_times;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }
}
