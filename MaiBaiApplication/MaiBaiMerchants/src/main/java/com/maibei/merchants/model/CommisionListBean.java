package com.maibei.merchants.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-11-18.
 */
public class CommisionListBean implements Serializable {
    public CommisionListBean() {
        commision_stages = new ArrayList<CommisionStagesItemBean>();
    }
    private static final long serialVersionUID = 1L;
    private String customer_name; // 用户姓名
    private String consume_date; // 消费日期 格式：2016-11-15 15:32:56
    private String commission_arrive;//到账金额
    private List<CommisionStagesItemBean> commision_stages;

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getConsume_date() {
        return consume_date;
    }

    public void setConsume_date(String consume_date) {
        this.consume_date = consume_date;
    }

    public List<CommisionStagesItemBean> getCommision_stages() {
        return commision_stages;
    }

    public void setCommision_stages(List<CommisionStagesItemBean> commision_stages) {
        this.commision_stages = commision_stages;
    }

    public String getCommission_arrive() {
        return commission_arrive;
    }

    public void setCommission_arrive(String commission_arrive) {
        this.commission_arrive = commission_arrive;
    }
}
