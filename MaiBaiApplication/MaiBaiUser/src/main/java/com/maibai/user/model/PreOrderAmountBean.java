package com.maibai.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-9-18.
 */
public class PreOrderAmountBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public PreOrderAmountBean() {
        data = new Data();
    }
    private int code;  // 返回码
    private String msg;  // 消息内容
    private Data data; // 订单数据
    private String img_url; // 图片域名

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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public class Data implements Serializable{
        public Data() {
            installment_info = new ArrayList<InstallmentInfoItemBean>();
        }
        private static final long serialVersionUID = 1L;
        private String down_payment_lowest;//最低首付
        private String type; // 还款方式 1-次月还款 2-分期还款
        private String discount_first_order; // 首单减免
        private String discount_full_reduce; // 商家满减
        private String discount_rebate_amount; // 商家折扣减免
        private String discount_amount; // 优惠总金额
        private String down_payment; // 首付金额
        private String amount; // 实付金额,单位分
//        private String repay_total; // 每期还款总数,单位分
//        private String repay_times; // 分期次数
        private String merchant_name; // 商户名称
        private String merchant_logo; // 商户图标
        private List<InstallmentInfoItemBean> installment_info; // 分期列表

        public String getDown_payment_lowest() {
            return down_payment_lowest;
        }

        public void setDown_payment_lowest(String down_payment_lowest) {
            this.down_payment_lowest = down_payment_lowest;
        }

        public String getMerchant_name() {
            return merchant_name;
        }

        public void setMerchant_name(String merchant_name) {
            this.merchant_name = merchant_name;
        }

        public String getMerchant_logo() {
            return merchant_logo;
        }

        public void setMerchant_logo(String merchant_logo) {
            this.merchant_logo = merchant_logo;
        }

        public List<InstallmentInfoItemBean> getInstallment_info() {
            return installment_info;
        }

        public void setInstallment_info(List<InstallmentInfoItemBean> installment_info) {
            this.installment_info = installment_info;
        }

        public String getDiscount_first_order() {
            return discount_first_order;
        }

        public void setDiscount_first_order(String discount_first_order) {
            this.discount_first_order = discount_first_order;
        }

        public String getDiscount_full_reduce() {
            return discount_full_reduce;
        }

        public void setDiscount_full_reduce(String discount_full_reduce) {
            this.discount_full_reduce = discount_full_reduce;
        }

        public String getDiscount_amount() {
            return discount_amount;
        }

        public void setDiscount_amount(String discount_amount) {
            this.discount_amount = discount_amount;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDiscount_rebate_amount() {
            return discount_rebate_amount;
        }

        public void setDiscount_rebate_amount(String discount_rebate_amount) {
            this.discount_rebate_amount = discount_rebate_amount;
        }

        public String getDown_payment() {
            return down_payment;
        }

        public void setDown_payment(String down_payment) {
            this.down_payment = down_payment;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

     /*   public String getRepay_total() {
            return repay_total;
        }

        public void setRepay_total(String repay_total) {
            this.repay_total = repay_total;
        }

        public String getRepay_times() {
            return repay_times;
        }

        public void setRepay_times(String repay_times) {
            this.repay_times = repay_times;
        }*/
    }
}
