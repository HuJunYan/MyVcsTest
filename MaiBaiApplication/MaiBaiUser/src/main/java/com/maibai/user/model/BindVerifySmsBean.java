package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/5.
 */
public class BindVerifySmsBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public BindVerifySmsBean() {
        data = new Data();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data; // 验证码信息

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
        private String bind_no;  // 绑定码

        public String getBind_no() {
            return bind_no;
        }

        public void setBind_no(String bind_no) {
            this.bind_no = bind_no;
        }
    }
}
