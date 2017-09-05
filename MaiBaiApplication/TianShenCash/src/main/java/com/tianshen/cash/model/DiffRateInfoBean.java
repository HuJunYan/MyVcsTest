package com.tianshen.cash.model;

/**
 * Created by Administrator on 2017/9/5.
 */

public class DiffRateInfoBean {

    public int code;
    public String msg;
    public DiffRateInfo data;

    public class DiffRateInfo {
        public String repayment_amout;//到期还款金额
        public String amount; // 到帐金额
        public String poundage; //利息
        public String consume_amount;// 申请金额
        public String timer;//申请时间
        public String type;// "申请类型 1:月息 2日息"
        public String bank_info; // 绑卡信息
    }
}
