package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by crslljj on 16/12/27.
 */

public class VerifyStepBean implements Serializable{
    private String display; // 步骤描述
    private String date_time; // 步骤审批时间
    private String opr_name; // 经办人

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getOpr_name() {
        return opr_name;
    }

    public void setOpr_name(String opr_name) {
        this.opr_name = opr_name;
    }
}
