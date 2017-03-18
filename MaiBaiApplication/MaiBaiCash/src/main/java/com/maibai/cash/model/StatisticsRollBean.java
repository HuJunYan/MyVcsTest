package com.maibai.cash.model;

import java.util.List;

/**
 * Created by chunpengguo on 2017/1/14.
 */

public class StatisticsRollBean {
    private int code;
    private String msg;
    private String apply_count;
    private List<StatisticsRollDataBean> data;

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

    public String getApply_count() {
        return apply_count;
    }

    public void setApply_count(String apply_count) {
        this.apply_count = apply_count;
    }

    public List<StatisticsRollDataBean> getData() {
        return data;
    }

    public void setData(List<StatisticsRollDataBean> data) {
        this.data = data;
    }
}
