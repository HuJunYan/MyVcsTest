package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by m on 16-9-14.
 */
public class ConsumeItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String consume_id; // 消费ID1
    private String merchant_name; // 商户名称
    private String merchant_logo; // 商户图标
    private String repay_type; // 还款类型1:次月还款,2:分期付款
    private String consume_date; // 消费日期：2016-08-01 18:22
    private String real_pay; // 实际付款：单位分
    private String show_amount="0";//展示金额
    private String discount_first_order;//首单减免
    private String discount_full_reduce;//商户减免
    private String discount_rebate_amount;//商户折扣减免
    private String status;//当前状态
    private String down_payment;//首付金额
    private String bind_card;//是否绑定银行卡：0-未绑定 1-已绑定
    private String card_num;//银行卡号
    private String bank_name;//银行名称

    public String getBind_card() {
        return bind_card;
    }

    public void setBind_card(String bind_card) {
        this.bind_card = bind_card;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getDown_payment() {
        return down_payment;
    }

    public void setDown_payment(String down_payment) {
        this.down_payment = down_payment;
    }

    public String getShow_amount() {
        return show_amount;
    }

    public void setShow_amount(String show_amount) {
        this.show_amount = show_amount;
    }

    public String getDiscount_first_order() {
        return discount_first_order;
    }

    public void setDiscount_first_order(String discount_first_order) {
        this.discount_first_order = discount_first_order;
    }

    public String getDiscount_full_reduce() {
        return discount_full_reduce;
    }

    public void setDiscount_full_reduce(String discount_full_reduce) {
        this.discount_full_reduce = discount_full_reduce;
    }

    public String getDiscount_rebate_amount() {
        return discount_rebate_amount;
    }

    public void setDiscount_rebate_amount(String discount_rebate_amount) {
        this.discount_rebate_amount = discount_rebate_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
