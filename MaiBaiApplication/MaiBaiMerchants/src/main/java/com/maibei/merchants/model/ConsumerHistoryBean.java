package com.maibei.merchants.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sbyh on 16/6/29.
 */

public class ConsumerHistoryBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;  // 返回码
    private String msg;  // 消息内容
    private List<ConsumerHistoryItemBean> data;

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

    public List<ConsumerHistoryItemBean> getData() {
        return data;
    }

    public void setData(List<ConsumerHistoryItemBean> data) {
        this.data = data;
    }
}
