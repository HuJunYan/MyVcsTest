package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by m on 16-10-18.
 */
public class WithdrawalsApplyBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public WithdrawalsApplyBean() {
        data = new Data();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data;

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

    public class Data implements Serializable{
        private static final long serialVersionUID = 1L;
        private String is_need_verify; // 审核状态，1需要审核，0不需要审核
        //<如果is_need_verify=1，则>
        private String consume_id; // 消费id
       // <如果is_need_verify=0，则>
        private String amount; // 本次授信额度 单位分

        public String getIs_need_verify() {
            return is_need_verify;
        }

        public void setIs_need_verify(String is_need_verify) {
            this.is_need_verify = is_need_verify;
        }

        public String getConsume_id() {
            return consume_id;
        }

        public void setConsume_id(String consume_id) {
            this.consume_id = consume_id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
