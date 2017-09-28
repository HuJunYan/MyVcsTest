package com.tianshen.cash.model;


import java.util.List;

public class MessageDataBean {

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

        private List<MessageBean> message_list;

        public List<MessageBean> getMessage_list() {
            return message_list;
        }

        public void setMessage_list(List<MessageBean> message_list) {
            this.message_list = message_list;
        }

    }
}
