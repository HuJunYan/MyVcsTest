package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/6.
 */
public class BankListItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bank_id; // 银行ID编号
    private String bank_name; // 银行名称
    private String card_num; // 银行卡号

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }
}