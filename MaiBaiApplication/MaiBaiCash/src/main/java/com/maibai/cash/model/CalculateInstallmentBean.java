package com.maibai.cash.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sbyh on 16/7/5.
 */

public class CalculateInstallmentBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public CalculateInstallmentBean() {
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String consume_amount;  // 消费金额(带转换的总金额)

    public String getConsume_amount() {
        return consume_amount;
    }

    public void setConsume_amount(String consume_amount) {
        this.consume_amount = consume_amount;
    }

    private List<CalculateInstallmentItemBean> data; // 数据内容

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

    public List<CalculateInstallmentItemBean> getData() {
        return data;
    }

    public void setData(List<CalculateInstallmentItemBean> data) {
        this.data = data;
    }

}
