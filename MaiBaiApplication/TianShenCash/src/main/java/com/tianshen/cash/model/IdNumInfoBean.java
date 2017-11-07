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

    public static class Data {

        private String real_name;
        private String id_num;
        private String gender;
        private String nation;
        private String birthday;
        private String birthplace;
        private String sign_organ;
        private String valid_period;
        private String front_idCard_url;
        private String back_idCard_url;
        private String face_url;
        private String face_change_key;
        public int change_type; //1 face++ 2 udun

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBirthplace() {
            return birthplace;
        }

        public void setBirthplace(String birthplace) {
            this.birthplace = birthplace;
        }

        public String getSign_organ() {
            return sign_organ;
        }

        public void setSign_organ(String sign_organ) {
            this.sign_organ = sign_organ;
        }

        public String getValid_period() {
            return valid_period;
        }

        public void setValid_period(String valid_period) {
            this.valid_period = valid_period;
        }

        public int getChange_type() {
            return change_type;
        }

        public void setChange_type(int change_type) {
            this.change_type = change_type;
        }

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
