package com.maibai.cash.model;

import java.io.Serializable;

/**
 * Created by chunpengguo on 2017/1/17.
 */

public class RecordPopBean implements Serializable {
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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
    public class Data implements Serializable{
        private String alreay_nums;//已通知次数
        private String hope_nums;//期望通知次数

        public String getAlreay_nums() {
            return alreay_nums;
        }

        public void setAlreay_nums(String alreay_nums) {
            this.alreay_nums = alreay_nums;
        }

        public String getHope_nums() {
            return hope_nums;
        }

        public void setHope_nums(String hope_nums) {
            this.hope_nums = hope_nums;
        }
    }
}
