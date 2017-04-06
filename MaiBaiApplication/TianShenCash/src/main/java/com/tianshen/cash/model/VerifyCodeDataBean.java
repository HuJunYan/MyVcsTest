package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by sbyh on 16/6/29.
 */

public class VerifyCodeDataBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int verify_code;  //验证码
    private String mobile;  //手机号

    public int getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(int verify_code) {
        this.verify_code = verify_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
