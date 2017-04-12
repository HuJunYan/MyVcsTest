package com.tianshen.cash.model;


public class UserAuthCenterBean {


    /**
     * code : 0
     * msg : 成功
     * data : {"id_num":"1","contacts_pass":"0","bankcard_pass":"0","china_mobile":"0","userdetail_pass":"0","china_mobile_url":"url"}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id_num : 1
         * contacts_pass : 0
         * bankcard_pass : 0
         * china_mobile : 0
         * userdetail_pass : 0
         * china_mobile_url : url
         */

        private String id_num;
        private String face_pass;
        private String contacts_pass;
        private String bankcard_pass;
        private String china_mobile;
        private String userdetail_pass;
        private String china_mobile_url;

        public String getFace_pass() {
            return face_pass;
        }

        public void setFace_pass(String face_pass) {
            this.face_pass = face_pass;
        }

        public String getId_num() {
            return id_num;
        }

        public void setId_num(String id_num) {
            this.id_num = id_num;
        }

        public String getContacts_pass() {
            return contacts_pass;
        }

        public void setContacts_pass(String contacts_pass) {
            this.contacts_pass = contacts_pass;
        }

        public String getBankcard_pass() {
            return bankcard_pass;
        }

        public void setBankcard_pass(String bankcard_pass) {
            this.bankcard_pass = bankcard_pass;
        }

        public String getChina_mobile() {
            return china_mobile;
        }

        public void setChina_mobile(String china_mobile) {
            this.china_mobile = china_mobile;
        }

        public String getUserdetail_pass() {
            return userdetail_pass;
        }

        public void setUserdetail_pass(String userdetail_pass) {
            this.userdetail_pass = userdetail_pass;
        }

        public String getChina_mobile_url() {
            return china_mobile_url;
        }

        public void setChina_mobile_url(String china_mobile_url) {
            this.china_mobile_url = china_mobile_url;
        }
    }
}
