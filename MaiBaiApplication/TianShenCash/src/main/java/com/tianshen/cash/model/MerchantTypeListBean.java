package com.tianshen.cash.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-9-14.
 */
public class MerchantTypeListBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public MerchantTypeListBean() {
        data = new ArrayList<MerchantTypeItemBean>();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String img_url; // 图片域名
    private List<MerchantTypeItemBean> data; // 数据内容

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

    public List<MerchantTypeItemBean> getData() {
        return data;
    }

    public void setData(List<MerchantTypeItemBean> data) {
        this.data = data;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}


