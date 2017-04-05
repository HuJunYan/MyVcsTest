package com.tianshen.cash.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-10-19.
 */
public class WithdrawalsRecordBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public WithdrawalsRecordBean() {
        data = new ArrayList<WithdrawalsRecordItemBean>();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String has_next_page; // 是否存在下一页：0-否  1-是
    private String offset; // 本页起始位置
    private String length; // 本页长度
    private List<WithdrawalsRecordItemBean> data; // 数据内容

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

    public String getHas_next_page() {
        return has_next_page;
    }

    public void setHas_next_page(String has_next_page) {
        this.has_next_page = has_next_page;
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

    public List<WithdrawalsRecordItemBean> getData() {
        return data;
    }

    public void setData(List<WithdrawalsRecordItemBean> data) {
        this.data = data;
    }
}