package com.tianshen.cash.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tianshen.cash.R
import com.tianshen.cash.adapter.RedPackageAdapter
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.GetBankListBean
import com.tianshen.cash.model.RedPackageBean
import com.tianshen.cash.model.WithDrawalsListBean
import com.tianshen.cash.net.api.GetBindBankList
import com.tianshen.cash.net.api.GetRedPackage
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.TianShenUserUtil
import com.tianshen.cash.utils.ToastUtil
import com.tianshen.cash.utils.Utils
import kotlinx.android.synthetic.main.activity_red_package.*
import kotlinx.android.synthetic.main.dialog_bind_bank_card.view.*
import org.json.JSONObject


class RedPackageActivity : BaseActivity() {

    private var mAdapter: RedPackageAdapter? = null

    override fun setContentView(): Int = R.layout.activity_red_package

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun findViews() {
    }

    override fun setListensers() {

        tv_red_package_back.setOnClickListener {
            backActivity()
        }

        tv_get_red_package.setOnClickListener {
            initMyBankCardData()
        }

    }

    /**
     * 得到数据
     */
    private fun initData() {
        var getRedPackage = GetRedPackage(mContext)
        var jsonobject = JSONObject()
        var userId = TianShenUserUtil.getUserId(mContext)
        jsonobject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        getRedPackage.redPackage(jsonobject, null, true, object : BaseNetCallBack<RedPackageBean> {
            override fun onSuccess(paramT: RedPackageBean?) {
                refreshUI(paramT)
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }
        })
    }

    /**
     * 刷新UI
     */
    private fun refreshUI(bean: RedPackageBean?) {

        var data = bean?.Data();
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
     * 获取银行卡信息
     */
    private fun initMyBankCardData() {
        var getBindBankList = GetBindBankList(mContext)
        var jsonobject = JSONObject()
        var userId = TianShenUserUtil.getUserId(mContext)
        jsonobject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        getBindBankList.getBindBankList(jsonobject, null, true, object : BaseNetCallBack<GetBankListBean> {
            override fun onSuccess(paramT: GetBankListBean?) {
                var size = paramT?.data?.size
                if (0 == size) {
                    showUnBindBankCardDialog()
                } else {
                    ToastUtil.showToast(mContext, "去提现!")
                }
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })

    }

    /**
     * 显示提示绑定银行卡dialog
     */
    private fun showUnBindBankCardDialog() {

        val mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = mLayoutInflater.inflate(R.layout.dialog_bind_bank_card, null, false)
        val dialog = Dialog(mContext, R.style.MyDialog)
        val screenWidth = Utils.getWidthPixels(mContext)
        val screenHeight = Utils.getHeightPixels(mContext)
        dialog.setContentView(view, ViewGroup.LayoutParams(screenWidth * 8 / 9, screenHeight * 1 / 3))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        view.tv_dialog_bind_bank_card_ok.setOnClickListener {
            gotoActivity(mContext, AuthBankCardActivity::class.java, null)
            dialog.dismiss()
        }

        view.tv_dialog_bind_bank_card_cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}
