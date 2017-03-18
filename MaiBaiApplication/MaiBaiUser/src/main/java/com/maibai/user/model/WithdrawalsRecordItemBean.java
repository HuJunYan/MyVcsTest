package com.maibai.user.model;

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
