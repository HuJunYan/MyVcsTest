package com.tianshen.cash.model;

import java.io.Serializable;

public class ResponseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 0; //成功
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

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

    public class Data {
        private String introduction;  // 强制升级内容
        private String download_url;  // 强制升级下载链接

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

    }
}
