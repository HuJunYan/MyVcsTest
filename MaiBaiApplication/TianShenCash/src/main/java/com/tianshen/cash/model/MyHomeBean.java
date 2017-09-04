package com.tianshen.cash.model;

public class MyHomeBean {

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

        private String red_packet_string;
        private String share_string;
        private String service_url;

        public String getService_url() {
            return service_url;
        }

        public void setService_url(String service_url) {
            this.service_url = service_url;
        }

        public String getRed_packet_string() {
            return red_packet_string;
        }

        public void setRed_packet_string(String red_packet_string) {
            this.red_packet_string = red_packet_string;
        }

        public String getShare_string() {
            return share_string;
        }

        public void setShare_string(String share_string) {
            this.share_string = share_string;
        }

    }


}
