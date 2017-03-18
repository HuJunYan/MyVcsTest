package com.maibai.cash.model;

import java.io.Serializable;

/**
 * Created by chenrongshang on 16/8/14.
 */
public class SaveCallRecordBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public SaveCallRecordBean() {
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

    public class Data implements Serializable {
        private static final long serialVersionUID = 1L;
        private String last_save_time; // 上次保存通话记录的时间，Unix 时间戳

        public String getLast_save_time() {
            return last_save_time;
        }

        public void setLast_save_time(String last_save_time) {
            this.last_save_time = last_save_time;
        }
    }
}
