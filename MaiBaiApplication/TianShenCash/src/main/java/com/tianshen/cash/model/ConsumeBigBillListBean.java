package com.tianshen.cash.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-11-7.
 */
public class ConsumeBigBillListBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public ConsumeBigBillListBean() {
        data_small = new ConsumeSmallBillBean();
        data_big = new ArrayList<ConsumeBigBillItemBean>();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String type; // 订单类型，1：大单，2：小单
    private String img_url; // 图片域名
    private List<ConsumeBigBillItemBean> data_big; // 大单时的数据结构
    private ConsumeSmallBillBean data_small; // 小单时的数据结构

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public List<ConsumeBigBillItemBean> getData_big() {
        return data_big;
    }

    public void setData_big(List<ConsumeBigBillItemBean> data_big) {
        this.data_big = data_big;
    }

    public ConsumeSmallBillBean getData_small() {
        return data_small;
    }

    public void setData_small(ConsumeSmallBillBean data_small) {
        this.data_small = data_small;
    }
}
