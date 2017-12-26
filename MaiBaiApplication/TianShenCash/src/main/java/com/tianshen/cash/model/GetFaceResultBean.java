package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by wang on 2017/12/26.
 */

public class GetFaceResultBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;  // 返回码

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

    private String msg;  // 消息内容
    private Data data; // 数据内容

    public class Data implements Serializable {
        private static final long serialVersionUID = 1L;

        public String getIs_pass() {
            return is_pass;
        }

        public void setIs_pass(String is_pass) {
            this.is_pass = is_pass;
        }

        public String is_pass;
    }
}
