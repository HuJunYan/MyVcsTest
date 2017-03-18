package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbyh on 16/7/4.
 */

public class GetBankListBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public GetBankListBean() {
        data = new ArrayList<GetBankListItemBean>();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private List<GetBankListItemBean> data; // 数据内容

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

    public List<GetBankListItemBean> getData() {
        return data;
    }

    public void setData(List<GetBankListItemBean> data) {
        this.data = data;
    }
}
