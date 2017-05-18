package com.tianshen.cash.model;


import java.io.Serializable;

public class IknowBean {
    /**
     * code : 0
     * msg : success
     */

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

        private String is_pop; // 当前剩余授信额度
        private String content; // 虚拟消费id

        public String getIs_pop() {
            return is_pop;
        }

        public void setIs_pop(String is_pop) {
            this.is_pop = is_pop;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
