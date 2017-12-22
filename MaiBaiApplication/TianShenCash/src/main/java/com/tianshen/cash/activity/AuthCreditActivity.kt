package com.tianshen.cash.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.tianshen.cash.R
import com.tianshen.cash.adapter.AuthNotRequiredAdapter
import com.tianshen.cash.adapter.AuthRequiredAdapter
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.AuthCreditBean
import com.tianshen.cash.net.api.GetCreditConf
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.ImageLoader
import com.tianshen.cash.utils.TianShenUserUtil
import com.tianshen.cash.view.CustomerItemDecoration
import kotlinx.android.synthetic.main.activity_auth_credit.*
import org.json.JSONObject

class AuthCreditActivity : BaseActivity() {

    var mAuthCreditBean: AuthCreditBean? = null
    var mAuthRequiredAdapter: AuthRequiredAdapter? = null
    var mAuthNotRequiredAdapter: AuthNotRequiredAdapter? = null

    override fun setContentView(): Int = R.layout.activity_auth_credit

    override fun findViews() {
    }

    override fun setListensers() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        val jsonObject = JSONObject()
        val userId = TianShenUserUtil.getUserId(mContext)
        jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        val getCreditConf = GetCreditConf(mContext)
        getCreditConf.getData(jsonObject, object : BaseNetCallBack<AuthCreditBean> {

            override fun onSuccess(data: AuthCreditBean?) {
                mAuthCreditBean = data
                refreshUI()
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })
    }

    private fun refreshUI() {
        ImageLoader.load(mContext, mAuthCreditBean!!.data!!.required_background, iv_auth_credit_required)
        tv_auth_credit_required.text = mAuthCreditBean!!.data!!.required_content

        ImageLoader.load(mContext, mAuthCreditBean!!.data!!.not_required_background, iv_auth_credit_not_required)
        tv_auth_credit_not_required.text = mAuthCreditBean!!.data!!.not_required_content

        mAuthRequiredAdapter = AuthRequiredAdapter(mAuthCreditBean!!.data.required, {

        })
        recyclerview_credit_required.layoutManager = LinearLayoutManager(this@AuthCreditActivity, LinearLayoutManager.VERTICAL, false)
        recyclerview_credit_required.adapter = mAuthRequiredAdapter
        recyclerview_credit_required.addItemDecoration(CustomerItemDecoration(this))


        mAuthNotRequiredAdapter = AuthNotRequiredAdapter(mAuthCreditBean!!.data.not_required, {

        })
        recyclerview_credit_not_required.layoutManager = LinearLayoutManager(this@AuthCreditActivity, LinearLayoutManager.VERTICAL, false)
        recyclerview_credit_not_required.adapter = mAuthNotRequiredAdapter
        recyclerview_credit_not_required.addItemDecoration(CustomerItemDecoration(this))
    }


}