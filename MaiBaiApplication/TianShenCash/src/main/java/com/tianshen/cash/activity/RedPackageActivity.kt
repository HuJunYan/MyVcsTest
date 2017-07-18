package com.tianshen.cash.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.ui.adapter.RedPackageAdapter
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.GetBankListBean
import com.tianshen.cash.model.RedPackageHistoryBean
import com.tianshen.cash.net.api.GetBindBankList
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
     * 模拟一些数据
     */
    private fun initData() {
        var bean1 = RedPackageHistoryBean()
        bean1.money = "500"
        bean1.is_withdrawals = "1"
        bean1.time = "2017/05/03 10:00"
        bean1.title = "提现"
        bean1.type = "0"

        var bean2 = RedPackageHistoryBean()
        bean2.money = "10"
        bean2.is_withdrawals = "0"
        bean2.time = "2017/05/03 10:00"
        bean2.title = "邀请注册-13123123123"
        bean2.type = "1"

        var bean3 = RedPackageHistoryBean()
        bean3.money = "20"
        bean3.is_withdrawals = "0"
        bean3.time = "2017/05/03 10:00"
        bean3.title = "邀请注册-13123123123"
        bean3.type = "1"
        var data = mutableListOf<RedPackageHistoryBean>(bean1, bean2, bean3)
        initRecyclerview(data)
    }

    private fun initRecyclerview(data: MutableList<RedPackageHistoryBean>) {
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
