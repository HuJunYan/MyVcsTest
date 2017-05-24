package com.tianshen.cash.model;

import java.util.ArrayList;

/**
 * Created by cuiyue on 2017/5/24.
 */

public class SuperMarkerBean {

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

        private ArrayList<SuperMarketData> supermarket_list;

        public ArrayList<SuperMarketData> getSupermarket_list() {
            return supermarket_list;
        }

        public void setSupermarket_list(ArrayList<SuperMarketData> supermarket_list) {
            this.supermarket_list = supermarket_list;
        }


        public class SuperMarketData {

            private String url;
            private String icon_url;
            private String name;
            private String description;
            private String people_count;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getIcon_url() {
                return icon_url;
            }

            public void setIcon_url(String icon_url) {
                this.icon_url = icon_url;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getPeople_count() {
                return people_count;
            }

            public void setPeople_count(String people_count) {
                this.people_count = people_count;
            }

        }

    }

}
