package com.tianshen.cash.activity

import android.os.Handler
import android.os.Message
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.BankCardInfoBean
import com.tianshen.cash.net.api.GetBankCardInfo
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.TianShenUserUtil
import kotlinx.android.synthetic.main.activity_bind_bank_card_confirm.*
import org.json.JSONObject

class BindBankCardConfirmActivity : BaseActivity() {
    var mData: BankCardInfoBean? = null
    private var mStartTime = 59

    private val MSG_SEVERITY_TIME = 1
    private val MSG_SEVERITY_DELAYED = 1 * 1000

    private val mHandler = object : Handler() {
        override fun handleMessage(message: Message) {
            when (message.what) {
                MSG_SEVERITY_TIME -> refreshSeverityTextUI()
            }
        }
    }

    override fun setContentView() = R.layout.activity_bind_bank_card_confirm

    override fun findViews() {
        initData();

    }

    override fun setListensers() {
        tv_auth_bank_card_back.setOnClickListener { backActivity() }
        tv_severity_code.setOnClickListener { getVerifyCode() }
        tv_auth_info_post.setOnClickListener { postConfirmBindCardInfo() }
    }

    /**
     * 请求确认帮卡信息接口
     */
    private fun postConfirmBindCardInfo() {

    }

    /**
     * 获取验证码
     */
    private fun getVerifyCode() {
        refreshSeverityTextUI()
    }


    private fun initData() {
        val jsonOjbect = JSONObject();
        val userId = TianShenUserUtil.getUserId(mContext);
        jsonOjbect.put(GlobalParams.USER_CUSTOMER_ID, userId)
        val getBankCardInfo = GetBankCardInfo(mContext);
        getBankCardInfo.getBankCardInfo(jsonOjbect, object : BaseNetCallBack<BankCardInfoBean> {

            override fun onSuccess(data: BankCardInfoBean?) {
                mData = data;
                refreshUI();
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })
    }

    private fun refreshUI() {
        val data = mData?.data;
        if (data != null) {
            tv_confirm_auth_bank_card_person.setText(data.card_user_name)
            tv_bank_card.setText(data.bank_name)
            tv_bank_province.setText(data.province_name)
            tv_bank_city.setText(data.city_name)
            tv_auth_card_num.setText(data.card_num)
            tv_bank_card_phone_num.setText(data.reserved_mobile)
        }
    }

    /**
     * 刷新验证码UI
     */
    private fun refreshSeverityTextUI() {

        if (isFinishing) {
            return
        }

        tv_severity_code.text = mStartTime.toString()
        mStartTime--
        if (mStartTime == 0) {
            tv_severity_code.text = "重获取验证码"
            mStartTime = 59
            tv_severity_code.isEnabled = true
            mHandler.removeMessages(MSG_SEVERITY_TIME)
        } else {
            tv_severity_code.isEnabled = false
            mHandler.sendEmptyMessageDelayed(MSG_SEVERITY_TIME, MSG_SEVERITY_DELAYED.toLong())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

}
