package com.tianshen.cash.model;

/**
 * Created by wang on 2017/10/13.
 */

public class XiangShangVerifyCodeBean {

    private int code;  // 返回码

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

    private String msg;  // 消息内容
    private Data data;

    public class Data {
        private String smsId;
        private String userNo;

        public String getSmsId() {
            return smsId;
        }

        public void setSmsId(String smsId) {
            this.smsId = smsId;
        }

        public String getUserNo() {
            return userNo;
        }

        public void setUserNo(String userNo) {
            this.userNo = userNo;
        }


    }

    ;
}
