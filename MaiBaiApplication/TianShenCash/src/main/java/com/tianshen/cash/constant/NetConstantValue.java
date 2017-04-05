package com.tianshen.cash.constant;

/**
 * Created by zhangchi on 2016/6/14.
 */
public class NetConstantValue {
    // release
//    public static String COMMONURI = "http://tsdapi.9maibei.com/Home/";
    // test
    public static String COMMONURI = "http://118.190.83.21/Home/";

    /**
     * 用户登录
     * api: com.maibei.merchants.net.api.SignIn.signIn()
     *
     * @return
     *
     */
    public static String getSignInUrl() {
        return COMMONURI + "UserCenter/signIn";
    }


    public static String getUserLoginPro(){
        //用户注册协议
        return COMMONURI+"Protocol/customerSignUp";
    }
    public static String getServerURL(){
        return COMMONURI+"Protocol/order";
    }

    /**
     * 判断当前是否是正式服务器
     *
     * @return true 代表正式服务器，false代表测试服务器
     */
    public static boolean checkIsReleaseService() {
        return "http://tsdapi.9maibei.com/Home/".equals(COMMONURI);
    }

    public static String getRedPackageUrl() {
        return COMMONURI + "Webapp/activity";
    }
    /**
     * 获取验证码
     * api: com.maibei.merchants.net.api.GetVerifyCode.getVerifyCode()
     *
     * @return
     */
    public static String getVerifyCodeUrl() {
        return COMMONURI + "Customer/getVerifyCode";
    }

    /**
     * 设置支付密码
     * api: com.maibei.merchants.net.api.SetPayPassword.setPayPassword()
     *
     * @return
     */
    public static String getSetPayPassUrl() {
        return COMMONURI + "Customer/setPayPass";
    }

    /**
     * 验证支付密码
     * api: com.maibei.merchants.net.api.VerifyPayPass.verifyPayPass()
     *
     * @return
     */
    public static String getVerifyPayPassUrl() {
        return COMMONURI + "Customer/verifyPayPass";
    }

    /**
     * 保存用户身份证信息
     * api: com.maibei.merchants.net.api.SaveIdCardInformation.saveIdCardInformation()
     *
     * @return
     */
    public static String getSaveIdCardInformationUrl() {
        return COMMONURI + "Customer/saveIdCardInformation";
    }

    /**
     * 保存用户信息
     * api: com.maibei.merchants.net.api.SaveInformation.saveInformation()
     *
     * @return
     */
    public static String getSaveInformationUrl() {
        return COMMONURI + "Customer/saveInformation";
    }


//    /**
//     * 添加银行卡（绑卡）
//     * api: com.maibei.merchants.net.api.BindBankCard.bindBankCard()
//     *
//     * @return
//     */
//    public static String getBindBankCardUrl() {
//        return COMMONURI + "UserCenter/bindBankCard";
//    }

    /**
     * 获取已经绑定的银行卡列表
     * api: com.maibei.merchants.net.api.GetBankList.getBankList()
     *
     * @return
     */
    public static String getBankListUrl() {
        return COMMONURI + "UserCenter/getBankList";
    }

    /**
     * 批量保存联系人
     * api: com.maibei.merchants.net.api.SaveContacts.saveContacts()
     *
     * @return
     */
    public static String getSaveContactsUrl() {
        return COMMONURI + "System/saveContacts";
    }

    /**
     * 批量保存通话记录 (支持增量，即只传上次上行数据之后的通话记录)
     * api: com.maibei.merchants.net.api.SaveCallRecord.saveCallRecord()
     *
     * @return
     */
    public static String getSaveCallRecordUrl() {
        return COMMONURI + "System/saveCallRecord";
    }

    /**
     * 取上一次保存通话记录的时间，下次增量上传通话记录的开始时间
     * api: com.maibei.merchants.net.api.SetLastSaveCallRecordTime.getLastSaveCallRecordTime()
     *
     * @return
     */
    public static String getLastSaveCallRecordTimeUrl() {
        return COMMONURI + "System/getLastSaveCallRecordTime";
    }

    /**
     * 我的(我的主页)
     * api: com.maibei.merchants.net.api.Mine.mine()
     *
     * @return
     */
    public static String getMineUrl() {
        return COMMONURI + "UserCenter/mine";
    }

    /**
     * 还款，立即还款
     * api: com.maibei.merchants.net.api.Repayment.repayment()
     *
     * @return
     */
    public static String getRepaymentUrl() {
        return COMMONURI + "Pay/payConfirm";
    }


    /**
     * 检查版本更新
     * api: com.maibei.merchants.net.api.CheckUpgrade.checkUpgrade()
     *
     * @return
     */
    public static String GetCheckUpgradeUrl() {
        return COMMONURI + "System/checkUpgrade";
    }


    /**
     * 图片上传接口
     * api: com.maibei.merchants.net.api.UploadImage.uploadImage()
     *
     * @return
     */
    public static String GetUploadImageUrl() {
        return COMMONURI + "UserCenter/uploadImage";
    }


    /**
     * 重新设置密码
     * api: com.maibei.merchants.net.api.ResetPassword.resetPassword()
     *
     * @return
     */
    public static String getResetPasswordUrl() {
        return COMMONURI + "Customer/resetPassword";
    }


    /**
     * 获取打开权限提示语
     * api: com.maibei.merchants.net.api.GetOpenPermissionHint.getOpenPermissionHint()
     *
     * @return
     */
    public static String getOpenPermissionHintUrl() {
        return COMMONURI + "Customer/getOpenPermissionHint";
    }

    /**
     * 退出登录
     * api: com.maibei.merchants.net.api.Logout.logout()
     *
     * @return
     */
    public static String getLogoutUrl() {
        return COMMONURI + "UserCenter/logout";
    }

    /**
     * 绑定银行卡时获取验证码
     * api: com.maibei.merchants.net.api.GetBindVerifySms.getBindVerifySms()
     *
     * @return
     */
    public static String getBindVerifySmsUrl() {
        return COMMONURI + "Pay/bindVerifySms";
    }

    /**
     * 绑定银行卡
     * api: com.maibei.merchants.net.api.BindBankCard.bindBankCard()
     *
     * @return
     */
    public static String getBindConfirmUrl() {
        return COMMONURI + "Pay/bindConfirm";
    }

    /**
     * 获取可绑定卡的银行列表
     * api: com.maibei.merchants.net.api.GetAllBankList.getAllBankList()
     *
     * @return
     */
    public static String GetAllBankListUrl() {
        return COMMONURI + "UserCenter/getAllBankList";
    }

    /**
     * 解绑银行卡
     * api: com.maibei.merchants.net.api.UnbindBankCard.unbindBankCard()
     *
     * @return
     */
    public static String GetUnbindBankCardUrl() {
        return COMMONURI + "Pay/unbindBankCard";
    }

    /**
     * 用户注册
     * api: com.maibei.merchants.net.api.GetSignUp.getSignUp()
     *
     * @return
     */
    public static String getSignUpUrl() {
        return COMMONURI + "UserCenter/signUp";
    }

    /**
     * 活体检测成功通知
     * api: com.maibei.merchants.net.api.CreditFace.creditFace()
     *
     * @return
     */
    public static String getCreditFaceUrl() {
        return COMMONURI + "Credit/CreditFace";
    }

    /*
    * 微信支付下单
    * api:com.maibei.user.net.api.GetWeChatOrder.getWeChatOrder()
    * */
    public static String getWeChatOrderURl(){
        return COMMONURI+"Pay/wxpayConfirm";
    }

    /*
    * 获取用户当前认证信息
    * api:com.maibei.user.net.api.GetCustomerAuth.getCustomerAuth()
    * */
    public static String getCustomerAuthURl(){
        return COMMONURI+"Credit/getCustomerAuth";
    }

    /*
    * 获取提升额度界面显示列表状态
    *api:com.maibei.user.net.api.GetAuthListStatus.getAuthList()
    * */
    public static String getAuthListURL(){
        return COMMONURI+"Credit/getAuthList";
    }



    /*
     * 上传联系人信息
     *api:com.maibei.user.net.api.UploadContactsInfo.uploadContactsInfo()
    * */
    public static String getUploadContactsInfoURL(){
        return COMMONURI+"Credit/uploadContactsInfo";
    }

    /*
    * 获取联系人信息
    *api:com.maibei.user.net.api.GetContactsInfo.getContactsInfo()
    * */
    public static String getGetContactsInfoURL(){
        return COMMONURI+"Credit/getContactsInfo";
    }

    /*
    * 修改联系人信息
    *api:com.maibei.user.net.api.ChangeContactsInfo.changeContactsInfo()
    * */
    public static String getChangeContactsInfoURL(){
        return COMMONURI+"Credit/changeContactsInfo";
    }

    /*
    * 获取提现账单
    *api:com.maibei.user.net.api.GetWithdrawalsBill.getWithdrawalsBill()
    * */
    public static String getGetWithdrawalsBillURL(){
        return COMMONURI+"UserCenter/getWithdrawalsBill";
    }

    /*
    * 提现账单还款
    *api:com.maibei.user.net.api.RepayWithdrawals.repayWithdrawals()
    * */
    public static String getRepayWithdrawalsURL(){
        return COMMONURI+"UserCenter/repayWithdrawals";
    }

    /*
    * 提现产品选择
    *api:com.maibei.user.net.api.SelWithdrawals.selWithdrawals()
    * */
    public static String getSelWithdrawalsURL(){
        return COMMONURI+"WithdrawalsOrder/selWithdrawals";
    }

    /*
    * 现金贷申请
    *api:com.maibei.user.net.api.WithdrawalsApply.withdrawalsApply()
    * */
    public static String getWithdrawalsApplyURL(){
        return COMMONURI+"WithdrawalsOrder/withdrawalsApply";
    }

    /*
    * 提现待审核刷新
    *api:com.maibei.user.net.api.WithdrawalsRefresh.withdrawalsRefresh()
    * */
    public static String getWithdrawalsRefreshURL(){
        return COMMONURI+"WithdrawalsOrder/withdrawalsRefresh";
    }

    /*
    * 现金贷提现下单
    *api:com.maibei.user.net.api.WithdrawalsOrder.withdrawalsOrder()
    * */
    public static String getWithdrawalsOrderURL(){
        return COMMONURI+"WithdrawalsOrder/withdrawalsOrder";
    }

    /*
    * 获取提现账单详情
    *api:com.maibei.user.net.api.GetWithdrawalsBillInfo.getWithdrawalsBillInfo()
    * */
    public static String getWithdrawalsBillInfoURL(){
        return COMMONURI+"UserCenter/GetWithdrawalsBillInfo";
    }

    /*
    * 获取取现记录
    *api:com.maibei.user.net.api.GetWithdrawalsRecord.getWithdrawalsRecord()
    * */
    public static String getWithdrawalsRecordURL(){
        return COMMONURI+"UserCenter/getWithdrawalsRecord";
    }

    /*
    * 现金贷审核通过的确认
    *api:com.maibei.user.net.api.CashVerifyConfirm.cashVerifyConfirm()
    * */
    public static String getCashVerifyConfirmURL(){
        return COMMONURI+"UserCenter/cashVerifyConfirm";
    }


    /*
    * 日志上传接口
    * api:com.maibei.merchants.net.api.UploadLog.uploadLog()
    * */
    public static String getUploadLogUrl(){
        return COMMONURI+"UserCenter/uploadUserLog";
//        return COMMONURI + "MerchantCRO/merchantUploadImage";
//        return "http://192.168.31.178:8080/UploadFileServer/upload";
    }

    /*
    * 运营商已重复认证
    * 分期消费等待审核界面使用
    *
    * */
    public static String getAgainUrl(){
        return COMMONURI+"Credit/creditMobile";
    }

    public static String getStatisticsRollUrl() {
        return COMMONURI+"UserCenter/getDemographic";
    }


    /**
     *
     *
     * 决策拒绝通知后台
     */
    public static String getRecordPopupTimeUrl(){
        return COMMONURI+"Customer/RecordPopupTimes";
    }
}