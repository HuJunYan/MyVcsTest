package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by m on 16-10-18.
 */
public class JpushWithdrawalsVerifyFinishedBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public JpushWithdrawalsVerifyFinishedBean() {
        msg_content = new MsgContent();
    }
    private String msg_type; // 消费类型 1-审核结果（待审核订单）
    private MsgContent msg_content;

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public MsgContent getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(MsgContent msg_content) {
        this.msg_content = msg_content;
    }

    public class MsgContent implements Serializable {
        private String status; // 1：订单已完成，6：审核未通过，7：决策执行失败
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