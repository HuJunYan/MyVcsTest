package com.tianshen.cash.model;

public class OrderConfirmBean {

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

        private String type;
        private String consume_amount;
        private String timer;
        private String poundage;
        private String amount;
        private String bank_name;
        private String id_num;
        private String detail;
        private String repayment_amout;
        private String repay_id;
        private String is_jump;
        private String second_party;
        private String unit_address;
        private String common_address;
        private String phone;
        private String date_time;
        private String expire;
        private String days;
        private String protocol_num;
        private String overdue;
        private String total;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getSecond_party() {
            return second_party;
        }

        public void setSecond_party(String second_party) {
            this.second_party = second_party;
        }

        public String getUnit_address() {
            return unit_address;
        }

        public void setUnit_address(String unit_address) {
            this.unit_address = unit_address;
        }

        public String getCommon_address() {
            return common_address;
        }

        public void setCommon_address(String common_address) {
            this.common_address = common_address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDate_time() {
            return date_time;
        }

        public void setDate_time(String date_time) {
            this.date_time = date_time;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getProtocol_num() {
            return protocol_num;
        }

        public void setProtocol_num(String protocol_num) {
            this.protocol_num = protocol_num;
        }

        public String getOverdue() {
            return overdue;
        }

        public void setOverdue(String overdue) {
            this.overdue = overdue;
        }

        public String getIs_jump() {
            return is_jump;
        }

        public void setIs_jump(String is_jump) {
            this.is_jump = is_jump;
        }

        public String getRepay_id() {
            return repay_id;
        }

        public void setRepay_id(String repay_id) {
            this.repay_id = repay_id;
        }

        public String getRepayment_amout() {
            return repayment_amout;
        }

        public void setRepayment_amout(String repayment_amout) {
            this.repayment_amout = repayment_amout;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getConsume_amount() {
            return consume_amount;
        }

        public void setConsume_amount(String consume_amount) {
            this.consume_amount = consume_amount;
        }

        public String getTimer() {
            return timer;
        }

        public void setTimer(String timer) {
            this.timer = timer;
        }

        public String getPoundage() {
            return poundage;
        }

        public void setPoundage(String poundage) {
            this.poundage = poundage;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getId_num() {
            return id_num;
        }

        public void setId_num(String id_num) {
            this.id_num = id_num;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

    }


}
