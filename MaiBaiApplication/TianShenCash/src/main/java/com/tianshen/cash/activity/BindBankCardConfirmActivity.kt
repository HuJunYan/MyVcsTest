package com.tianshen.cash.activity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.view.View
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.BankCardInfoBean
import com.tianshen.cash.model.XiangShangSubmitInfoBean
import com.tianshen.cash.model.XiangShangVerifyCodeBean
import com.tianshen.cash.net.api.GetBankCardInfo
import com.tianshen.cash.net.api.GetBindXiangShangVerifyCodeApi
import com.tianshen.cash.net.api.GetSubmitXiangShangBindApi
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.SpannableUtils
import com.tianshen.cash.utils.TianShenUserUtil
import com.tianshen.cash.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_bind_bank_card_confirm.*
import org.json.JSONObject

class BindBankCardConfirmActivity : BaseActivity() {
    var mData: BankCardInfoBean? = null
    private var mStartTime = 59
    var mVerifyData: XiangShangVerifyCodeBean? = null;
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
        initData()

    }

    override fun setListensers() {
        tv_auth_bank_card_back.setOnClickListener { backActivity() }
        tv_severity_code.setOnClickListener { getVerifyCode() }
        tv_confirm_apply.setOnClickListener { postConfirmBindCardInfo() }
        check_box.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tv_confirm_apply.background = resources.getDrawable(R.drawable.shape_home_button_border)
                tv_confirm_apply.isEnabled = true
            } else {
                tv_confirm_apply.background = resources.getDrawable(R.drawable.shape_home_button_unchecked)
                tv_confirm_apply.isEnabled = false
            }
        }
    }

    /**
     * 请求确认帮卡信息接口
     */
    private fun postConfirmBindCardInfo() {
        if (mData == null) {
            ToastUtil.showToast(mContext, "数据错误")
            return;
        }
        if (mVerifyData == null) {
            ToastUtil.showToast(mContext, "请先获取验证码")
            return;
        }
        var text = et_severity_code.text.toString()
        if (TextUtils.isEmpty(text)) {
            ToastUtil.showToast(mContext, "请输入验证码")
            return;
        }
        text = text.trim()
        var getSubmitXiangShangBindApi = GetSubmitXiangShangBindApi(mContext);
        var jsonObject = JSONObject();
        jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext))
        jsonObject.put("card_user_name", mData?.data?.card_user_name);
        jsonObject.put("card_num", mData?.data?.card_num);
        jsonObject.put("reserved_mobile", mData?.data?.reserved_mobile);
        jsonObject.put("verify_code", text);
        jsonObject.put("bank_name", mData?.data?.bank_name);
        jsonObject.put("bank_id", mData?.data?.bank_id);
        jsonObject.put("city_code", mData?.data?.city_code);
        jsonObject.put("identity_code", mData?.data?.identity_code);
        jsonObject.put("smsId", mVerifyData?.data?.smsId);
        jsonObject.put("userNo", mVerifyData?.data?.userNo);
        getSubmitXiangShangBindApi.getSubmitXiangShangBindInfo(jsonObject, tv_confirm_apply, true, object : BaseNetCallBack<XiangShangSubmitInfoBean> {
            override fun onSuccess(paramT: XiangShangSubmitInfoBean?) {
                ToastUtil.showToast(mContext, "验证银行卡信息成功");
                backActivity();
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {
            }
        })
    }

    /**
     * 获取验证码
     */
    private fun getVerifyCode() {
        if (mData == null) {
            ToastUtil.showToast(mContext,"数据错误")
            return
        }
        val getBindXiangShangVerifyCodeApi = GetBindXiangShangVerifyCodeApi(mContext)
        var jsonObject = JSONObject()
        jsonObject.put("bank_name", mData?.data?.bank_name)
        jsonObject.put("bank_id", mData?.data?.bank_id)
        jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext))
        jsonObject.put("card_user_name", mData?.data?.card_user_name)
        jsonObject.put("card_num", mData?.data?.card_num)
        jsonObject.put("reserved_mobile", mData?.data?.reserved_mobile)
        jsonObject.put("identity_code", mData?.data?.identity_code)
        getBindXiangShangVerifyCodeApi.getBindXiangShangVerifyCode(jsonObject, tv_severity_code, true, object : BaseNetCallBack<XiangShangVerifyCodeBean> {
            override fun onSuccess(paramT: XiangShangVerifyCodeBean?) {
                mVerifyData = paramT
                ToastUtil.showToast(mContext, "验证码发送成功")
                refreshSeverityTextUI()
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {
            }
        })
    }


    private fun initData() {
        val jsonObject = JSONObject()
        val userId = TianShenUserUtil.getUserId(mContext)
        jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        val getBankCardInfo = GetBankCardInfo(mContext)
        getBankCardInfo.getBankCardInfo(jsonObject, object : BaseNetCallBack<BankCardInfoBean> {

            override fun onSuccess(data: BankCardInfoBean?) {
                mData = data
                refreshUI()
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })
        initWebData();
    }

    private fun initWebData() {
        var ssList = ArrayList<CharacterStyle>()
        ssList.add(webSpan)
        SpannableUtils.setWebSpannableString(tv_bind_card_agreement, resources.getString(R.string.bind_card_confirm_text), "《", "》", ssList, resources.getColor(R.color.global_txt_orange))

    }

    private fun refreshUI() {
        val data = mData?.data
        if (data != null) {
            tv_confirm_auth_bank_card_person.text = data.card_user_name
            tv_id_num.text = data.identity_code
            tv_auth_card_num.text = data.card_num
            tv_bank_card_phone_num.text = data.reserved_mobile
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

    private val webSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            gotoWebActivity()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
        }
    }

    private fun gotoWebActivity() {
        if (mData == null || mData?.data == null) {
            ToastUtil.showToast(mContext,"数据错误")
            return;
        }
        val bundle = Bundle()
        bundle.putString(GlobalParams.WEB_URL_KEY, mData?.data?.protocol_url)
        gotoActivity(mContext, WebActivity::class.java, bundle)
    }

}
