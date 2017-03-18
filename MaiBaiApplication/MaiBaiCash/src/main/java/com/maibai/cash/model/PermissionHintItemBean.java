package com.maibai.cash.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/2.
 */
public class PermissionHintItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public String step; // 权限提醒步骤
    public String hint; // 权限提醒的步骤描述

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
