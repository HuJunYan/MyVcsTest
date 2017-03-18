package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by chenrongshang on 16/7/4.
 */
public class WithDrawBean implements Serializable {
    private static final long serialVersionUID = 1L;
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
        private String balance;//商户余额(分)

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }
    }
}
