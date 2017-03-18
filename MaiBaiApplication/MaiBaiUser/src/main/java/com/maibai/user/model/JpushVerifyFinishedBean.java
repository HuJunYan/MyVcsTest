package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by m on 16-9-24.
 */
public class JpushVerifyFinishedBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public JpushVerifyFinishedBean() {
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
        private String status; // 1：订单已完成，2：待缴首付 3：已还款，4：次月转分期，5：订单待审核，6：审核未通过，7：决策执行失败，8：订单待付款
        private String consume_id; // 当前消费id
        //   <如果 status=5,用户还在待审核，客户端无需处理 >
        //   <如果 status=6 || status=7, 审核未通过>
        private String reason; // 失败原因
        //   <如果 status=1 || status=4, 审核通过>
        private String down_pay; // 是否需要首付：0-不需要 1-需要
        private String down_payment; // 首付款
        private String bind_card; // 是否绑定银行卡：0-未绑定 1-已绑定
        private String card_num; // 银行卡号
        private String bank_name; // 银行名称"

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

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getDown_pay() {
            return down_pay;
        }

        public void setDown_pay(String down_pay) {
            this.down_pay = down_pay;
        }

        public String getDown_payment() {
            return down_payment;
        }

        public void setDown_payment(String down_payment) {
            this.down_payment = down_payment;
        }

        public String getBind_card() {
            return bind_card;
        }

        public void setBind_card(String bind_card) {
            this.bind_card = bind_card;
        }

        public String getCard_num() {
            return card_num;
        }

        public void setCard_num(String card_num) {
            this.card_num = card_num;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }
    }

}