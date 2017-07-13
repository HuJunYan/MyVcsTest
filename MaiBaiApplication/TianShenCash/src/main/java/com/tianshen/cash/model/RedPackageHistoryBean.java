package com.tianshen.cash.model;

public class RedPackageHistoryBean {

    private String type;
    private String title;
    private String time;
    private String money;
    private String is_withdrawals;

    public String getIs_withdrawals() {
        return is_withdrawals;
    }

    public void setIs_withdrawals(String is_withdrawals) {
        this.is_withdrawals = is_withdrawals;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }


}
