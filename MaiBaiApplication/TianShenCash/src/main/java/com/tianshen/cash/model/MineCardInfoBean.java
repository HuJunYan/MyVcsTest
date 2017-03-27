package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by sbyh on 16/7/5.
 */

public class MineCardInfoBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String card_user_name;  // 持卡人姓名
    private String card_num;  // 银行卡号
    private String reserved_mobile;  // 预留手机号

    public String getCard_user_name() {
        return card_user_name;
    }

    public void setCard_user_name(String card_user_name) {
        this.card_user_name = card_user_name;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getReserved_mobile() {
        return reserved_mobile;
    }

    public void setReserved_mobile(String reserved_mobile) {
        this.reserved_mobile = reserved_mobile;
    }
}
