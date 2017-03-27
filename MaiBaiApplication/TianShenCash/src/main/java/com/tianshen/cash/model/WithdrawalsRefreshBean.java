package com.tianshen.cash.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by m on 16-10-17.
 */
public class WithdrawalsRefreshBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public WithdrawalsRefreshBean() {
        data = new Data();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
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

    public class Data implements Serializable{
        private static final long serialVersionUID = 1L;
        private String status; // 1：订单已完成，5：订单待审核，6：审核未通过，7：决策执行失败
        // <如果 status=5,用户还在待审核，客户端无需处理 >
        private String amount; // 现金贷授权金额

        // <如果 status=6 || status=7, 审核未通过>
        private String reason; // 失败原因

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
