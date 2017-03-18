package com.maibai.user.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by m on 16-9-18.
 */
public class OrderRefreshBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public OrderRefreshBean() {
        data = new Data();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data; // 订单数据

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
        private String bank_name; // 银行名称
        private String amount; // 申请金额
        private String merchant_name; // 消费商家
        private String order_id; // 服务单号
        private String add_time; // 申请时间
        private String cur_step; // 当前步骤（与step数组key值对应）
        private String err_code; // 0-正常，1-重新认证（客户端增加重新认证按钮）， 2-业务员拒绝（客户端增加轮询）
        private String discount; // 是否是优惠单，1：是优惠单，0:不是优惠单
        private List<VerifyStepBean> step; // 认证步骤
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getMerchant_name() {
            return merchant_name;
        }

        public void setMerchant_name(String merchant_name) {
            this.merchant_name = merchant_name;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getCur_step() {
            return cur_step;
        }

        public void setCur_step(String cur_step) {
            this.cur_step = cur_step;
        }

        public String getErr_code() {
            return err_code;
        }

        public void setErr_code(String err_code) {
            this.err_code = err_code;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public List<VerifyStepBean> getStep() {
            return step;
        }

        public void setStep(List<VerifyStepBean> step) {
            this.step = step;
        }
    }
}
