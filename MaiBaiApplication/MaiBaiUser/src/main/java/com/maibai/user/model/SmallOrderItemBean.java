package com.maibai.user.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by m on 16-11-7.
 */
public class SmallOrderItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id; // 账单id，内层小id
    private String principal; // 应还本息
    private String late_fee; // 逾期金额，没有逾期返回0
    private String time; // 还款时间：已经还款的格式：08-10已还，逾期的格式：逾期6天，未到期的格式：11-10待还
    private String state; // 账单状态，0：已还，1：待还有逾期，2：待还无逾期
    private String repay_amount; // 本笔应还总金额（本笔的本金+利息+罚金），单位分
    private String repay_date; // 还款日期，2016-10-10
    private String need_pay; // 需付款，单位分
    private String save_amount; // 节省金额，单位分，注：没有节省传0
    private boolean isChecked;//是否选中
    private List<SmallOrderItemTipDataItemBean> money_data; // 各种钱的计算结果值

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getLate_fee() {
        return late_fee;
    }

    public void setLate_fee(String late_fee) {
        this.late_fee = late_fee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRepay_amount() {
        return repay_amount;
    }

    public void setRepay_amount(String repay_amount) {
        this.repay_amount = repay_amount;
    }

    public String getRepay_date() {
        return repay_date;
    }

    public void setRepay_date(String repay_date) {
        this.repay_date = repay_date;
    }

    public String getNeed_pay() {
        return need_pay;
    }

    public void setNeed_pay(String need_pay) {
        this.need_pay = need_pay;
    }

    public String getSave_amount() {
        return save_amount;
    }

    public void setSave_amount(String save_amount) {
        this.save_amount = save_amount;
    }

    public List<SmallOrderItemTipDataItemBean> getMoney_data() {
        return money_data;
    }

    public void setMoney_data(List<SmallOrderItemTipDataItemBean> money_data) {
        this.money_data = money_data;
    }
}
