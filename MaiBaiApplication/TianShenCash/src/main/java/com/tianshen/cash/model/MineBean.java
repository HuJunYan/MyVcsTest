package com.tianshen.cash.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbyh on 16/7/5.
 */

public class MineBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public MineBean() {
        data = new Data();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容

    private Data data; // 数据内容

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

    public class Data implements Serializable {
        private static final long serialVersionUID = 1L;
        public Data() {
            bank_card_list = new ArrayList<MineCardInfoBean>();
        }
        private String need_repay_amount;  // 本月应还总金额,单位分
        private String consume_bill_amount;//本月应还消费账单总额，单位分
        private String withdrawals_bill_amount;//本月应还现金贷账单总额，单位分
        private List<MineCardInfoBean> bank_card_list; // 绑定的银行卡列表

        public String getConsume_bill_amount() {
            return consume_bill_amount;
        }

        public void setConsume_bill_amount(String consume_bill_amount) {
            this.consume_bill_amount = consume_bill_amount;
        }

        public String getWithdrawals_bill_amount() {
            return withdrawals_bill_amount;
        }

        public void setWithdrawals_bill_amount(String withdrawals_bill_amount) {
            this.withdrawals_bill_amount = withdrawals_bill_amount;
        }

        public String getNeed_repay_amount() {
            return need_repay_amount;
        }

        public void setNeed_repay_amount(String need_repay_amount) {
            this.need_repay_amount = need_repay_amount;
        }
        public List<MineCardInfoBean> getBank_card_list() {
            return bank_card_list;
        }

        public void setBank_card_list(List<MineCardInfoBean> bank_card_list) {
            this.bank_card_list = bank_card_list;
        }

    }
}


