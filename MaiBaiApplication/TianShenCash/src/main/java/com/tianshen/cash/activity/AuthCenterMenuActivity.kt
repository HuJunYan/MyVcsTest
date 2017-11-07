package com.tianshen.cash.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tianshen.cash.R
import com.tianshen.cash.adapter.MyViewPagerAdapter
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.fragment.AuthCenterMenuFragment
import com.tianshen.cash.model.AuthCenterMenuBean
import com.tianshen.cash.net.api.AuthCenterMenuService
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.LogUtil
import com.tianshen.cash.utils.TianShenUserUtil
import kotlinx.android.synthetic.main.activity_auth_center_menu.*
import org.json.JSONObject
import java.util.*

/**
 * Created by cuiyue on 2017/11/6.
 */
class AuthCenterMenuActivity : BaseActivity() {

    private var mFragmentList: ArrayList<Fragment> = ArrayList()
    private var mViewPagerAdapter: MyViewPagerAdapter? = null

    override fun setContentView() = R.layout.activity_auth_center_menu

    override fun findViews() {
    }

    override fun setListensers() {
        tv_auth_center_menu_back.setOnClickListener { backActivity() }
        tv_identity.setOnClickListener {
            gotoActivity(mContext, RiskPreAuthIdentityActivity::class.java, null)
        }
        tv_person_info.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AuthMyInfoActivity.ACTIVITY_FLAG, AuthMyInfoActivity.PERSONFLAG)
            gotoActivity(mContext, AuthMyInfoActivity::class.java, bundle)
        }
        tv_credit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AuthMyInfoActivity.ACTIVITY_FLAG, AuthMyInfoActivity.CREDITFLAG)
            gotoActivity(mContext, AuthMyInfoActivity::class.java, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        initFragment()
        initViewPager()

    }

    private fun initFragment() {
        for (i in 0..2) {
            val fragment = AuthCenterMenuFragment()
            mFragmentList.add(fragment)
        }
    }

    private fun initViewPager() {
        mViewPagerAdapter = MyViewPagerAdapter(supportFragmentManager, mFragmentList)
        vp_auth_center_menu.setAdapter(mViewPagerAdapter)
        vp_auth_center_menu.setOffscreenPageLimit(mFragmentList.size)
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
                refreshUI(data)
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })
    }

    private fun refreshUI(data: AuthCenterMenuBean?) {
        var fragmentStep1 = mFragmentList[0] as AuthCenterMenuFragment
        var fragmentStep2 = mFragmentList[1] as AuthCenterMenuFragment
        var fragmentStep3 = mFragmentList[2] as AuthCenterMenuFragment

        fragmentStep1.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_1, "测试文字2222")
        fragmentStep2.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_2, "测试文字3333")
        fragmentStep3.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_3, "测试文字4444")
    }

}