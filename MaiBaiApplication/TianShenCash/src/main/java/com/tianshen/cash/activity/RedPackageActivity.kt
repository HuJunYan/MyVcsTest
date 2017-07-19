package com.tianshen.cash.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.tianshen.cash.R
import com.tianshen.cash.adapter.RedPackageAdapter
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.AuthStepBean
import com.tianshen.cash.model.GetBankListBean
import com.tianshen.cash.model.RedPackageBean
import com.tianshen.cash.model.RedPackageBean.Data
import com.tianshen.cash.model.WithDrawalsListBean
import com.tianshen.cash.net.api.GetAuthStep
import com.tianshen.cash.net.api.GetBindBankList
import com.tianshen.cash.net.api.GetRedPackage
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.*
import kotlinx.android.synthetic.main.activity_red_package.*
import kotlinx.android.synthetic.main.dialog_red_package_verify_code.view.*
import org.json.JSONObject


class RedPackageActivity : BaseActivity() {

    private var mRedPackageBean: RedPackageBean? = null
    private var mAdapter: RedPackageAdapter? = null
    private var mIsAuthOK: Boolean = false


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

        var data = mRedPackageBean?.Data();
        tv_withdrawals_money.text = data?.withdrawals_money
        tv_all_income.text = data?.all_income
        tv_already_withdrawals_money.text = data?.already_withdrawals_money
        tv_min_withdrawals.text = "满" + data?.already_withdrawals_money + "可提现"

        var withdrawals_list = data?.withdrawals_list
        if (withdrawals_list != null) {
            initRecyclerview(withdrawals_list)
        }
    }


    private fun initRecyclerview(data: MutableList<WithDrawalsListBean>) {
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

        view.tv_dialog_red_package_bank.text = "到账银行卡 " + bankName + "(" + "$bankNumEnd)"

        if (!TextUtils.isEmpty(mRedPackageBean?.data?.withdrawals_money)) {
            var moneyY = MoneyUtils.changeF2Y(mRedPackageBean?.data?.withdrawals_money).toInt()
            if (moneyY > 1000) {
                moneyY = 1000
            }
            view.et_get_money.setText(moneyY.toString())
        }

        mVerifyCodeDialog.show()

    }

}
