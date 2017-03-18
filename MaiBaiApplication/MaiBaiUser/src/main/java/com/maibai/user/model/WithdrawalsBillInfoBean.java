package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-10-19.
 */
public class WithdrawalsBillInfoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    public WithdrawalsBillInfoBean() {
        data = new Data();
    }

    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data; // 数据内容

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

    public class Data implements Serializable {
        public Data() {
            list = new ArrayList<WithdrawalsBillInfoItenBean>();
        }
    	private static final long serialVersionUID = 1L;
        private String amount; // 取现总金额，单位分
        private String transfer_amount; // 到帐金额，单位分
        private String consume_time; // 取时款间 2016-07-10 08:08:08
        private String repay_times; // 取款期次
        private String have_repay; // 已还金额
        private String need_repay_total; // 应还总金额（本金+利息+罚金）
        private String consume_id; // 消费id，外层大id
        private List<WithdrawalsBillInfoItenBean> list;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTransfer_amount() {
            return transfer_amount;
        }

        public void setTransfer_amount(String transfer_amount) {
            this.transfer_amount = transfer_amount;
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


        public String getHave_repay() {
            return have_repay;
        }

        public void setHave_repay(String have_repay) {
            this.have_repay = have_repay;
        }

        public String getNeed_repay_total() {
            return need_repay_total;
        }

        public void setNeed_repay_total(String need_repay_total) {
            this.need_repay_total = need_repay_total;
        }

        public String getConsume_id() {
            return consume_id;
        }

        public void setConsume_id(String consume_id) {
            this.consume_id = consume_id;
        }

        public List<WithdrawalsBillInfoItenBean> getList() {
            return list;
        }

        public void setList(List<WithdrawalsBillInfoItenBean> list) {
            this.list = list;
        }
    }
}
