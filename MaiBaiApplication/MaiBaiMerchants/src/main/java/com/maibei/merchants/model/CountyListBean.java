package com.maibei.merchants.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-11-2.
 */
public class CountyListBean implements Serializable {
    public CountyListBean() {
        data = new ArrayList<CountyListItemBean>();
    }
    private static final long serialVersionUID = 1L;
    private int code;  // 返回码
    private String msg;  // 消息内容
    public List<CountyListItemBean> data;

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

    public List<CountyListItemBean> getData() {
        return data;
    }

    public void setData(List<CountyListItemBean> data) {
        this.data = data;
    }
}
