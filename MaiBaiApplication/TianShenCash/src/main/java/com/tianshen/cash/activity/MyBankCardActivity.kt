package com.tianshen.cash.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.github.ui.adapter.MyBankCardAdapter
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.GetBankListBean
import com.tianshen.cash.model.UserAuthCenterBean
import com.tianshen.cash.net.api.GetBindBankList
import com.tianshen.cash.net.api.GetUserAuthCenter
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.LogUtil
import com.tianshen.cash.utils.TianShenUserUtil
import com.tianshen.cash.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_my_bank_card.*
import org.json.JSONException
import org.json.JSONObject

class MyBankCardActivity : BaseActivity() {

    private var mAdapter: MyBankCardAdapter? = null
    private var identityStatus = 0

    override fun onResume() {
        super.onResume()
        initMyBankCardData()
    }

    override fun setContentView() = R.layout.activity_my_bank_card

    override fun findViews() {
    }

    override fun setListensers() {
        //点击了返回
        tv_my_bank_card_back.setOnClickListener {
            backActivity()
        }

        //点击了添加银行卡
        ll_add_bank_card.setOnClickListener {
            if (0 == identityStatus) {
                ToastUtil.showToast(mContext, "请先进行身份认证")
            } else {
                val bundle = Bundle()
                bundle.putInt(GlobalParams.BANK_CARD_FROM_KEY, 1)
                gotoActivity(mContext, AuthBankCardActivity::class.java, bundle)
            }
        }

        //点击了修改绑定银行卡
        ll_edit_bank_card.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(GlobalParams.BANK_CARD_FROM_KEY, 2)
            gotoActivity(mContext, AuthBankCardActivity::class.java, bundle)
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
                    showAddBankCardUI()
                    initAuthCenterData()
                } else {
                    showMyBankCardUI(paramT)
                }
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })

    }

    /**
     * 得到用户认证信息
     */
    private fun initAuthCenterData() {
        try {
            val jsonObject = JSONObject()
            val userId = TianShenUserUtil.getUserId(mContext)
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
            val getUserAuthCenter = GetUserAuthCenter(mContext)
            getUserAuthCenter.userAuthCenter(jsonObject, null, true, object : BaseNetCallBack<UserAuthCenterBean> {
                override fun onSuccess(paramT: UserAuthCenterBean) {
                    if ("0".equals(paramT.data.id_num) || "0".equals(paramT.data.face_pass)) {//判断身份认证和扫脸都成功没。如果有一个失败就算身份认证失败
                        identityStatus = 0
                    } else {
                        identityStatus = 1
                    }
                }

                override fun onFailure(url: String, errorType: Int, errorCode: Int) {}
            })

        } catch (e: JSONException) {
            e.printStackTrace()
        }

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
