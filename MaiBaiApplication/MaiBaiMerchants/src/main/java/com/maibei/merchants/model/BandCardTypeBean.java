package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/2.
 */
public class BandCardTypeBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public BandCardTypeBean() {
        data = new Data();
    }
    private int code; // 返回码
    private String msg; //返回消息
    private Data data;// 数据内容

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private static final long serialVersionUID = 1L;
        private String bank_card_type;//银行卡类型：招商银行，工商银行

        public String getBank_card_type() {
            return bank_card_type;
        }

        public void setBank_card_type(String bank_card_type) {
            this.bank_card_type = bank_card_type;
        }
    }
}