package com.tianshen.cash.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-10-17.
 */
public class SelWithdrawalsBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public SelWithdrawalsBean() {
        data = new ArrayList<WithdrawalsItemBean>() {
        };
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String max_cash; // 最大提现金额（单位分）
    private String def_cash; // 当前默认金额（单位分）
    private String unit; // 滑块单位（单位分）
    private List<WithdrawalsItemBean> data; // 可提现数据

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

    public String getMax_cash() {
        return max_cash;
    }

    public void setMax_cash(String max_cash) {
        this.max_cash = max_cash;
    }

    public String getDef_cash() {
        return def_cash;
    }

    public void setDef_cash(String def_cash) {
        this.def_cash = def_cash;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<WithdrawalsItemBean> getData() {
        return data;
    }

    public void setData(List<WithdrawalsItemBean> data) {
        this.data = data;
    }
}
