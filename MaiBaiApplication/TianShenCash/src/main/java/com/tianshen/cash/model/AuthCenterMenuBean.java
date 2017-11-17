package com.tianshen.cash.model;

/**
 * Created by cuiyue on 2017/11/6.
 */

public class AuthCenterMenuBean {

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

        private String auth_id_num;
        private String auth_person_info;
        private String auth_credit;
        private String cash_amount;
        private String show_dialog;

        public String getShow_dialog() {
            return show_dialog;
        }

        public void setShow_dialog(String show_dialog) {
            this.show_dialog = show_dialog;
        }

        public String getAuth_id_num() {
            return auth_id_num;
        }

        public void setAuth_id_num(String auth_id_num) {
            this.auth_id_num = auth_id_num;
        }

        public String getAuth_person_info() {
            return auth_person_info;
        }

        public void setAuth_person_info(String auth_person_info) {
            this.auth_person_info = auth_person_info;
        }

        public String getAuth_credit() {
            return auth_credit;
        }

        public void setAuth_credit(String auth_credit) {
            this.auth_credit = auth_credit;
        }

        public String getCash_amount() {
            return cash_amount;
        }

        public void setCash_amount(String cash_amount) {
            this.cash_amount = cash_amount;
        }

    }

}
