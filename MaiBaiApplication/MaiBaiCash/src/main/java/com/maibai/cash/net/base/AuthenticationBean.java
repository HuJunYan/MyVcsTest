package com.maibai.cash.net.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/7.
 */
public class AuthenticationBean implements Serializable{
    private String id;
    private String name;
    private String url;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
