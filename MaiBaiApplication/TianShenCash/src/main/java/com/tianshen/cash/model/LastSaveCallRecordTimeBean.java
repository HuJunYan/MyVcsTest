package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by chenrongshang on 16/7/5.
 */
public class LastSaveCallRecordTimeBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public LastSaveCallRecordTimeBean() {
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
        private static final long serialVersionUID = 1L;
        private String customer_id;  // 用户ID
        private String last_save_time;  // 上次保存通话记录的时间，Unix 时间戳

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }

        public String getLast_save_time() {
            return last_save_time;
        }

        public void setLast_save_time(String last_save_time) {
            this.last_save_time = last_save_time;
        }
    }

}
