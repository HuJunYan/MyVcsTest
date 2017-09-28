package com.tianshen.cash.model;


import java.io.Serializable;

public class MessageBean implements Serializable {

    private String msg_id;
    private String msg_time_str;
    private String msg_title;
    private String msg_description;
    private String msg_img_url;
    private String msg_url;
    private String msg_type;
    private String msg_share_title;
    private String msg_share_url;
    private String msg_share_description;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg_time_str() {
        return msg_time_str;
    }

    public void setMsg_time_str(String msg_time_str) {
        this.msg_time_str = msg_time_str;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public String getMsg_description() {
        return msg_description;
    }

    public void setMsg_description(String msg_description) {
        this.msg_description = msg_description;
    }

    public String getMsg_img_url() {
        return msg_img_url;
    }

    public void setMsg_img_url(String msg_img_url) {
        this.msg_img_url = msg_img_url;
    }

    public String getMsg_url() {
        return msg_url;
    }

    public void setMsg_url(String msg_url) {
        this.msg_url = msg_url;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getMsg_share_title() {
        return msg_share_title;
    }

    public void setMsg_share_title(String msg_share_title) {
        this.msg_share_title = msg_share_title;
    }

    public String getMsg_share_url() {
        return msg_share_url;
    }

    public void setMsg_share_url(String msg_share_url) {
        this.msg_share_url = msg_share_url;
    }

    public String getMsg_share_description() {
        return msg_share_description;
    }

    public void setMsg_share_description(String msg_share_description) {
        this.msg_share_description = msg_share_description;
    }

}
