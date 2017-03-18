package com.maibai.cash.model;

import java.io.Serializable;

/**
 * Created by zhangchi on 2016/7/6.
 */
public class IDCardBean implements Serializable {
    public String name;
    public long time_used;
    public String gender;
    public String id_card_number;    //"410181198910064510",
    public String request_id;       //"1467790126,da25565a-1e53-410d-9288-f1785ba437e3",
    public String side;             //"front"
    public String race;              //"汉"
    public String address;          //"河南省巩义市孝义办事处孝南村景沟街129号",
    public String issued_by;
    public String valid_date;
    public Birthday birthday;
    public Legality legality;

    public class Birthday implements Serializable {
        public String month;  //"10",
        public String day;  //"6",
        public String year;  //"1989"
    }

    public class Legality implements Serializable {
        public double ID_Photo;
    }
}
