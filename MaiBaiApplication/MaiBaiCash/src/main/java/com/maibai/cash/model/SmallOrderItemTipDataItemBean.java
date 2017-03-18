package com.maibai.cash.model;

import java.io.Serializable;

/**
 * Created by m on 16-11-7.
 */
public class SmallOrderItemTipDataItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String img; // 小图标
    private String name; // 名称
    private String money; // 钱数

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
