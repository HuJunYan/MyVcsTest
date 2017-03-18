package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbyh on 16/7/4.
 */

public class NearByMerchantListBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public NearByMerchantListBean() {
        data = new ArrayList<NearByMerchantItemBean>();
    }
    public static final int SUCCESS = 0; //成功
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String offset; // 默认为0，分页中使用，开始位置【选填】
    private String length; // 默认为30，分页中使用，每次获取数据的长度【选填】
    private String total; // 列表总长度
    private List<NearByMerchantItemBean> data; // 附近的商户列表

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<NearByMerchantItemBean> getData() {
        return data;
    }

    public void setData(List<NearByMerchantItemBean> data) {
        this.data = data;
    }
}
