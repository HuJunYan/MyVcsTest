package com.tianshen.cash.constant;

/**
 * Created by zhangchi on 2016/6/14.
 */
public class NetConstantValue {

    //正式
//    public static String HOST = "http://pro.tianshenjr.com/";

    //预发布
//    public static String HOST = "http://pre.tianshenjr.com/";

    //测试
//    public static String HOST = "http://tst.tianshenjr.com/";

    //开发
    public static String HOST = "http://dev.tianshenjr.com/";


    public static String COMMONURI = HOST + "Home/";

    /**
     * 判断当前是否是正式服务器
     *
     * @return true 代表正式服务器，false代表测试服务器
     */
    public static boolean checkIsReleaseService() {
        return "http://pro.tianshenjr.com/Home/".equals(COMMONURI);
    }

    /**
     * 得到天神贷用户服务协议URL
     */
    public static String getUserServiceProtocolURL() {
        return HOST + "h5/protocol/customerSignUp.html";
    }

    /**
     * 得到天神贷借款协议
     */
    public static String getUserPayProtocolURL() {
        return HOST + "h5/protocol/agreement.html";
    }

    /**
     * 得到天神贷还款协议
     */
    public static String getUserRePayProtocolURL() {
        return HOST + "h5/protocol/order.html";
    }

    /**
     * 得到天神贷移动客服URL
     */
    public static String getServiceOnlineURL() {
        return "https://www.sobot.com/chat/h5/index.html?sysNum=eed6a16224794580a33bf2e3a4e4a16a";
    }

    /**
     * 用户登录
     * api: com.maibei.merchants.net.api.SignIn.signIn()
     *
     * @return
     */
    public static String getSignInUrl() {
        return COMMONURI + "UserCenter/signIn";
    }


    public static String getUserLoginPro() {
        //用户注册协议
        return COMMONURI + "Protocol/customerSignUp";
    }

    public static String getServerURL() {
        return COMMONURI + "Protocol/order";
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
     * 取上一次保存通话记录的时间，下次增量上传通话记录的开始时间
     * api: com.maibei.merchants.net.api.SetLastSaveCallRecordTime.getLastSaveCallRecordTime()
     *
     * @return
     */
    public static String getLastSaveCallRecordTimeUrl() {
        return COMMONURI + "System/getLastSaveCallRecordTime";
    }

    /**
     * 还款，立即还款
     * api: com.maibei.merchants.net.api.Repayment.repayment()
     * //TODO  由此字段 替换为箭头指向字段 服务器还在改 暂时不能保证没有问题payConfirm->repaymentProcessing
     *
     * @return
     */
    public static String getRepaymentUrl() {
        return COMMONURI + "Pay/repaymentProcessing";
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
        return COMMONURI + "UserCenter/resetPassword";
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
     * 解绑银行卡
     */
    public static String getUnBindBankCardUrl() {
        return COMMONURI + "Pay/unBindBankCard";
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
     * 用户注册
     * api: com.maibei.merchants.net.api.GetSignUp.getSignUp()
     *
     * @return
     */
    public static String getSignUpUrl() {
        return COMMONURI + "UserCenter/signUp";
    }

    /**
     * 扫脸成功通知服务器
     */
    public static String getCreditFaceUrl() {
        return COMMONURI + "Customer/creditFace";
    }

    /*
    * 提现产品选择
    *api:com.maibei.user.net.api.SelWithdrawals.selWithdrawals()
    * */
    public static String getSelWithdrawalsURL() {
        return COMMONURI + "WithdrawalsOrder/selWithdrawals";
    }

    /*
    * 现金贷申请
    *api:com.maibei.user.net.api.WithdrawalsApply.withdrawalsApply()
    * */
    public static String getWithdrawalsApplyURL() {
        return COMMONURI + "WithdrawalsOrder/withdrawalsApply";
    }

    /*
    * 获取取现记录
    *api:com.maibei.user.net.api.GetWithdrawalsRecord.getWithdrawalsRecord()
    * */
    public static String getWithdrawalsRecordURL() {
        return COMMONURI + "UserCenter/getWithdrawalsRecord";
    }

    /*
    * 现金贷审核通过的确认
    *api:com.maibei.user.net.api.CashVerifyConfirm.cashVerifyConfirm()
    * */
    public static String getCashVerifyConfirmURL() {
        return COMMONURI + "UserCenter/cashVerifyConfirm";
    }


    /*
    * 日志上传接口
    * api:com.maibei.merchants.net.api.UploadLog.uploadLog()
    * */
    public static String getUploadLogUrl() {
        return COMMONURI + "UserCenter/uploadUserLog";
//        return COMMONURI + "MerchantCRO/merchantUploadImage";
//        return "http://192.168.31.178:8080/UploadFileServer/upload";
    }

    /**
     * 首页滚动数据
     */
    public static String getStatisticsRollUrl() {
        return COMMONURI + "UserCenter/getDemographic";
    }


    /**
     * 决策拒绝通知后台
     */
    public static String getRecordPopupTimeUrl() {
        return COMMONURI + "Customer/RecordPopupTimes";
    }

    /**
     * 得到用户配置
     */
    public static String getUserConfigUrl() {
        return COMMONURI + "UserCenter/userConfig";
    }

    /**
     * 得到用户身份认证的信息
     */
    public static String getUserIdNumInfoUrl() {
        return COMMONURI + "UserCenter/getIdNumInfo";
    }

    /**
     * 得到用户认证中心信息
     */
    public static String getUserAuthCenterUrl() {
        return COMMONURI + "UserCenter/authCenter";
    }

    /**
     * 保存用户身份证正面信息
     */
    public static String getSaveFrontIdCardInfoUrl() {
        return COMMONURI + "Customer/saveFrontIdCardInfo";
    }

    /**
     * 保存用户身份证反面信息
     */
    public static String getSaveBackIdCardInfoUrl() {
        return COMMONURI + "Customer/saveBackIdCardInfo";
    }

    /**
     * 得到用户的紧急联系人信息
     */
    public static String getExtroContactsUrl() {
        return COMMONURI + "Customer/getExtroContacts";
    }

    /**
     * 保存用户的紧急联系人信息
     */
    public static String getSaveExtroContactsUrl() {
        return COMMONURI + "Customer/saveExtroContacts";
    }

    /**
     * 得到用户个人信息
     */
    public static String getCustomerInfoUrl() {
        return COMMONURI + "Customer/getCustomerInfo";
    }

    /**
     * 保存用户个人信息
     */
    public static String getSaveCustomerInfoUrl() {
        return COMMONURI + "Customer/saveCustomerInfo";
    }

    /**
     * 得到省份
     */
    public static String getProvinceUrl() {
        return COMMONURI + "Customer/getProvince";
    }

    /**
     * 得到城市
     */
    public static String getCityUrl() {
        return COMMONURI + "Customer/getCity";
    }

    /**
     * 得到区域
     */
    public static String getCountyUrl() {
        return COMMONURI + "Customer/getCounty";
    }

    /**
     * 得到用户绑卡的信息
     */
    public static String getBankCardInfoUrl() {
        return COMMONURI + "UserCenter/getBankCardInfo";
    }

    /**
     * 得到流量超市
     */
    public static String getFlowSupermarketListURL() {
        return COMMONURI + "UserCenter/getFlowSupermarketList";
    }

    /**
     * 增加点击流量超市的点击量
     */
    public static String addFlowSupermarketClickCountURL() {
        return COMMONURI + "UserCenter/addFlowSupermarketClickCount";
    }

    /**
     * 得到订单确认信息
     */
    public static String getOrderConfirmUrl() {
        return COMMONURI + "Order/orderConfirm";
    }

    /**
     * 得到还款信息
     */
    public static String getRepayInfoUrl() {
        return COMMONURI + "Order/getRepayInfo";
    }

    /**
     * 还款点击我知道按钮调用
     */
    public static String getIKnowUrl() {
        return COMMONURI + "Order/iKnow";
    }

    /**
     * 手机贷借款返回
     */
    public static String getSJDLoanBackUrl() {
        return COMMONURI + "Order/sjdLoanBack";
    }

    /**
     * 得到公司信息
     */
    public static String getCompanyInformationUrl() {
        return COMMONURI + "Customer/companyInformation";
    }

    /**
     * 得到第三方验证码(掌众)
     */
    public static String getVerifySmsForConfirmLoanUrl() {
        return COMMONURI + "Order/ZhangZhongLoanCommit";
    }

    /**
     * 提交确认放款验证码(掌众)
     */
    public static String getSubmitVerifyCodeUrl() {
        return COMMONURI + "Order/ZhangZhongSubmitLoan";
    }

    /**
     * 得到第三方验证码还款的时候(掌众)
     */
    public static String getVerifySmsForRepaymentUrl() {
        return COMMONURI + "Order/ZhangZhongCommitRepayMentApply";
    }

    /**
     * 还款(掌众)
     */
    public static String getPayConfirmZhangzhongUrl() {
        return COMMONURI + "Order/ZhangZhongConfirmReturnMoney";
    }

    /**
     * 预下单订单信息
     */
    public static String getBaseLoanInfoUrl() {
        return COMMONURI + "Order/getBaseLoanInfo";
    }

    /**
     * 预下单入库
     */
    public static String getBaseLoanInfoApply() {
        return COMMONURI + "Order/baseLoanInfoApply";
    }

    /**
     * 手动刷新接口
     */
    public static String getManualRefreshURL() {
        return COMMONURI + "UserCenter/manualRefresh";
    }

    /**
     * 上传用户信息url
     *
     * @return
     */
    public static String getUploadUserInfoURL() {
        return COMMONURI + "Customer/savePhoneInfo";
    }

    /**
     * 用户认证步骤
     */
    public static String getAuthStepUrl() {
        return COMMONURI + "Customer/authStep";
    }

    /**
     * 获取邀请好友页面数据 url
     *
     * @return
     */
    public static String getInviteFriendsUrl() {
        return COMMONURI + "Invite/invite";
    }

    /**
     * 首页显示活动
     */
    public static String getActivityUrl() {
        return COMMONURI + "Invite/activity";
    }

    /**
     * 我的页面
     */
    public static String getMyHomeUrl() {
        return COMMONURI + "Invite/myHome";
    }

    /**
     * 我的红包
     */
    public static String getMyRedPacketUrl() {
        return COMMONURI + "Invite/myRedPacket";
    }

    /**
     * 红包提现
     */
    public static String getInviteWithDrawalsUrl() {
        return COMMONURI + "Invite/inviteWithdrawals";
    }

    /**
     * 协议URL
     *
     * @return
     */
    public static String getUserPayServerURL() {
        return HOST + "h5/protocol/server.html";
    }

    /**
     * 首页获取还款明细
     * Home/UserCenter/getRepaymentDetails
     */
    public static String getRepayDetailURL() {
        return COMMONURI + "UserCenter/getRepaymentDetails";
    }

    /**
     * 有盾 回调地址
     **/
    public static String getUDunNotifyURL() {
        return COMMONURI + "Udun/idAuthNotify";
    }

    /**
     * 获取差异化数据地址
     *
     * @return
     */
    public static String getDiffRateInfoURL() {
        return COMMONURI + "Order/diffOrderConfirm";
    }

    /**
     * 差异化订单确认提现
     *
     * @return
     */
    public static String getDiffOrderPayURL() {
        return COMMONURI + "Order/diffOrderPay";
    }

    /**
     * 通知服务器分享成功地址
     *
     * @return
     */
    public static String getUpdateShareCountURL() {
        return COMMONURI + "Message/updateShareCount";
    }

    /**
     * 通知服务器查看了信息的消息
     *
     * @return
     */
    public static String getUpdateMessageStatusURL() {
        return COMMONURI + "Message/updateMessageStatus";
    }

    /**
     * 得到消息中心的地址
     *
     * @return
     */
    public static String getMessageCenterURL() {
        return COMMONURI + "Message/getMessageList";
    }

    /**
     * 得到判断绑定了"向上"银行卡的地址
     *
     * @return
     */
    public static String getIsXiangShangURL() {
        return COMMONURI + "BindBank/IsXiangShang";
    }

    /**
     * 获取绑定向上资金端验证码
     *
     * @return
     */
    public static String getBindXiangShangVerifyCodeURL() {
        return COMMONURI + "BindBank/bindVerifyXsSms";
    }

    public static String getSubmitXiangShangBindInfoURL(){
        return COMMONURI + "BindBank/bindConfirmXiangShang";
    }
}
