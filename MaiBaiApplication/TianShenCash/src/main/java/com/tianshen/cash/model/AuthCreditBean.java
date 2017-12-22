package com.tianshen.cash.model;

import java.util.ArrayList;

public class AuthCreditBean {

    private int code;
    private String msg;
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

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


    public class Data {

        private String page_title;
        private String required_content;
        private String not_required_content;
        private String required_background;
        private String not_required_background;
        private ArrayList<RequiredBean> required;
        private ArrayList<RequiredBean> not_required;

        public String getPage_title() {
            return page_title;
        }

        public void setPage_title(String page_title) {
            this.page_title = page_title;
        }

        public String getRequired_content() {
            return required_content;
        }

        public void setRequired_content(String required_content) {
            this.required_content = required_content;
        }

        public String getNot_required_content() {
            return not_required_content;
        }

        public void setNot_required_content(String not_required_content) {
            this.not_required_content = not_required_content;
        }

        public String getRequired_background() {
            return required_background;
        }

        public void setRequired_background(String required_background) {
            this.required_background = required_background;
        }

        public String getNot_required_background() {
            return not_required_background;
        }

        public void setNot_required_background(String not_required_background) {
            this.not_required_background = not_required_background;
        }

        public ArrayList<RequiredBean> getRequired() {
            return required;
        }

        public void setRequired(ArrayList<RequiredBean> required) {
            this.required = required;
        }

        public ArrayList<RequiredBean> getNot_required() {
            return not_required;
        }

        public void setNot_required(ArrayList<RequiredBean> not_required) {
            this.not_required = not_required;
        }


    }


}
