package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by m on 16-10-19.
 */
public class WithdrawalsRecordItemBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String consume_id; // 消费id
    private String amount; // 取现总金额，单位分
    private String consume_time; // 取时款间 2016-07-10 08:08:08
    private String repay_times; // 取款期次
    private String status; // 订单状态
    private String is_payway; // "0自营，1掌中，2手机贷"
    private String sjd_url; // "手机贷URL"

    public String getIs_payway() {
        return is_payway;
    }

    public void setIs_payway(String is_payway) {
        this.is_payway = is_payway;
    }

    public String getSjd_url() {
        return sjd_url;
    }

    public void setSjd_url(String sjd_url) {
        this.sjd_url = sjd_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getConsume_id() {
        return consume_id;
    }

    public void setConsume_id(String consume_id) {
        this.consume_id = consume_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getConsume_time() {
        return consume_time;
    }

    public void setConsume_time(String consume_time) {
        this.consume_time = consume_time;
    }

    public String getRepay_times() {
        return repay_times;
    }

    public void setRepay_times(String repay_times) {
        this.repay_times = repay_times;
    }
}
