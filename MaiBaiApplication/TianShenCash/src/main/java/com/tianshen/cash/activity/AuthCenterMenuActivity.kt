package com.tianshen.cash.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
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

class AuthCenterMenuActivity : BaseActivity() {

    private var mFragmentList: ArrayList<Fragment> = ArrayList()
    private var mViewPagerAdapter: MyViewPagerAdapter? = null
    private var mCurrentIndex: Int = 0

    override fun setContentView() = R.layout.activity_auth_center_menu

    override fun findViews() {
        tv_goto_auth.setOnClickListener {
            when (mCurrentIndex) {
                0 -> {
                    gotoActivity(mContext, RiskPreAuthIdentityActivity::class.java, null)
                }
                1 -> {
                    val bundle = Bundle()
                    bundle.putString(AuthMyInfoActivity.ACTIVITY_FLAG, AuthMyInfoActivity.PERSONFLAG)
                    gotoActivity(mContext, AuthMyInfoActivity::class.java, bundle)
                }
                2 -> {
                    val bundle = Bundle()
                    bundle.putString(AuthMyInfoActivity.ACTIVITY_FLAG, AuthMyInfoActivity.CREDITFLAG)
                    gotoActivity(mContext, AuthMyInfoActivity::class.java, bundle)
                }
            }
        }

        vp_auth_center_menu.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mCurrentIndex = position
            }

        })
    }

    override fun setListensers() {
        tv_auth_center_menu_back.setOnClickListener { backActivity() }
        rl_identity.setOnClickListener {
            vp_auth_center_menu.currentItem = 0
            mCurrentIndex = 0
        }
        rl_person_info.setOnClickListener {
            vp_auth_center_menu.currentItem = 1
            mCurrentIndex = 1
        }
        rl_credit.setOnClickListener {
            vp_auth_center_menu.currentItem = 2
            mCurrentIndex = 2
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
        vp_auth_center_menu.adapter = mViewPagerAdapter
        vp_auth_center_menu.offscreenPageLimit = mFragmentList.size
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

        fragmentStep1.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_1, "公安部联网认证")
        fragmentStep2.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_2, "填写真实信息，顺利通过认证")
        fragmentStep3.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_3, "信用贷款，还有机会提升额度")
    }

}