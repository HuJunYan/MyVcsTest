package com.maibai.cash.net.base;

import android.content.Context;
import android.text.TextUtils;

import com.maibai.cash.model.SignInBean;
import com.maibai.cash.utils.SharedPreferencesUtil;

/**
 * Created by chenrongshang on 16/7/3.
 */
public class UserUtil {
    /**
     * 获取注册成功后产生的用户ID
     *
     * @param context
     * @return
     */
    public static String getId(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("id");
    }

    public static int getScanTimes(Context context){
        return SharedPreferencesUtil.getInstance(context).getInt("scanfacetimes");
    }
    public static void setScanTimes(Context context,int time){
        SharedPreferencesUtil.getInstance(context).putInt("scanfacetimes",time);
    }
    /**
     * 获取设备ID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("device_id");
    }

    /**
     * 获取姓名
     *
     * @param context
     * @return
     */
    public static String getRealName(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("real_name");
    }

    /**
     * 获取手机号
     *
     * @param context
     * @return
     */
    public static String getMobile(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("mobile");
    }

    /**
     * 获取身份证号
     *
     * @param context
     * @return
     */
    public static String getIdNum(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("id_num");
    }

    /*
    * 获取log状态信息
    *
    * */
    public static String getLogStatus(Context context){
        return SharedPreferencesUtil.getInstance(context).getString("log_status");
    }
    /*
    * 设置log上传的状态
    * */

    public static void setLogStatus(Context context,String status){
        SharedPreferencesUtil.getInstance(context).putString("log_status",status);
    }
    /**
     * 获取是否设置密码0:未设置,1:已设置
     *
     * @param context
     * @return
     */
    public static String getIsSetPayPass(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("isset_pay_pass");
    }

    /**
     * 获取当前总的信用额度，单位分
     *
     * @param context
     * @return
     */
    public static String getCustomerAmount(Context context) {
        String amount = SharedPreferencesUtil.getInstance(context).getString("customer_amount");
        return amount.isEmpty() ? "0" : amount;
    }

    /**
     * 获取当前剩余信用额度, 单位分
     *
     * @param context
     * @return
     */
    public static String getBalanceAmount(Context context) {
        String balance = SharedPreferencesUtil.getInstance(context).getString("balance_amount");
        return balance.isEmpty() ? "0" : balance;
    }

    /**
     * 获取最高可提升的额度, 单位分
     *
     * @param context
     * @return
     */
    public static String getMaxAmount(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("max_amount");
    }

    /**
     * 认证阶梯：0-未认证 1-已身份认证 2-已face++认证
     *
     * @param context
     * @return
     */
    public static String getCreditStep(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("credit_step");
    }

    /**
     * 获取创建时间 Unix 时间戳, 单位秒
     *
     * @param context
     * @return
     */
    public static String getCreateTime(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("create_time");
    }

    /**
     * 获取更新时间 Unix 时间戳, 单位秒
     *
     * @param context
     * @return
     */
    public static String getUpdateTime(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("update_time");
    }

    /**
     * face++通过的分数
     * @param context
     * @return
     */
    public static String getFacePassScore(Context context) {

        return SharedPreferencesUtil.getInstance(context).getString("face_pass_score");
    }



    public static String getAlreadyNums(Context context){
        return SharedPreferencesUtil.getInstance(context).getString("already_nums");
    }
    public static String getHopeNums(Context context){
        return SharedPreferencesUtil.getInstance(context).getString("hope_nums");
    }
    public static void setAlreadyNums(Context context,String alreadyNums){
        SharedPreferencesUtil.getInstance(context).putString("already_nums",alreadyNums);
    }
    public static void setHopeNums(Context context,String hopeNums){
        SharedPreferencesUtil.getInstance(context).putString("hope_nums",hopeNums);
    }


    /**
     * 实付金额/申请提升额度
     * @param context
     * @return
     */
    public static String getCreditAmount(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("credit_amount");
    }

    /**
     * 消费时间/申请日期 年-月-日 时:分:秒
     * @param context
     * @return
     */
    public static String getAddTime(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("add_time");
    }

    /**
     * 消费类型 1：次月还款 2：分期还款 3：提升额度
     * @param context
     * @return
     */
    public static String getType(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("type");
    }

    /**
     * 商户id
     * @param context
     * @return
     */
    public static String getMerchantId(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("merchant_id");
    }

    /**
     * 商户名称
     * @param context
     * @return
     */
    public static String getMerchantName(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("merchant_name");
    }

    /**
     * 消费金额
     * @param context
     * @return
     */
    public static String getConsumeAmount(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("consume_amount");
    }

    /**
     * 优惠金额（商家优惠金额+平台优惠金额）
     * @param context
     * @return
     */
    public static String getReduceAmount(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("reduce_amount");
    }

    /**
     * 逾期金额
     * @param context
     * @return
     */
    public static String getOverdueAmount(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("overdue_amount");
    }

    /**
     * 还款时间 年-月-日
     * @param context
     * @return
     */
    public static String getRepaymentTime(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("repayment_time");
    }

    /**
     * 首付款
     * @param context
     * @return
     */
    public static String getDownPayment(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("down_payment");
    }

    /**
     * 是否绑定银行卡：0-未绑定 1-已绑定
     * @param context
     * @return
     */
    public static String getBindCard(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("bind_card");
    }

    /**
     * 银行卡号
     * @param context
     * @return
     */
    public static String getCardNum(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("card_num");
    }

    /**
     * 消费id
     * @param context
     * @return
     */
    public static String getConsumeId(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("consume_id");
    }

    /**
     * 银行名称
     * @param context
     * @return
     */
    public static String getBankName(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("bank_name");
    }


    /**
     * 登录密码
     * @param context
     * @return
     */
    public static String getLoginPsw(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("login_password");
    }

    /**
     * 现金贷当前信审状态
     * @param context
     * @return
     */
    public static String getCashCreditStatus(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("cash_credit_status");
    }

    /**
     * 提现申请订单id
     * @param context
     * @return
     */
    public static String getCashCreditConsumeId(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("cash_credit_consume_id");
    }

    /**
     * 提现申请失败原因
     * @param context
     * @return
     */
    public static String getCashCreditReason(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("cash_credit_reason");
    }

    /**
     * 当前总的现金贷信用额度，单位分
     * @param context
     * @return
     */
    public static String getCashAmount(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("cash_amount");
    }

    /**
     * 当前剩余现金贷信用额度, 单位分
     * @param context
     * @return
     */
    public static String getCashBalanceAmount(Context context) {

        return SharedPreferencesUtil.getInstance(context).getString("balance_cash_amount");
    }

    /**
     * 现金贷申请审核通过后，是否弹框：0-不需要 1-需要
     * @param context
     * @return
     */
    public static String getCashNeedPop(Context context) {

        return SharedPreferencesUtil.getInstance(context).getString("cash_need_pop");
    }

    /**
     * 设置注册成功后产生的用户ID
     *
     * @param context
     * @param id
     */
    public static void setId(Context context, String id) {
        SharedPreferencesUtil.getInstance(context).putString("id", id);
    }

    /**
     * 设置设备ID
     *
     * @param context
     * @param device_id
     */
    public static void setDeviceId(Context context, String device_id) {
        SharedPreferencesUtil.getInstance(context).putString("device_id", device_id);
    }

    /**
     * 设置姓名
     *
     * @param context
     * @param real_name
     */
    public static void setRealName(Context context, String real_name) {
        SharedPreferencesUtil.getInstance(context).putString("real_name", real_name);
    }

    /**
     * 设置手机号
     *
     * @param context
     * @param mobile
     */
    public static void setMobile(Context context, String mobile) {
        SharedPreferencesUtil.getInstance(context).putString("mobile", mobile);
    }

    /**
     * 设置身份证号
     *
     * @param context
     * @param id_num
     */
    public static void setIdNum(Context context, String id_num) {
        SharedPreferencesUtil.getInstance(context).putString("id_num", id_num);
    }

    /**
     * 设置是否设置密码0:未设置,1:已设置
     *
     * @param context
     * @param isset_pay_pass
     */
    public static void setIssetPayPass(Context context, String isset_pay_pass) {
        SharedPreferencesUtil.getInstance(context).putString("isset_pay_pass", isset_pay_pass);
    }

    /**
     * 设置当前总的信用额度，单位分
     *
     * @param context
     * @param amount
     */
    public static void setCustomerAmount(Context context, String amount) {
        SharedPreferencesUtil.getInstance(context).putString("customer_amount", amount);
    }

    /**
     * 设置当前剩余信用额度, 单位分
     *
     * @param context
     * @param balance_amount
     */
    public static void setBalanceAmount(Context context, String balance_amount) {
        SharedPreferencesUtil.getInstance(context).putString("balance_amount", balance_amount);
    }

    /**
     * 设置最高可提升的额度, 单位分
     *
     * @param context
     * @param max_amount
     */
    public static void setMaxAmount(Context context, String max_amount) {
        SharedPreferencesUtil.getInstance(context).putString("max_amount", max_amount);
    }

    /**
     * 认证阶梯：0-未认证 1-已身份认证 2-已face++认证
     *
     * @param context
     * @return
     */
    public static void setCreditStep(Context context, String credit_step) {
        SharedPreferencesUtil.getInstance(context).putString("credit_step", credit_step);
    }

    /**
     * 设置创建时间 Unix 时间戳
     *
     * @param context
     * @param create_time
     */
    public static void setCreateTime(Context context, String create_time) {
        SharedPreferencesUtil.getInstance(context).putString("create_time", create_time);
    }

    /**
     * 设置更新时间 Unix 时间戳
     *
     * @param context
     * @param update_time
     */
    public static void setUpdateTime(Context context, String update_time) {
        SharedPreferencesUtil.getInstance(context).putString("update_time", update_time);
    }

    /**
     * face++通过的分数
     * @param context
     * @return
     */
    public static void setFacePassScore(Context context, String face_pass_score) {
        SharedPreferencesUtil.getInstance(context).putString("face_pass_score", face_pass_score);
    }



    /**
     * 实付金额/申请提升额度
     *
     * @param context
     * @param credit_amount
     */
    public static void setCreditAmount(Context context, String credit_amount) {
        SharedPreferencesUtil.getInstance(context).putString("credit_amount", credit_amount);
    }

    /**
     * 消费时间/申请日期 年-月-日 时:分:秒
     *
     * @param context
     * @param add_time
     */
    public static void setAddTime(Context context, String add_time) {
        SharedPreferencesUtil.getInstance(context).putString("add_time", add_time);
    }

    /**
     * 消费类型 1：次月还款 2：分期还款 3：提升额度
     *
     * @param context
     * @param type
     */
    public static void setType(Context context, String type) {
        SharedPreferencesUtil.getInstance(context).putString("type", type);
    }

    /**
     * 商户id
     *
     * @param context
     * @param merchant_id
     */
    public static void setMerchantId(Context context, String merchant_id) {
        SharedPreferencesUtil.getInstance(context).putString("merchant_id", merchant_id);
    }

    /**
     * 商户名称
     *
     * @param context
     * @param merchant_name
     */
    public static void setMerchantName(Context context, String merchant_name) {
        SharedPreferencesUtil.getInstance(context).putString("merchant_name", merchant_name);
    }

    /**
     * 消费金额
     *
     * @param context
     * @param consume_amount
     */
    public static void setConsumeAmount(Context context, String consume_amount) {
        SharedPreferencesUtil.getInstance(context).putString("consume_amount", consume_amount);
    }

    /**
     * 优惠金额（商家优惠金额+平台优惠金额）
     *
     * @param context
     * @param reduce_amount
     */
    public static void setReduceAmount(Context context, String reduce_amount) {
        SharedPreferencesUtil.getInstance(context).putString("reduce_amount", reduce_amount);
    }

    /**
     * 逾期金额
     *
     * @param context
     * @param overdue_amount
     */
    public static void setOverdueAmount(Context context, String overdue_amount) {
        SharedPreferencesUtil.getInstance(context).putString("overdue_amount", overdue_amount);
    }

    /**
     * 还款时间 年-月-日
     *
     * @param context
     * @param repayment_time
     */
    public static void setRepaymentTime(Context context, String repayment_time) {
        SharedPreferencesUtil.getInstance(context).putString("repayment_time", repayment_time);
    }

    /**
     * 首付款
     *
     * @param context
     * @param down_payment
     */
    public static void setDownPayment(Context context, String down_payment) {
        SharedPreferencesUtil.getInstance(context).putString("down_payment", down_payment);
    }

    /**
     * 是否绑定银行卡：0-未绑定 1-已绑定
     *
     * @param context
     * @param bind_card
     */
    public static void setBindCard(Context context, String bind_card) {
        SharedPreferencesUtil.getInstance(context).putString("bind_card", bind_card);
    }

    /**
     * 银行卡号
     *
     * @param context
     * @param card_num
     */
    public static void setCardNum(Context context, String card_num) {
        SharedPreferencesUtil.getInstance(context).putString("card_num", card_num);
    }

    /**
     * 消费id
     *
     * @param context
     * @param consume_id
     */
    public static void setConsumeId(Context context, String consume_id) {
        SharedPreferencesUtil.getInstance(context).putString("consume_id", consume_id);
    }

    /**
     * 银行名称
     *
     * @param context
     * @param bank_name
     */
    public static void setBankName(Context context, String bank_name) {
        SharedPreferencesUtil.getInstance(context).putString("bank_name", bank_name);
    }

    /**
     * 登录密码
     *
     * @param context
     * @param login_password
     */
    public static void setLoginPassword(Context context, String login_password) {
        SharedPreferencesUtil.getInstance(context).putString("login_password", login_password);
    }

    /**
     * 现金贷当前信审状态
     * @param context
     * @return
     */
    public static void setCashCreditStatus(Context context, String cash_credit_status) {
        SharedPreferencesUtil.getInstance(context).putString("cash_credit_status", cash_credit_status);
    }

    /**
     * 提现申请订单id
     * @param context
     * @return
     */
    public static void setCashCreditConsumeId(Context context, String cash_credit_consume_id) {
        SharedPreferencesUtil.getInstance(context).putString("cash_credit_consume_id", cash_credit_consume_id);
    }

    /**
     * 提现申请失败原因
     * @param context
     * @return
     */
    public static void setCashCreditReason(Context context, String cash_credit_reason) {
        SharedPreferencesUtil.getInstance(context).putString("cash_credit_reason", cash_credit_reason);
    }

    /**
     * 当前总的现金贷信用额度，单位分
     * @param context
     * @return
     */
    public static void setCashAmount(Context context, String cash_amount) {
        SharedPreferencesUtil.getInstance(context).putString("cash_amount", cash_amount);
    }

    /**
     * 当前剩余现金贷信用额度, 单位分
     * @param context
     * @return
     */
    public static void setCashBalanceAmount(Context context, String balance_cash_amount) {
        SharedPreferencesUtil.getInstance(context).putString("balance_cash_amount", balance_cash_amount);
    }

    /**
     * 现金贷申请审核通过后，是否弹框：0-不需要 1-需要
     * @param context
     * @return
     */
    public static void setCashNeedPop(Context context, String cash_need_pop) {

        SharedPreferencesUtil.getInstance(context).putString("cash_need_pop", cash_need_pop);
    }

    public static void setUser(Context context, SignInBean signIn) {
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getId())) {
            setId(context, signIn.getData().getCustomer().getId());
        }
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getDevice_id())) {
            setDeviceId(context, signIn.getData().getCustomer().getDevice_id());
        }
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getReal_name())) {
            setRealName(context, signIn.getData().getCustomer().getReal_name());
        }
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getMobile())) {
            setMobile(context, signIn.getData().getCustomer().getMobile());
        }
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getId_num())) {
            setIdNum(context, signIn.getData().getCustomer().getId_num());
        }
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getIsset_pay_pass())) {
            setIssetPayPass(context, signIn.getData().getCustomer().getIsset_pay_pass());
        }
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getAmount())) {
            setCustomerAmount(context, signIn.getData().getCustomer().getAmount());
        }
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getBalance_amount())) {
            setBalanceAmount(context, signIn.getData().getCustomer().getBalance_amount());
        }
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getMax_amount())) {
            setMaxAmount(context, signIn.getData().getCustomer().getMax_amount());
        }

        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getCredit_step())) {
            setCreditStep(context, signIn.getData().getCustomer().getCredit_step());
        }
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getCreate_time())) {
            setCreateTime(context, signIn.getData().getCustomer().getCreate_time());
        }
        if (!TextUtils.isEmpty(signIn.getData().getCustomer().getUpdate_time())) {
            setUpdateTime(context, signIn.getData().getCustomer().getUpdate_time());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCustomer().getFace_pass_score())){
            setFacePassScore(context, signIn.getData().getCustomer().getFace_pass_score());
        }

        if(!TextUtils.isEmpty(signIn.getData().getCash_credit().getReason())){
            setCashCreditReason(context, signIn.getData().getCredit().getReason());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getAmount())){
            setCreditAmount(context, signIn.getData().getCredit().getAmount());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getAdd_time())){
            setAddTime(context, signIn.getData().getCredit().getAdd_time());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getType())){
            setType(context, signIn.getData().getCredit().getType());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getMerchant_id())){
            setMerchantId(context, signIn.getData().getCredit().getMerchant_id());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getMerchant_name())){
            setMerchantName(context, signIn.getData().getCredit().getMerchant_name());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getConsume_amount())){
            setConsumeAmount(context, signIn.getData().getCredit().getConsume_amount());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getReduce_amount())){
            setReduceAmount(context, signIn.getData().getCredit().getReduce_amount());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getOverdue_amount())){
            setOverdueAmount(context, signIn.getData().getCredit().getOverdue_amount());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getRepayment_time())){
            setRepaymentTime(context, signIn.getData().getCredit().getRepayment_time());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getDown_payment())){
            setDownPayment(context, signIn.getData().getCredit().getDown_payment());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getBind_card())){
            setBindCard(context, signIn.getData().getCredit().getBind_card());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getCard_num())){
            setCardNum(context, signIn.getData().getCredit().getCard_num());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getBank_name())){
            setBankName(context, signIn.getData().getCredit().getBank_name());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCredit().getConsume_id())){
            setConsumeId(context, signIn.getData().getCredit().getConsume_id());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCash_credit().getStatus())){
            setCashCreditStatus(context, signIn.getData().getCash_credit().getStatus());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCash_credit().getConsume_id())){
            setCashCreditConsumeId(context, signIn.getData().getCash_credit().getConsume_id());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCash_credit().getReason())){
            setCashCreditReason(context, signIn.getData().getCash_credit().getReason());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCustomer().getCash_amount())){
            setCashAmount(context, signIn.getData().getCustomer().getCash_amount());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCustomer().getBalance_cash_amount())){
            setCashBalanceAmount(context, signIn.getData().getCustomer().getBalance_cash_amount());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCash_credit().getNeed_pop())){
            setCashNeedPop(context, signIn.getData().getCash_credit().getNeed_pop());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCash_credit().getAlready_nums())){
            setAlreadyNums(context,signIn.getData().getCash_credit().getAlready_nums());
        }
        if(!TextUtils.isEmpty(signIn.getData().getCash_credit().getHope_nums())){
            setHopeNums(context,signIn.getData().getCash_credit().getHope_nums());
        }
    }

    public static void removeUser(Context context) {
        SharedPreferencesUtil preferencesUtil = SharedPreferencesUtil.getInstance(context);
        preferencesUtil.clearSp();
       /* preferencesUtil.remove("id");
        preferencesUtil.remove("device_id");
        preferencesUtil.remove("real_name");
        preferencesUtil.remove("mobile");
        preferencesUtil.remove("id_num");
        preferencesUtil.remove("isset_pay_pass");
        preferencesUtil.remove("customer_amount");
        preferencesUtil.remove("balance_amount");
        preferencesUtil.remove("max_amount");
        preferencesUtil.remove("credit_step");
        preferencesUtil.remove("create_time");
        preferencesUtil.remove("update_time");

        preferencesUtil.remove("status");
        preferencesUtil.remove("reason");
        preferencesUtil.remove("credit_amount");
        preferencesUtil.remove("add_time");
        preferencesUtil.remove("type");
        preferencesUtil.remove("merchant_id");
        preferencesUtil.remove("merchant_name");
        preferencesUtil.remove("consume_amount");
        preferencesUtil.remove("reduce_amount");
        preferencesUtil.remove("overdue_amount");
        preferencesUtil.remove("repayment_time");
        preferencesUtil.remove("down_payment");
        preferencesUtil.remove("bind_card");
        preferencesUtil.remove("card_num");
        preferencesUtil.remove("bank_name");
        preferencesUtil.remove("consume_id");
        preferencesUtil.remove("login_password");
        preferencesUtil.remove("cash_credit_status");
        preferencesUtil.remove("cash_credit_consume_id");
        preferencesUtil.remove("cash_credit_reason");
        preferencesUtil.remove("cash_amount");
        preferencesUtil.remove("balance_cash_amount");
        preferencesUtil.remove("cash_need_pop");*/
    }
}
