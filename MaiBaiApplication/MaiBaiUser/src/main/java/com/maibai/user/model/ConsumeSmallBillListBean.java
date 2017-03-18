package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by m on 16-11-7.
 */
public class ConsumeSmallBillListBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public ConsumeSmallBillListBean() {
        data = new ConsumeSmallBillBean();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String img_url; // 图片域名

    private ConsumeSmallBillBean data; // 小单时的数据结构

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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public ConsumeSmallBillBean getData() {
        return data;
    }

    public void setData(ConsumeSmallBillBean data) {
        this.data = data;
    }
}
