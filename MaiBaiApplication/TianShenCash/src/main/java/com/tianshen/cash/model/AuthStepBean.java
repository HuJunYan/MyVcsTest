package com.tianshen.cash.model;

import java.util.ArrayList;

public class AuthStepBean {

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

        private String current_step;
        private String all_step;

        public String getCurrent_step() {
            return current_step;
        }

        public void setCurrent_step(String current_step) {
            this.current_step = current_step;
        }

        public String getAll_step() {
            return all_step;
        }

        public void setAll_step(String all_step) {
            this.all_step = all_step;
        }

    }


}
