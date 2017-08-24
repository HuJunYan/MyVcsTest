package com.tianshen.cash.model;

/**
 * Created by Administrator on 2017/8/24.
 */

public class UserRepayDetailBean {
    public int code;
    public String msg;
    public Detail data;

    public class Detail {
        public String consume_amount;
        public String interest;
        public String service_charge;
        public String late_fee;
        public String late_charge;
        public String overdue_management_fee;
        public String annual_interest_rate;
        public String collect_customer_money;
    }

}
