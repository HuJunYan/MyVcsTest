package com.maibei.merchants.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-11-18.
 */
public class CommisionBean implements Serializable {
    public CommisionBean() {
        data = new CommisionData();
    }
    private static final long serialVersionUID = 1L;
    private int code;  // 返回码
    private String msg;  // 消息内容
    private String total; // 总记录数
    private String offset; // 分页开始位置
    private String length; // 分页长度
    private CommisionData data;
    public class CommisionData implements Serializable {
        private static final long serialVersionUID = 1L;
        public CommisionData() {
            commision_list = new ArrayList<CommisionListBean>();
        }
        private String commision_all; // 全部佣金
        private String commission_total;//该月份内佣金总数(已给到的)
        private String commission_get;//该月份(已转)提现佣金
        private String commission_fine;//该月份逾期佣金
        private String pre_commission;//上月佣金
        private String next_commission;//下月佣金
        private List<CommisionListBean> commision_list; // 佣金列表
        private String date_time;

        public String getPre_commission() {
            return pre_commission;
        }

        public void setPre_commission(String pre_commission) {
            this.pre_commission = pre_commission;
        }

        public String getNext_commission() {
            return next_commission;
        }

        public void setNext_commission(String next_commission) {
            this.next_commission = next_commission;
        }

        public String getDate_time() {
            return date_time;
        }

        public void setDate_time(String date_time) {
            this.date_time = date_time;
        }

        public String getCommission_total() {
            return commission_total;
        }

        public void setCommission_total(String commission_total) {
            this.commission_total = commission_total;
        }

        public String getCommission_get() {
            return commission_get;
        }

        public void setCommission_get(String commission_get) {
            this.commission_get = commission_get;
        }

        public String getCommission_fine() {
            return commission_fine;
        }

        public void setCommission_fine(String commission_fine) {
            this.commission_fine = commission_fine;
        }

        public String getCommision_all() {
            return commision_all;
        }

        public void setCommision_all(String commision_all) {
            this.commision_all = commision_all;
        }

        public List<CommisionListBean> getCommision_list() {
            return commision_list;
        }

        public void setCommision_list(List<CommisionListBean> commision_list) {
            this.commision_list = commision_list;
        }
    }

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

    public CommisionData getData() {
        return data;
    }

    public void setData(CommisionData data) {
        this.data = data;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
