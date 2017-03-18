package com.maibei.merchants.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenrongshang on 16/7/3.
 */
public class SaleListBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String total; //总记录数
    private String offset; //分页开始位置
    private String length; //分页长度
    private String balance; //商户余额,单位分
    private String commision_status; // 佣金状态 0-无 1-有

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<SaleListItemBean> data;

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

    public List<SaleListItemBean> getData() {
        return data;
    }

    public void setData(List<SaleListItemBean> data) {
        this.data = data;
    }

    public String getCommision_status() {
        return commision_status;
    }

    public void setCommision_status(String commision_status) {
        this.commision_status = commision_status;
    }
}
