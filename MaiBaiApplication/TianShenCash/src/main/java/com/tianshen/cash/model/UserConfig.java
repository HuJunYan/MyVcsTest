package com.tianshen.cash.model;

public class UserConfig {

    private int code;
    private String msg;
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

    public class Data {

        private String consume_id; //如果没有待完成的订单，取值为0
        private String virtual_card_num;//虚拟银行卡号
        private String virtual_card_type; // 0:绿卡，1:银卡，2:金卡，3:钻石卡”
        private String cur_credit_step;//当前的认证步数
        private String total_credit_step; //总的认证步数
        private String status;//0:新用户，没有提交过订单；1:订单待审核；2:审核通过待放款；3:放款成功（钱已经到银行卡）；4:审核失败；5:放款失败
        private String reason;//审核失败的原因

        public String getConsume_id() {
            return consume_id;
        }

        public void setConsume_id(String consume_id) {
            this.consume_id = consume_id;
        }

        public String getVirtual_card_num() {
            return virtual_card_num;
        }

        public void setVirtual_card_num(String virtual_card_num) {
            this.virtual_card_num = virtual_card_num;
        }

        public String getVirtual_card_type() {
            return virtual_card_type;
        }

        public void setVirtual_card_type(String virtual_card_type) {
            this.virtual_card_type = virtual_card_type;
        }

        public String getCur_credit_step() {
            return cur_credit_step;
        }

        public void setCur_credit_step(String cur_credit_step) {
            this.cur_credit_step = cur_credit_step;
        }

        public String getTotal_credit_step() {
            return total_credit_step;
        }

        public void setTotal_credit_step(String total_credit_step) {
            this.total_credit_step = total_credit_step;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

    }


}
