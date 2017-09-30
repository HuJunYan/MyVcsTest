package com.tianshen.cash.model;

import java.util.ArrayList;
import java.util.List;

public class ActivityBean {

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

        private ArrayList<ActivityDataBean> activity_list;
        private String remind_num;
        private String message_count;

        public String getMessage_count() {
            return message_count;
        }

        public void setMessage_count(String message_count) {
            this.message_count = message_count;
        }

        public ArrayList<ActivityDataBean> getActivity_list() {
            return activity_list;
        }

        public void setActivity_list(ArrayList<ActivityDataBean> activity_list) {
            this.activity_list = activity_list;
        }

        public String getRemind_num() {
            return remind_num;
        }

        public void setRemind_num(String remind_num) {
            this.remind_num = remind_num;
        }


    }


}
