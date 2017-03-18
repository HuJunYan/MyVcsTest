package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/6.
 */
public class BankListBean implements Serializable {
    public BankListBean() {
        data = new ArrayList<BankListItemBean>();
    }
    private static final long serialVersionUID = 1L;
    private int code;  // 返回码
    private String msg;  // 消息内容
    public List<BankListItemBean> data;

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

    public List<BankListItemBean> getData() {
        return data;
    }

    public void setData(List<BankListItemBean> data) {
        this.data = data;
    }
}
