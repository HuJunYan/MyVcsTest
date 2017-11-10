package com.tianshen.cash.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.tianshen.cash.R
import com.tianshen.cash.adapter.MyViewPagerAdapter
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.event.AuthCenterBackEvent
import com.tianshen.cash.event.RiskPreEvaluateFinishEvent
import com.tianshen.cash.fragment.AuthCenterMenuFragment
import com.tianshen.cash.model.AuthCenterMenuBean
import com.tianshen.cash.net.api.AuthCenterMenuService
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.CashAmountDialogUtils
import com.tianshen.cash.utils.TianShenUserUtil
import com.tianshen.cash.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_auth_center_menu.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONObject
import java.util.*

class AuthCenterMenuActivity : BaseActivity() {

    private var mFragmentList: ArrayList<Fragment> = ArrayList()
    private var mViewPagerAdapter: MyViewPagerAdapter? = null
    private var mCurrentIndex: Int = 0
    private var mAuthCenterMenuBean: AuthCenterMenuBean? = null

    override fun setContentView() = R.layout.activity_auth_center_menu

    override fun findViews() {
        tv_goto_auth.setOnClickListener {
            checkGoToOtherActivity()
        }

        vp_auth_center_menu.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mCurrentIndex = position
                refreshUI()
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
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().post(AuthCenterBackEvent())
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
                mAuthCenterMenuBean = data
                checkShowStep()
                checkShowDialog()
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })
    }

    /**
     * 判断当前在第几步
     */
    private fun checkShowStep() {

        val auth_id_num = mAuthCenterMenuBean?.data?.auth_id_num
        val auth_person_info = mAuthCenterMenuBean?.data?.auth_person_info
        val auth_credit = mAuthCenterMenuBean?.data?.auth_credit

        if ("0" == auth_id_num) {
            vp_auth_center_menu.setCurrentItem(0, false)
            refreshUI()
        } else if ("0" == auth_person_info) {
            vp_auth_center_menu.setCurrentItem(1, false)
        } else if ("0" == auth_credit) {
            vp_auth_center_menu.setCurrentItem(2, false)
        } else if ("1" == auth_id_num && "1" == auth_person_info && "1" == auth_credit) {
            vp_auth_center_menu.setCurrentItem(0, false)
            refreshUI()
        }

    }

    private fun checkShowDialog() {
        val auth_id_num = mAuthCenterMenuBean?.data?.auth_id_num
        val auth_person_info = mAuthCenterMenuBean?.data?.auth_person_info
        val auth_credit = mAuthCenterMenuBean?.data?.auth_credit
        val cash_amount = mAuthCenterMenuBean?.data?.cash_amount
        if ("1" == auth_id_num && "1" == auth_person_info && "1" == auth_credit && "0" == cash_amount) {
            CashAmountDialogUtils.show(mContext)
        }
    }

    /**
     * 跳转到子页面去认证
     */
    private fun checkGoToOtherActivity() {

        val auth_id_num = mAuthCenterMenuBean?.data?.auth_id_num
        val auth_person_info = mAuthCenterMenuBean?.data?.auth_person_info

        when (mCurrentIndex) {
            0 -> {
                gotoActivity(mContext, RiskPreAuthIdentityActivity::class.java, null)
            }
            1 -> {
                if ("0" == auth_id_num) {
                    ToastUtil.showToast(mContext, "请先身份认证")
                    return
                }
                val bundle = Bundle()
                bundle.putString(AuthMyInfoActivity.ACTIVITY_FLAG, AuthMyInfoActivity.PERSONFLAG)
                gotoActivity(mContext, AuthMyInfoActivity::class.java, bundle)
            }
            2 -> {
                if ("0" == auth_id_num) {
                    ToastUtil.showToast(mContext, "请先身份认证")
                    return
                }
                if ("0" == auth_person_info) {
                    ToastUtil.showToast(mContext, "请先个人信息认证")
                    return
                }
                val bundle = Bundle()
                bundle.putString(AuthMyInfoActivity.ACTIVITY_FLAG, AuthMyInfoActivity.CREDITFLAG)
                gotoActivity(mContext, AuthMyInfoActivity::class.java, bundle)
            }
        }
    }

    /**
     * 刷新UI
     */
    private fun refreshUI() {

        val fragmentStep1 = mFragmentList[0] as AuthCenterMenuFragment
        val fragmentStep2 = mFragmentList[1] as AuthCenterMenuFragment
        val fragmentStep3 = mFragmentList[2] as AuthCenterMenuFragment

        val auth_id_num = mAuthCenterMenuBean?.data?.auth_id_num
        val auth_person_info = mAuthCenterMenuBean?.data?.auth_person_info
        val auth_credit = mAuthCenterMenuBean?.data?.auth_credit

        when (mCurrentIndex) {
            0 -> {
                if ("0" == auth_id_num) {
                    iv_auth_center_step1.setImageResource(R.drawable.ic_auth_center_menu_selected_1)
                    tv_goto_auth.text = "进入认证"
                } else if ("1" == auth_id_num) {
                    iv_auth_center_step1.setImageResource(R.drawable.ic_auth_center_menu_selected_ok)
                    tv_goto_auth.text = "完善"
                }
                if ("0" == auth_person_info) {
                    iv_auth_center_step2.setImageResource(R.drawable.ic_auth_center_menu_unselected_2)

                } else if ("1" == auth_person_info) {
                    iv_auth_center_step2.setImageResource(R.drawable.ic_auth_center_menu_unselected_ok)
                }
                if ("0" == auth_credit) {
                    iv_auth_center_step3.setImageResource(R.drawable.ic_auth_center_menu_unselected_3)
                } else if ("1" == auth_credit) {
                    iv_auth_center_step3.setImageResource(R.drawable.ic_auth_center_menu_unselected_ok)
                }
            }
            1 -> {
                if ("0" == auth_id_num) {
                    iv_auth_center_step1.setImageResource(R.drawable.ic_auth_center_menu_unselected_1)
                } else if ("1" == auth_id_num) {
                    iv_auth_center_step1.setImageResource(R.drawable.ic_auth_center_menu_unselected_ok)
                }
                if ("0" == auth_person_info) {
                    iv_auth_center_step2.setImageResource(R.drawable.ic_auth_center_menu_selected_2)
                    tv_goto_auth.text = "进入认证"
                } else if ("1" == auth_person_info) {
                    iv_auth_center_step2.setImageResource(R.drawable.ic_auth_center_menu_selected_ok)
                    tv_goto_auth.text = "完善"
                }
                if ("0" == auth_credit) {
                    iv_auth_center_step3.setImageResource(R.drawable.ic_auth_center_menu_unselected_3)
                } else if ("1" == auth_credit) {
                    iv_auth_center_step3.setImageResource(R.drawable.ic_auth_center_menu_unselected_ok)
                }
            }
            2 -> {
                if ("0" == auth_id_num) {
                    iv_auth_center_step1.setImageResource(R.drawable.ic_auth_center_menu_unselected_1)
                } else if ("1" == auth_id_num) {
                    iv_auth_center_step1.setImageResource(R.drawable.ic_auth_center_menu_unselected_ok)
                }
                if ("0" == auth_person_info) {
                    iv_auth_center_step2.setImageResource(R.drawable.ic_auth_center_menu_unselected_2)
                } else if ("1" == auth_person_info) {
                    iv_auth_center_step2.setImageResource(R.drawable.ic_auth_center_menu_unselected_ok)
                }
                if ("0" == auth_credit) {
                    iv_auth_center_step3.setImageResource(R.drawable.ic_auth_center_menu_selected_3)
                    tv_goto_auth.text = "进入认证"
                } else if ("1" == auth_credit) {
                    iv_auth_center_step3.setImageResource(R.drawable.ic_auth_center_menu_selected_ok)
                    tv_goto_auth.text = "完善"
                }
            }
        }

        iv_auth_center_step1.visibility = View.VISIBLE
        iv_auth_center_step2.visibility = View.VISIBLE
        iv_auth_center_step3.visibility = View.VISIBLE

        if ("0" == auth_id_num) {
            fragmentStep1.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_1, "公安部联网认证")
        } else if ("1" == auth_id_num) {
            fragmentStep1.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_ok, "认证成功")
        }
        if ("0" == auth_person_info) {
            fragmentStep2.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_2, "填写真实信息，顺利通过认证")
        } else if ("1" == auth_person_info) {
            fragmentStep2.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_ok, "认证成功")
        }
        if ("0" == auth_credit) {
            fragmentStep3.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_3, "信用贷款，还有机会提升额度")
        } else if ("1" == auth_credit) {
            fragmentStep3.setPicAndTxt(R.drawable.ic_auth_center_menu_pic_ok, "认证成功")
        }

    }

    @Subscribe
    fun onRiskPreEvaluateFinishEvent(event: RiskPreEvaluateFinishEvent) {
        finish()
    }

}