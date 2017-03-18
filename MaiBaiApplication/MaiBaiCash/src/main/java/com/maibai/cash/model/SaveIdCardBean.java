package com.maibai.cash.model;

import java.io.Serializable;

/**
 * Created by chunpengguo on 2017/1/17.
 */

public class SaveIdCardBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 0; //成功
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
        private String qualified;//是否为借贷合格用户：0-不合格 1-合格
        private String reason;//不合格原因

        public String getQualified() {
            return qualified;
        }

        public void setQualified(String qualified) {
            this.qualified = qualified;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
