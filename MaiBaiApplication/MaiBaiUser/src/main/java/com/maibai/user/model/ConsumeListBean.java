package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-9-14.
 */
public class ConsumeListBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public ConsumeListBean() {
        data = new ArrayList<ConsumeItemBean>();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String total;  // 列表总数
    private String offset;  // 本页起始位置
    private String length;  // 本页长度
    private String img_url; // 图片域名
    private String bind_card;//是否绑定银行卡：0-未绑定 1-已绑定
    private String card_num;//银行卡号
    private String bank_name;//银行名称

    public String getBind_card() {
        return bind_card;
    }

    public void setBind_card(String bind_card) {
        this.bind_card = bind_card;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    private List<ConsumeItemBean> data; // 数据内容

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

    public List<ConsumeItemBean> getData() {
        return data;
    }

    public void setData(List<ConsumeItemBean> data) {
        this.data = data;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}