package com.tianshen.cash.activity

import android.os.Bundle
import android.view.KeyEvent
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.event.RefreshRepayDataEvent
import com.tianshen.cash.event.RepayFailureEvent
import com.tianshen.cash.model.RepayInfoBean
import com.tianshen.cash.net.api.GetRepayInfo
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.TianShenUserUtil
import com.tianshen.cash.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_confirm_repay_zi_ying.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject

class ConfirmRepayZiYingActivity : BaseActivity() {


    var mRepayInfoBean: RepayInfoBean? = null;
    override fun setContentView() = R.layout.activity_confirm_repay_zi_ying

    override fun findViews() {
    }

    override fun setListensers() {
        tv_confirm_money_back.setOnClickListener { EventBus.getDefault().post(RepayFailureEvent());backActivity() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(RepayFailureEvent())
        }
        return super.onKeyDown(keyCode, event)
    }


    /**
     * 得到确认还款信息
     */
    private fun initRepayData() {
        try {
            val jsonObject = JSONObject()
            val userId = TianShenUserUtil.getUserId(mContext)
            val extras = intent.extras
            if (extras != null) {
                val consume_id = extras.getString(GlobalParams.CONSUME_ID, "")
                jsonObject.put(GlobalParams.CONSUME_ID, consume_id)
            }
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
            val getRepayInfo = GetRepayInfo(mContext)
            getRepayInfo.getRepayInfo(jsonObject, null, true, object : BaseNetCallBack<RepayInfoBean> {
                override fun onSuccess(paramT: RepayInfoBean) {
                    mRepayInfoBean = paramT
                    refreshUI()
                }

                override fun onFailure(url: String, errorType: Int, errorCode: Int) {}
            })
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    /**
     * 刷新UI
     */
    private fun refreshUI() {

        if (mRepayInfoBean == null || mRepayInfoBean?.data == null) {
            ToastUtil.showToast(mContext, "数据错误")
            return
        }
        val bank_card_num = mRepayInfoBean?.getData()?.bank_card_num
        val bank_name = mRepayInfoBean?.getData()?.bank_name
//        val consumeAmount = mRepayInfoBean.getData().consume_amount
//        val overdueAmount = mRepayInfoBean.getData().overdue_amount
//        tvConfirmRepay.setText(MoneyUtils.getPointTwoMoney(consumeAmount, overdueAmount) + "元")
//        tvConfirmRepayBank.setText(bank_name)
//        tvConfirmRepayNumBank.setText(bank_card_num)

    }

    @Subscribe
    fun onRefreshRepayDataEvent(event: RefreshRepayDataEvent) {
        initRepayData()
    }
}
