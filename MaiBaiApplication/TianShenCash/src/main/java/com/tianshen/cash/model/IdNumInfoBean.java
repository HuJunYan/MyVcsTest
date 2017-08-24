package com.tianshen.cash.model;

public class IdNumInfoBean {

    private int code;
    private String msg;
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

    public class Data {

        private String real_name;
        private String id_num;
        private String front_idCard_url;
        private String back_idCard_url;
        private String face_url;
        private String face_change_key;
        public int face_udn;

        public String getFace_change_key() {
            return face_change_key;
        }

        public void setFace_change_key(String face_change_key) {
            this.face_change_key = face_change_key;
        }

        public String getBack_idCard_url() {
            return back_idCard_url;
        }

        public void setBack_idCard_url(String back_idCard_url) {
            this.back_idCard_url = back_idCard_url;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getId_num() {
            return id_num;
        }

        public void setId_num(String id_num) {
            this.id_num = id_num;
        }

        public String getFront_idCard_url() {
            return front_idCard_url;
        }

        public void setFront_idCard_url(String front_idCard_url) {
            this.front_idCard_url = front_idCard_url;
        }

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }

    }

}
