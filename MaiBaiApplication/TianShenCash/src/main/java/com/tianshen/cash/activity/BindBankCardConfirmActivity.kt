package com.tianshen.cash.activity

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
    override fun setContentView() = R.layout.activity_bind_bank_card_confirm

    override fun findViews() {
        initData();

    }

    override fun setListensers() {
        tv_auth_bank_card_back.setOnClickListener { backActivity() }
        tv_severity_code.setOnClickListener { getVerifyCode() }
    }

    /**
     * 获取验证码
     */
    private fun getVerifyCode() {

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

}
