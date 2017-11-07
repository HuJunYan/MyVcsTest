package com.tianshen.cash.activity

import android.os.Bundle
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.AuthCenterMenuBean
import com.tianshen.cash.model.ConstantBean
import com.tianshen.cash.net.api.AuthCenterMenuService
import com.tianshen.cash.net.api.CustomerInfoService
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.LogUtil
import com.tianshen.cash.utils.TianShenUserUtil
import kotlinx.android.synthetic.main.activity_auth_center_menu.*
import org.json.JSONObject

/**
 * Created by cuiyue on 2017/11/6.
 */
class AuthCenterMenuActivity : BaseActivity() {

    override fun setContentView() = R.layout.activity_auth_center_menu

    override fun findViews() {
    }

    override fun setListensers() {
        tv_auth_center_menu_back.setOnClickListener { backActivity() }
        tv_identity.setOnClickListener {
            gotoActivity(mContext, RiskPreAuthIdentityActivity::class.java, null)
        }
        tv_person_info.setOnClickListener {
            gotoActivity(mContext, AuthMyInfoActivity::class.java, null)
        }
        tv_credit.setOnClickListener {
            gotoActivity(mContext, AuthMyInfoActivity::class.java, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        testCustomerInfo()
    }

    private fun initData() {
        val jsonObject = JSONObject()
        val userId = TianShenUserUtil.getUserId(mContext)
        jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        val authCenterMenu = AuthCenterMenuService(mContext)
        authCenterMenu.getData(jsonObject, object : BaseNetCallBack<AuthCenterMenuBean> {

            override fun onSuccess(data: AuthCenterMenuBean?) {
                LogUtil.d("abc", "onSuccess--->" + data?.msg)
                LogUtil.d("abc", "one--->" + data?.data?.auth_id_num)
                LogUtil.d("abc", "two--->" + data?.data?.auth_person_info)
                LogUtil.d("abc", "two--->" + data?.data?.auth_credit)

            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })
    }

    private fun testCustomerInfo() {
        val jsonObject = JSONObject()
        val userId = TianShenUserUtil.getUserId(mContext)
        jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        val customerInfo = CustomerInfoService(mContext)
        customerInfo.getData(jsonObject, object : BaseNetCallBack<ConstantBean> {

            override fun onSuccess(data: ConstantBean?) {
                LogUtil.d("abc", "onSuccess--->" + data?.code)
                LogUtil.d("abc", "user_address_city--->" + data?.data?.user_address_city)
                LogUtil.d("abc", "company_name--->" + data?.data?.company_name)

            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })
    }


}