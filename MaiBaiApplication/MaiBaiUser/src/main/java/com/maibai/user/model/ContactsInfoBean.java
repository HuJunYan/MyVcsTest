package com.maibai.user.model;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * Created by m on 16-10-10.
 */
public class ContactsInfoBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public ContactsInfoBean() {
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
        private String user_mobile;//登录手机号
        private String user_name;//本人姓名
        private String id_card_num;//身份证号
        private String wechat; // 本人微信号
        private String user_address; // 本人常住地址
        private String company_name; // 工作单位
        private String company_phone; // 单位电话
        private String parent_name; // 父母姓名
        private String parent_phone; // 父母电话
        private String parent_address; // 父母住址
        private String brothers_name; // 直亲姓名

        public String getBrothers_name() {
            return brothers_name;
        }

        public void setBrothers_name(String brothers_name) {
            this.brothers_name = brothers_name;
        }

        public String getBrothers_phone() {
            return brothers_phone;
        }

        public void setBrothers_phone(String brothers_phone) {
            this.brothers_phone = brothers_phone;
        }

        public String getQq_num() {
            return qq_num;
        }

        public void setQq_num(String qq_num) {
            this.qq_num = qq_num;
        }

        public String getCompany_address() {
            return company_address;
        }

        public void setCompany_address(String company_address) {
            this.company_address = company_address;
        }

        public String getFriends_name() {
            return friends_name;
        }

        public void setFriends_name(String friends_name) {
            this.friends_name = friends_name;
        }

        public String getFriends_phone() {
            return friends_phone;
        }

        public void setFriends_phone(String friends_phone) {
            this.friends_phone = friends_phone;
        }

        public String getColleague_name() {
            return colleague_name;
        }

        public void setColleague_name(String colleague_name) {
            this.colleague_name = colleague_name;
        }

        public String getColleague_phone() {
            return colleague_phone;
        }

        public void setColleague_phone(String colleague_phone) {
            this.colleague_phone = colleague_phone;
        }

        private String brothers_phone; // 直亲电话

        private String qq_num;//本人qq
        private String company_address;//单位地址
        private String friends_name;//朋友姓名
        private String friends_phone;//朋友电话
        private String colleague_name;//同事姓名
        private String colleague_phone;//同事电话
        private String is_can_change;//是否可修改0：不可以修改，1：可以修改"

        public String getIs_can_change() {
            return is_can_change;
        }

        public void setIs_can_change(String is_can_change) {
            this.is_can_change = is_can_change;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getId_card_num() {
            return id_card_num;
        }

        public void setId_card_num(String id_card_num) {
            this.id_card_num = id_card_num;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getUser_address() {
            return user_address;
        }

        public void setUser_address(String user_address) {
            this.user_address = user_address;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCompany_phone() {
            return company_phone;
        }

        public void setCompany_phone(String company_phone) {
            this.company_phone = company_phone;
        }

        public String getParent_name() {
            return parent_name;
        }

        public void setParent_name(String parent_name) {
            this.parent_name = parent_name;
        }

        public String getParent_phone() {
            return parent_phone;
        }

        public void setParent_phone(String parent_phone) {
            this.parent_phone = parent_phone;
        }

        public String getParent_address() {
            return parent_address;
        }

        public void setParent_address(String parent_address) {
            this.parent_address = parent_address;
        }

    }
}


