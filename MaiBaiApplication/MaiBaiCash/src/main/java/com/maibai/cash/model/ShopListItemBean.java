package com.maibai.cash.model;

/**
 * Created by zhangchi on 2016/6/20.
 */
public class ShopListItemBean {
    public ShopListItemBean(String url, String name, String distance, String content) {
        this.url = url;
        this.name = name;
        this.distance = distance;
        this.content = content;
    }

    public String url;
    public String name;
    public String distance;
    public String content;
}
