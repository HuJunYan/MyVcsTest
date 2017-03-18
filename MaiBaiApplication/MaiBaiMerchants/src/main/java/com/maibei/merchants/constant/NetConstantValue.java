package com.maibei.merchants.constant;

/**
 * Created by zhangchi on 2016/6/14.
 */
public class NetConstantValue {
    // release
//    public static String COMMONURI = "http://182.92.10.40/Home/";
    // test
    public static String COMMONURI = "http://123.56.252.169/Home/";

    public static boolean isReleaseVersion(){
        if ("http://182.92.10.40/Home/".equals(COMMONURI)){
            return true;
        }
        return false;
    }

    public static String merchantSignUp(){
        return COMMONURI + "Protocol/merchantSignUp";
    }

    /**
     * 商户登录
     * api: com.maibei.merchants.net.api.UserLogin.userLogin()
     * @return
     */
    public static String getUserLoginUrl() {
        return COMMONURI + "Merchant/signIn";
    }
    /**
     * 获取该商户的销售记录
     * api: com.maibei.merchants.net.api.GetSaleList.getSaleList()
     * @return
     */
    public static String getSaleListUrl() {
        return COMMONURI + "Merchant/getSaleList";
    }
    /**
     * 商户提现
     * api: com.maibei.merchants.net.api.WithDraw.withdraw()
     * @return
     */
    public static String getWithdrawUrl() {
        return COMMONURI + "Pay/MerchantWithdraw";
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
     * 快速注册
     * api: com.maibei.merchants.net.api.SignUp.signUp()
     *
     * @return
     */
    public static String getSignUpUrl() {
        return COMMONURI + "Merchant/signUp";
    }

    /**
     * 保存商户信息
     * api: com.maibei.merchants.net.api.SaveInfo.saveInfo()
     *
     * @return
     */
    public static String getSaveInfoUrl() {
        return COMMONURI + "Merchant/saveInfo";
    }

    /**
     * 获取银行卡的类型
     * api: com.maibei.merchants.net.api.GetBandCardType.getBandCardType()
     *
     * @return
     */
    public static String getBandCardTypeUrl() {
        return COMMONURI + "Merchant/getBandCardType";
    }

//    /**
//     * 获取银行预留手机号验证码
//     * api: com.maibei.merchants.net.api.GetReserveMobileVerifyCode.getReserveMobileVerifyCode()
//     *
//     * @return
//     */
//    public static String getReserveMobileVerifyCodeUrl() {
//        return COMMONURI + "Merchant/getReserveMobileVerifyCode";
//    }

    /**
     * 绑定银行卡
     * api: com.maibei.merchants.net.api.GetBindBandCard.getBindBandCard()
     *
     * @return
     */
    public static String getBindBandCardUrl() {
        return COMMONURI + "Merchant/bindBankCard";
    }

    /**
     * 重置密码
     * api: com.maibei.merchants.net.api.ResetPassword.resetPassword()
     *
     * @return
     */
    public static String getResetPasswordUrl() {
        return COMMONURI + "Merchant/resetPassword";
    }

    /**
     * 获取商家提现历史
     * api: com.maibei.merchants.net.api.GetWithdrawHistory.getWithdrawHistory()
     *
     * @return
     */
    public static String getWithdrawHistoryUrl() {
        return COMMONURI + "merchant/getWithdrawHistory";
    }

    /**
     * 退出登录
     * api: com.maibei.merchants.net.api.Logout.logout()
     *
     * @return
     */
    public static String getLogoutUrl() {
        return COMMONURI + "Merchant/logout";
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
     * 图片上传接口
     * api: com.maibei.merchants.net.api.UploadImage.uploadImage()
     *
     * @return
     */
    public static String GetUploadImageUrl() {
        return COMMONURI + "MerchantCRO/merchantUploadImage";
    }

    /**
     * 获取可绑定卡的银行列表
     * api: com.maibei.merchants.net.api.GetBankList.getBankList()
     *
     * @return
     */
    public static String GetBankListUrl() {
        return COMMONURI + "Merchant/getBankList";
    }

    /**
     * 解绑银行卡
     * api: com.maibei.merchants.net.api.UnbindBankCard.unbindBankCard()
     *
     * @return
     */
    public static String GetUnbindBankCardUrl() {
        return COMMONURI + "MerchantBankInfo/unbindBankCard";
    }

    /**
     * 获取商户银行卡列表
     * api: com.maibei.merchants.net.api.GetMerchantBankList.getMerchantBankList()
     *
     * @return
     */
    public static String GetMerchantBankListUrl() {
        return COMMONURI + "MerchantBankInfo/getMerchantBankList";
    }

    /**
     * 商户完成资料完善操作
     * api: com.maibei.merchants.net.api.UpdToApprove.updToApprove()
     *
     * @return
     */
    public static String GetUpdToApproveUrl() {
        return COMMONURI + "Merchant/updToApprove";
    }

    /**
     * 获取城市（city）信息
     * api: com.maibei.merchants.net.api.GetCity.getCity()
     *
     * @return
     */
    public static String GetCityUrl() {
        return COMMONURI + "Merchant/getCity";
    }

    /**
     * 获取州县（county）信息
     * api: com.maibei.merchants.net.api.GetCounty.getCounty()
     *
     * @return
     */
    public static String GetCountyUrl() {
        return COMMONURI + "Merchant/getCounty";
    }

    /**
     * 商户佣金列表
     * api: com.maibei.merchants.net.api.GetMerchantCommision.getMerchantCommision()
     *
     * @return
     */
    public static String GetMerchantCommisionUrl() {
        return COMMONURI + "Merchant/MerchantCommisionList";
    }

    /**
     * 商户佣金提现
     * api: com.maibei.merchants.net.api.MerchantCommision.merchantCommision()
     *
     * @return
     */
    public static String merchantCommisionUrl() {
        return COMMONURI + "Merchant/MerchantCommision";
    }
    /*
   * 日志上传接口
   * api:com.maibei.merchants.net.api.UploadLog.uploadLog()
   * */
    public static String getUploadLogUrl(){
        return COMMONURI+"UserCenter/uploadUserLog";
    }
}
