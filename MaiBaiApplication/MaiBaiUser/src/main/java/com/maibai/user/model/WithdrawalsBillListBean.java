package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-10-14.
 */
public class WithdrawalsBillListBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public WithdrawalsBillListBean () {
        data = new ArrayList<WithdrawalsBillItemBean>();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String total; // 列表总数
    private String offset; // 本页起始位置
    private String length; // 本页长度
    private List<WithdrawalsBillItemBean> data; // 数据内容

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

    public List<WithdrawalsBillItemBean> getData() {
        return data;
    }

    public void setData(List<WithdrawalsBillItemBean> data) {
        this.data = data;
    }
}
