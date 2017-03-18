package com.maibei.merchants.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class WithdrawHistoryBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public WithdrawHistoryBean() {
        data = new ArrayList<WithdrawHistoryItemBean>();
    }
    private int code; // 返回码
    private String msg; //返回消息;
    private String total; //列表总数
    private String offset; // 本页起始位置
    private String length; // 本页长度
    private List<WithdrawHistoryItemBean> data; // 提现历史列表

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public List<WithdrawHistoryItemBean> getData() {
        return data;
    }

    public void setData(List<WithdrawHistoryItemBean> data) {
        this.data = data;
    }
}
