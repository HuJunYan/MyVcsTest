package com.tianshen.cash.model;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class UserRepayDetailBean {
    public int code;
    public String msg;
    public Detail data;

    public class Detail {
        public List<DetailInfo> dialog_list;
    }

    public class DetailInfo {
        public String title;
        public String value;
        public String value2;
    }

}
