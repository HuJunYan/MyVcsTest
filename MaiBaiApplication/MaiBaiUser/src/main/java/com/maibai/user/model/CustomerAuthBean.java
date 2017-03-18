package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-9-6.
 */
public class CustomerAuthBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public CustomerAuthBean() {
        data = new Data();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容

    private Data data; // 数据内容

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private static final long serialVersionUID = 1L;
        private String credit_must; // 必须认证 0-否 1-必须认证1项 2-必须认证2项 ...
        private String id_num; // 身份证认证 0-未认证 1-已认证
        private String face_pass; // 扫脸活体检测 0-未认证 1-已认证
        private String china_mobile; // 运营商认证 0-未认证 1-已认证
        private String taobao_pass; // 淘宝认证 0-未认证 1-已认证
        private String jd_pass; // 京东认证 0-未认证 1-已认证
        private String alipay_pass;//支付宝认证 0-未认证 1-已认证
        private String zhima_pass;//芝麻信用认证

        public String getAlipay_pass() {
            return alipay_pass;
        }

        public void setAlipay_pass(String alipay_pass) {
            this.alipay_pass = alipay_pass;
        }

        public String getZhima_pass() {
            return zhima_pass;
        }

        public void setZhima_pass(String zhima_pass) {
            this.zhima_pass = zhima_pass;
        }

        public String getCredit_must() {
            return credit_must;
        }

        public void setCredit_must(String credit_must) {
            this.credit_must = credit_must;
        }

        public String getId_num() {
            return id_num;
        }

        public void setId_num(String id_num) {
            this.id_num = id_num;
        }

        public String getFace_pass() {
            return face_pass;
        }

        public void setFace_pass(String face_pass) {
            this.face_pass = face_pass;
        }

        public String getChina_mobile() {
            return china_mobile;
        }

        public void setChina_mobile(String china_mobile) {
            this.china_mobile = china_mobile;
        }

        public String getTaobao_pass() {
            return taobao_pass;
        }

        public void setTaobao_pass(String taobao_pass) {
            this.taobao_pass = taobao_pass;
        }

        public String getJd_pass() {
            return jd_pass;
        }

        public void setJd_pass(String jd_pass) {
            this.jd_pass = jd_pass;
        }
    }
}


