package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by m on 16-11-21.
 */
public class MerchantCommisionBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data;
    public class Data implements Serializable {
        private static final long serialVersionUID = 1L;
        private String commision_balance;//佣金余额

        public String getCommision_balance() {
            return commision_balance;
        }

        public void setCommision_balance(String commision_balance) {
            this.commision_balance = commision_balance;
        }
    }

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
}
