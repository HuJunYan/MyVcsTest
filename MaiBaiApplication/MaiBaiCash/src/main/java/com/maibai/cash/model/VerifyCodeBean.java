package com.maibai.cash.model;

import java.io.Serializable;

/**
 * Created by sbyh on 16/6/29.
 */

public class VerifyCodeBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;  // 返回码
    private String msg;  // 消息内容
    private VerifyCodeDataBean data; // 数据内容

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

    public VerifyCodeDataBean getData() {
        return data;
    }

    public void setData(VerifyCodeDataBean data) {
        this.data = data;
    }
}
