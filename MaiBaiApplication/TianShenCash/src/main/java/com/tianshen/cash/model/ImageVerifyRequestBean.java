package com.tianshen.cash.model;

import java.util.Map;

/**
 * Created by zhangchi on 2016/7/6.
 */
public class ImageVerifyRequestBean {
    public String name;
    public String idcard;
    public String imageref;
    public String delta;
    public Map<String, byte[]> images;

    public ImageVerifyRequestBean() {
    }

    public ImageVerifyRequestBean(String name, String idcard, String imageref, String delta, Map<String, byte[]> images) {
        this.name = name;
        this.idcard = idcard;
        this.imageref = imageref;
        this.delta = delta;
        this.images = images;
    }
}
