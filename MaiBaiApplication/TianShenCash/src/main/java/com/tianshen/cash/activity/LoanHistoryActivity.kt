package com.tianshen.cash.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tianshen.cash.R
import com.tianshen.cash.adapter.LoanHistoryAdapter
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.WithdrawalsRecordBean
import com.tianshen.cash.model.WithdrawalsRecordItemBean
import com.tianshen.cash.net.api.GetWithdrawalsRecord
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.TianShenUserUtil
import kotlinx.android.synthetic.main.activity_loan_history.*
import org.json.JSONException
import org.json.JSONObject

class LoanHistoryActivity : BaseActivity() {

    private var mAdapter: LoanHistoryAdapter? = null
    private var withdrawalsRecordItemBeanList: MutableList<WithdrawalsRecordItemBean>? = null

//    private var mIsRefresh: Boolean = false

    override fun setContentView(): Int = R.layout.activity_loan_history

    override fun findViews() {
    }

    override fun setListensers() {
        tv_loan_history_back.setOnClickListener { backActivity() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initXRecyclerview()
        getBorrowBill(true)
    }


    /**
     * 初始化XRecyclerview
     */
    private fun initXRecyclerview() {


        mAdapter = LoanHistoryAdapter(mutableListOf<WithdrawalsRecordItemBean>(), {
        })
        refreshLayout.setOnRefreshListener { getBorrowBill(true) }
        refreshLayout.setOnLoadmoreListener { getBorrowBill(false) }
        refreshLayout.refreshHeader = ClassicsHeader(this) // header
        refreshLayout.refreshFooter = ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale) //footer
        xrecyclerview_loan_history.layoutManager = LinearLayoutManager(this@LoanHistoryActivity, LinearLayoutManager.VERTICAL, false)
        xrecyclerview_loan_history.adapter = mAdapter
    }


    private fun getBorrowBill(isRefresh: Boolean) {

        try {
            val jsonObject = JSONObject()
            val userId = TianShenUserUtil.getUserId(mContext)
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)

            var offset = ""
            if (isRefresh) {
                offset = "0"
            } else {
                offset = withdrawalsRecordItemBeanList?.size.toString()
            }

            jsonObject.put("offset", offset)
            jsonObject.put("length", GlobalParams.CONSUMPTIONRECORD_LOAD_LENGTH)
            val getWithdrawalsRecord = GetWithdrawalsRecord(mContext)
            getWithdrawalsRecord.getWithdrawalsBill(jsonObject, null, true, object : BaseNetCallBack<WithdrawalsRecordBean> {
                override fun onSuccess(paramT: WithdrawalsRecordBean) {

                    if (isRefresh) { //下拉刷新
                        withdrawalsRecordItemBeanList?.clear()
                        withdrawalsRecordItemBeanList = paramT.data.list
                        mAdapter?.setData(withdrawalsRecordItemBeanList!!)
                        refreshLayout.setLoadmoreFinished(false)
                    } else {//上拉加载更多
                        withdrawalsRecordItemBeanList?.addAll(paramT.data.list)
                        mAdapter?.setData(withdrawalsRecordItemBeanList!!)
                    }
                    mAdapter?.notifyDataSetChanged()
                    if (isRefresh) {
                        refreshLayout.finishRefresh()
                    } else {
                        refreshLayout.finishLoadmore()
                        if (withdrawalsRecordItemBeanList?.size == paramT.data.total) {
//                            xrecyclerview_loan_history.setNoMore(true)
                            refreshLayout.setLoadmoreFinished(true)
                        }
                    }
                }

                override fun onFailure(url: String, errorType: Int, errorCode: Int) {
                }
            })
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

}