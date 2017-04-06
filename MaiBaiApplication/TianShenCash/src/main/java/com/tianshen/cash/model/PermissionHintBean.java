package com.tianshen.cash.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class PermissionHintBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public PermissionHintBean() {
        data = new Data();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data; // 权限开启步骤

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

    public class Data implements Serializable{
        private static final long serialVersionUID = 1L;
        public Data() {
            options = new ArrayList<PermissionHintItemBean>();
        }
        public String title; // 权限提醒标题
        List<PermissionHintItemBean> options;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<PermissionHintItemBean> getOptions() {
            return options;
        }

        public void setOptions(List<PermissionHintItemBean> options) {
            this.options = options;
        }
    }
}
