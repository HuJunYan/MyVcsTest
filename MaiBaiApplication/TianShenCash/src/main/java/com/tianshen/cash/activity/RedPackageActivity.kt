package com.tianshen.cash.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.tianshen.cash.R
import com.tianshen.cash.adapter.RedPackageAdapter
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.event.GetRedPackageEvent
import com.tianshen.cash.model.*
import com.tianshen.cash.net.api.*
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.*
import kotlinx.android.synthetic.main.activity_red_package.*
import kotlinx.android.synthetic.main.dialog_red_package_verify_code.view.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject


class RedPackageActivity : BaseActivity() {

    private var mRedPackageBean: RedPackageBean? = null
    private var mAdapter: RedPackageAdapter? = null
    private var mIsAuthOK: Boolean = false

    private var tv_dialog_get_verify_code: TextView? = null

    private var mStartTime = 59

    private val MSG_SEVERITY_TIME = 1
    private val MSG_SEVERITY_DELAYED = 1 * 1000

    private val mHandler = object : Handler() {
        override fun handleMessage(message: Message) {
            when (message.what) {
                MSG_SEVERITY_TIME -> refreshSeverityTextUI()
            }
        }
    }

    override fun setContentView(): Int = R.layout.activity_red_package

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRedPackageData()
        initAuthStepData()
    }

    override fun findViews() {
    }

    override fun setListensers() {

        tv_red_package_back.setOnClickListener {
            backActivity()
        }

        tv_get_red_package.setOnClickListener {
            if (mIsAuthOK) {
                initMyBankCardData()
            } else {
                showAuthDialog()
            }
        }

    }

    /**
     * 得到页面数据
     */
    private fun initRedPackageData() {
        var getRedPackage = GetRedPackage(mContext)
        var jsonobject = JSONObject()
        var userId = TianShenUserUtil.getUserId(mContext)
        jsonobject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        getRedPackage.redPackage(jsonobject, null, true, object : BaseNetCallBack<RedPackageBean> {
            override fun onSuccess(paramT: RedPackageBean?) {
                mRedPackageBean = paramT
                refreshUI()
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }
        })
    }

    /**
     * 获取认证步骤信息
     */
    private fun initAuthStepData() {
        var getAuthStep = GetAuthStep(mContext)
        var jsonobject = JSONObject()
        var userId = TianShenUserUtil.getUserId(mContext)
        jsonobject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        getAuthStep.authStep(jsonobject, null, true, object : BaseNetCallBack<AuthStepBean> {
            override fun onSuccess(paramT: AuthStepBean?) {
                if (paramT == null) {
                    return
                }
                if (paramT.data?.all_step.equals(paramT.data?.current_step)) {
                    mIsAuthOK = true
                }
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {
            }

        })
    }

    /**
     * 获取银行卡信息
     */
    private fun initMyBankCardData() {
        var getBindBankList = GetBindBankList(mContext)
        var jsonobject = JSONObject()
        var userId = TianShenUserUtil.getUserId(mContext)
        jsonobject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        getBindBankList.getBindBankList(jsonobject, null, true, object : BaseNetCallBack<GetBankListBean> {
            override fun onSuccess(paramT: GetBankListBean?) {
                if (paramT == null || paramT.data == null || paramT.data.size == 0) {
                    ToastUtil.showToast(mContext, "获取银行卡信息失败")
                    return
                }
                showGetMoneyDialog(paramT)
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })

    }

    /**
     * 刷新UI
     */
    private fun refreshUI() {

        var data = mRedPackageBean?.data

        var withdrawals_money = MoneyUtils.changeF2Y(data?.withdrawals_money, 2)
        var all_income = MoneyUtils.changeF2Y(data?.all_income, 2)
        var already_withdrawals_money = MoneyUtils.changeF2Y(data?.already_withdrawals_money, 2)
        var min_withdrawals = MoneyUtils.changeF2Y(data?.min_withdrawals, 2)

        //设置提现按钮是否可以点击
        tv_get_red_package.isEnabled = withdrawals_money > min_withdrawals

        tv_withdrawals_money.text = withdrawals_money
        tv_all_income.text = all_income
        tv_already_withdrawals_money.text = already_withdrawals_money
        tv_min_withdrawals.text = "满" + min_withdrawals + "可提现"

        var withdrawals_list = data?.withdrawals_list
        if (withdrawals_list != null) {
            initRecyclerview(withdrawals_list)
        }
    }


    private fun initRecyclerview(data: MutableList<WithDrawalsListBean>) {
        //增加footer
        if (data.size > 0) {
            data.add(WithDrawalsListBean())
        }

        if (mAdapter == null) {
            xrecyclerview_red_package.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            xrecyclerview_red_package.setPullRefreshEnabled(false)
            xrecyclerview_red_package.setLoadingMoreEnabled(false)
            mAdapter = RedPackageAdapter(data, {
            })
            xrecyclerview_red_package.adapter = mAdapter
        } else {
            mAdapter?.setData(data)
            mAdapter?.notifyDataSetChanged()
        }
    }

    /**
     * 显示提示认证的dialog
     */
    private fun showAuthDialog() {
        MaterialDialog.Builder(mContext)
                .content("如需提现，请先完成全部认证")
                .positiveText("去认证")
                .negativeText("取消")
                .onPositive { _, _ ->
                    gotoActivity(mContext, AuthCenterActivity::class.java, null)
                }
                .onNegative { _, _ ->
                }
                .show()
    }

    /**
     * 显示提现Dialog
     */
    private fun showGetMoneyDialog(bankListBean: GetBankListBean) {

        var bankName = bankListBean.data.get(0).bank_name
        var bankNun = bankListBean.data.get(0).card_num
        var bankNumEnd = StringUtil.getEndBankCard(bankNun)

        val mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = mLayoutInflater.inflate(R.layout.dialog_red_package_verify_code, null, false)
        var mVerifyCodeDialog = Dialog(mContext, R.style.MyDialog)
        var screenWidth = Utils.getWidthPixels(mContext);
        mVerifyCodeDialog.setContentView(view, ViewGroup.LayoutParams(screenWidth * 8 / 9, ViewGroup.LayoutParams.WRAP_CONTENT))
        mVerifyCodeDialog.setCanceledOnTouchOutside(false)
        mVerifyCodeDialog.setCancelable(true)

        view.tv_dialog_red_package_bank.text = "到账银行卡 $bankName($bankNumEnd)"

        if (!TextUtils.isEmpty(mRedPackageBean?.data?.withdrawals_money)) {
            var moneyInt = mRedPackageBean?.data?.withdrawals_money?.toInt()
            if (moneyInt!! > 100000) { //判断当前的提现金额是否大于1000，如果大于1000界面置为1000
                moneyInt = 100000
            }
            var moneyY = MoneyUtils.changeF2Y(moneyInt.toString(), 2)
            view.et_get_money.setText(moneyY)
        }

        tv_dialog_get_verify_code = view.tv_dialog_get_verify_code
        tv_dialog_get_verify_code?.setOnClickListener {
            refreshSeverityTextUI()
            getVerifyCode()
        }

        view.tv_dialog_ok.setOnClickListener {
            var verify_code = view.et_dialog_verify_code.text.toString().trim()
            getMoney(verify_code, mRedPackageBean?.data?.withdrawals_money!!)
        }

        view.tv_dialog_cancel.setOnClickListener {
            mVerifyCodeDialog.dismiss()
        }

        mVerifyCodeDialog.show()

    }

    /**
     * 刷新验证码
     */
    private fun refreshSeverityTextUI() {
        if (isFinishing) {
            return
        }

        tv_dialog_get_verify_code?.text = mStartTime.toString()

        mStartTime--
        if (mStartTime == 0) {
            tv_dialog_get_verify_code?.text = "重获取验证码"
            mStartTime = 59
            tv_dialog_get_verify_code?.isEnabled = true
            mHandler.removeMessages(MSG_SEVERITY_TIME)
        } else {
            tv_dialog_get_verify_code?.isEnabled = false
            mHandler.sendEmptyMessageDelayed(MSG_SEVERITY_TIME, MSG_SEVERITY_DELAYED.toLong())
        }
    }

    /**
     * 得到验证码
     */
    private fun getVerifyCode() {
        var getVerifyCode = GetVerifyCode(mContext)
        var jsonObject = JSONObject()
        var userId = TianShenUserUtil.getUserId(mContext)
        var mobile = TianShenUserUtil.getUserPhoneNum(mContext)
        jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        jsonObject.put("mobile", mobile)
        jsonObject.put("type", "7")
        getVerifyCode.getVerifyCode(jsonObject, null, true, object : BaseNetCallBack<VerifyCodeBean> {
            override fun onSuccess(paramT: VerifyCodeBean?) {
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {
            }

        })
    }

    /**
     * 红包提现
     */
    private fun getMoney(verify_code: String, withdrawals_money: String) {
        var getInviteWithDrawals = GetInviteWithDrawals(mContext)
        var jsonObject = JSONObject()
        var userId = TianShenUserUtil.getUserId(mContext)
        jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        jsonObject.put("verify_code", verify_code)
        jsonObject.put("withdrawals_money", withdrawals_money)
        getInviteWithDrawals.inviteWithDrawals(jsonObject, null, true, object : BaseNetCallBack<PostDataBean> {
            override fun onSuccess(paramT: PostDataBean?) {
                initRedPackageData()
                EventBus.getDefault().post(GetRedPackageEvent())
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })
    }


}
