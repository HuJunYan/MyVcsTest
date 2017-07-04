package com.tianshen.cash.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.github.ui.adapter.MyBankCardAdapter
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.model.GetBankListBean
import com.tianshen.cash.net.api.GetBindBankList
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.LogUtil
import com.tianshen.cash.utils.TianShenUserUtil
import kotlinx.android.synthetic.main.activity_my_bank_card2.*
import org.json.JSONObject

class MyBankCardActivity2 : BaseActivity() {

    private var mAdapter: MyBankCardAdapter? = null


    override fun onResume() {
        super.onResume()
        initMyBankCardData()
    }

    override fun setContentView() = R.layout.activity_my_bank_card2

    override fun findViews() {
    }

    override fun setListensers() {
        //点击了返回
        tv_my_bank_card_back.setOnClickListener {
            backActivity()
        }

        //点击了添加银行卡
        ll_add_bank_card.setOnClickListener {
            gotoActivity(mContext, AuthBankCardActivity::class.java, null)
        }

        //点击了修改绑定银行卡
        ll_edit_bank_card.setOnClickListener {
            gotoActivity(mContext, AuthBankCardActivity::class.java, null)
        }
    }

    /**
     * 获取银行卡信息
     */
    private fun initMyBankCardData() {
        var getBindBankList = GetBindBankList(mContext)
        var jsonobject = JSONObject()
        var userId = TianShenUserUtil.getUserId(mContext)
        jsonobject.put("customer_id", userId)
        getBindBankList.getBindBankList(jsonobject, null, true, object : BaseNetCallBack<GetBankListBean> {
            override fun onSuccess(paramT: GetBankListBean?) {
                var size = paramT?.data?.size
                if (0 == size) {
                    showAddBankCardUI()
                } else {
                    showMyBankCardUI(paramT)
                }
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })

    }

    /**
     * 显示添加银行卡Ui
     */
    private fun showAddBankCardUI() {
        ll_add_bank_card.visibility = View.VISIBLE
        xrecyclerview_my_bank_card.visibility = View.GONE
        ll_edit_bank_card.visibility = View.GONE
    }

    /**
     * 显示我的银行卡UI
     */
    private fun showMyBankCardUI(paramT: GetBankListBean?) {

        ll_add_bank_card.visibility = View.GONE
        xrecyclerview_my_bank_card.visibility = View.VISIBLE
        ll_edit_bank_card.visibility = View.VISIBLE

        if (mAdapter == null) {
            xrecyclerview_my_bank_card.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            xrecyclerview_my_bank_card.setPullRefreshEnabled(false)
            xrecyclerview_my_bank_card.setLoadingMoreEnabled(false)
            mAdapter = MyBankCardAdapter(paramT?.data!!, {
                LogUtil.d("abc", "点击了--->" + it.bank_name)
            })
            xrecyclerview_my_bank_card.adapter = mAdapter
        } else {
            mAdapter?.setData(paramT?.data!!)
            mAdapter?.notifyDataSetChanged()
        }


    }

}
