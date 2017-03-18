package com.maibei.merchants.model;

import java.io.Serializable;

/**
 * Created by sbyh on 16/7/5.
 */

public class CheckUpgradeBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public CheckUpgradeBean() {
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
        private String app_type;  // 1:客户端，2:商户端（此字段用于app验证服务器是否搞混`端`）
        private String download_url;  // http://serverName/download/app/android-1.2.apk
        private String introduction;  // 升级介绍
        private String force_upgrade;  // 0：非强制升级，1:强制升级

        public String getApp_type() {
            return app_type;
        }

        public void setApp_type(String app_type) {
            this.app_type = app_type;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getForce_upgrade() {
            return force_upgrade;
        }

        public void setForce_upgrade(String force_upgrade) {
            this.force_upgrade = force_upgrade;
        }
    }
}
