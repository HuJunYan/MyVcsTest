package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbyh on 16/7/5.
 */

public class CertUploadStatusBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public CertUploadStatusBean() {
        data = new ArrayList<CertUploadStatusItemBean>();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private List<CertUploadStatusItemBean> data; // 数据内容

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

    public List<CertUploadStatusItemBean> getData() {
        return data;
    }

    public void setData(List<CertUploadStatusItemBean> data) {
        this.data = data;
    }
}
