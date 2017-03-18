package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 14658 on 2016/7/12.
 */
public class ActiveQuotaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    public ActiveQuotaBean() {
        data = new Data();
    }

    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data; // 数据内容

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
        private String amount; // 激活额度
        private String balance_amount; // 剩余额度

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBalance_amount() {
            return balance_amount;
        }

        public void setBalance_amount(String balance_amount) {
            this.balance_amount = balance_amount;
        }
    }
}
