package com.maibai.user.net.base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/7.
 */
public class AuthenticationStatus implements Serializable{
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data;

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

    public class Data{
        List<AuthenticationBean> auth_list;

        public List<AuthenticationBean> getAuth_list() {
            return auth_list;
        }

        public void setAuth_list(List<AuthenticationBean> auth_list) {
            this.auth_list = auth_list;
        }
    }
}
