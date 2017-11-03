package com.tianshen.cash.model;

public class XiangShangDataBean {

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

        private String is_xiangshang;

        public String getIs_xiangshang() {
            return is_xiangshang;
        }

        public void setIs_xiangshang(String is_xiangshang) {
            this.is_xiangshang = is_xiangshang;
        }


    }
}
