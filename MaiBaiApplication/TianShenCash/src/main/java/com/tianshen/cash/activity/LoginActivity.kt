package com.tianshen.cash.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.telephony.TelephonyManager
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowManager
import cn.jpush.android.api.JPushInterface
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.base.MyApplicationLike
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.event.LoginSuccessEvent
import com.tianshen.cash.model.CashAmountBean
import com.tianshen.cash.model.TianShenLoginBean
import com.tianshen.cash.net.api.GetCashAmountService
import com.tianshen.cash.net.api.SignIn
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.net.base.UserUtil
import com.tianshen.cash.utils.*
import com.umeng.analytics.MobclickAgent
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login2.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class LoginActivity : BaseActivity() {
    override fun setContentView(): Int = R.layout.activity_login2
    var str = "客服电话: 400-000-8685"
    var phoneNum = "4000008685"
    var hasTelephonePermission: Boolean? = false
    var mUniqueId: String? = null
    var mobile: String? = null
    var password: String? = null
    var mHandler: Handler? = null
    var mRegIdQueryTimes = 0
    var mSignIn: SignIn? = null
    override fun findViews() {
        initSpanString();
    }

    private fun initSpanString() {
        val ss = SpannableStringBuilder(str)
        ss.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                callPhone()
            }

            override fun updateDrawState(ds: TextPaint?) {
//                super.updateDrawState(ds)
                ds?.isUnderlineText = false;
            }
        }, 6, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //span 字体颜色
        ss.setSpan(ForegroundColorSpan(resources.getColor(R.color.global_popular_color)), 6, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // span 默认背景颜色
        ss.setSpan(BackgroundColorSpan(Color.TRANSPARENT), 6, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_service_phone.highlightColor = Color.TRANSPARENT
        tv_service_phone.setText(ss);
        tv_service_phone.movementMethod = LinkMovementMethod.getInstance()
    }

    //打电话
    private fun callPhone() {
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(Consumer<Boolean> {
            if (it) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum))
                startActivity(intent)
            }
        })

    }

    override fun setListensers() {
        in_back.setOnClickListener { backActivity() }
        tv_login.setOnClickListener { loginHandle() }
        tv_forget_pwd.setOnClickListener { forgetPwd() }
        tv_regist.setOnClickListener { gotoActivity(mContext, RegisteActivity::class.java, null) }
        initData()
        initHandler()
    }

    //登录
    private fun loginHandle() {
        mobile = et_phone_number.text.toString().trim()
        password = et_login_pwd.text.toString().trim()
        //检查账号
        if (!TextUtils.isEmpty(mobile) and RegexUtil.IsTelephone(mobile)) {
            if (TextUtils.isEmpty(password)) {
                return;
            } else {
                tv_login.isClickable = false
                val msg = Message()
                msg.what = 380
                mHandler?.sendMessage(msg)
            }
        } else {
            ToastUtil.showToast(mContext, "不是合法的手机号码")
            return
        }

    }

    private fun initHandler() {
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (380 == msg.what) {
                    val app = MyApplicationLike.getMyApplicationLike()
                    val reg_id = JPushInterface.getRegistrationID(app)
                    LogUtil.d("ret", "reg_id = " + reg_id)
                    if (TextUtils.isEmpty(reg_id)) {
                        if (mRegIdQueryTimes == 7) {
                            login(mobile, password)
                            mRegIdQueryTimes = 0
                            return
                        }
                        val msgNext = Message()
                        msgNext.what = 380
                        mHandler?.sendMessageDelayed(msgNext, 500)
                        mRegIdQueryTimes++
                        if (mRegIdQueryTimes == 1) {
                            ToastUtil.showToast(mContext, resources.getString(R.string.initialization_please_wait))
                        }
                    } else {
                        login(mobile, password)
                        mRegIdQueryTimes = 0
                    }
                }
            }
        }
    }


    fun initData() {

        val rxPermissions = RxPermissions(this@LoginActivity)
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE).subscribe { aBoolean ->
            if (aBoolean) {
                val TelephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                mUniqueId = TelephonyMgr.deviceId
                UserUtil.setDeviceId(mContext, TelephonyMgr.deviceId)
            } else {
                ToastUtil.showToast(mContext, resources.getString(R.string.please_open_permission))
            }
            hasTelephonePermission = aBoolean
        }

        mSignIn = SignIn(mContext)
    }

    fun login(mobile: String?, password: String?) {
        if (hasTelephonePermission == false) {
            ToastUtil.showToast(mContext, resources.getString(R.string.please_open_permission))
            return
        }
        if (hasTelephonePermission == true && TextUtils.isEmpty(mUniqueId)) {
            mUniqueId = System.currentTimeMillis().toString()
        }

        val app = MyApplicationLike.getMyApplicationLike()

        try {
            val json = JSONObject()
            json.put("device_id", mUniqueId)
            json.put("mobile", mobile)
            json.put("password", password)

            var jpushId = TianShenUserUtil.getUserJPushId(mContext)
            if (TextUtils.isEmpty(jpushId)) {
                jpushId = JPushInterface.getRegistrationID(app)
            }
            json.put("push_id", jpushId)
            val finalJpushId = jpushId
            mSignIn?.signIn(json, tv_login, true, object : BaseNetCallBack<TianShenLoginBean> {
                override fun onSuccess(paramT: TianShenLoginBean) {
                    //保存用户信息
                    TianShenUserUtil.saveUserToken(mContext, paramT.data.token)
                    TianShenUserUtil.saveUserId(mContext, paramT.data.customer_id)
                    TianShenUserUtil.saveUserPhoneNum(mContext, mobile)
                    TianShenUserUtil.saveUserJPushId(mContext, finalJpushId)


                    val tags = HashSet<String>()
                    val bdLocation = LocationUtil.getInstance().location
                    if (bdLocation != null) {
                        tags.add(bdLocation.cityCode)
                        tags.add(bdLocation.countryCode)
                        tags.add(bdLocation.province)
                        JPushInterface.setAliasAndTags(app, TianShenUserUtil.getUserId(app), tags)
                        if (JPushInterface.isPushStopped(app)) {
                            JPushInterface.resumePush(app)
                        }
                    }
                    checkStatusGoActivity()
                }

                override fun onFailure(url: String, errorType: Int, errorCode: Int) {
                    tv_login.isClickable = true
                }
            })
        } catch (e: JSONException) {
            e.printStackTrace()
            MobclickAgent.reportError(mContext, LogUtil.getException(e))
        }

    }

    private fun forgetPwd() {
        val bundle = Bundle()
        bundle.putString("type", GlobalParams.CHANGE_LOGIN_PASSWORD)
        gotoActivity(mContext, ForgetPasswordActivity::class.java, bundle)
    }

    /**
     * 根据当前用户的状态跳转到不同的页面
     */
    private fun checkStatusGoActivity() {

        val jsonObject = JSONObject()
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext))

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val getCashAmountService = GetCashAmountService(mContext)
        getCashAmountService.getData(jsonObject, object : BaseNetCallBack<CashAmountBean> {
            override fun onSuccess(bean: CashAmountBean) {

                val is_payway = bean.data.is_payway
                val cash_amount_status = bean.data.cash_amount_status

                val cur_credit_step = bean.data.cur_credit_step
                val total_credit_step = bean.data.total_credit_step
                val cash_amount = bean.data.cash_amount
                val show_dialog = bean.data.show_dialog

                var totalCreditStep = 0
                var curCreditStep = 0
                var cashAmount = 0

                if (!TextUtils.isEmpty(cur_credit_step)) {
                    curCreditStep = Integer.parseInt(cur_credit_step)
                }
                if (!TextUtils.isEmpty(total_credit_step)) {
                    totalCreditStep = Integer.parseInt(total_credit_step)
                }
                if (!TextUtils.isEmpty(cash_amount)) {
                    cashAmount = Integer.parseInt(cash_amount)
                }
                if ("0" == show_dialog) { //掌众认证失败跳转到首页
                    gotoActivity(mContext, MainActivity::class.java, null)
                    EventBus.getDefault().post(LoginSuccessEvent())
                    return
                }
                if (curCreditStep > 0 && curCreditStep < totalCreditStep) { //跳转到认证中心页面
                    gotoActivity(mContext, AuthCenterMenuActivity::class.java, null)
                    EventBus.getDefault().post(LoginSuccessEvent())
                    finish()
                    return
                }
                if ("0" == cash_amount_status) { //跳转到首页
                    if (cashAmount == 0 && curCreditStep > 0) { //跳转到认证中心页面
                        gotoActivity(mContext, AuthCenterMenuActivity::class.java, null)
                    } else {//跳转到首页
                        gotoActivity(mContext, MainActivity::class.java, null)
                    }
                    EventBus.getDefault().post(LoginSuccessEvent())
                } else if ("1" == cash_amount_status && "0" == is_payway) { //跳转到首页
                    gotoActivity(mContext, MainActivity::class.java, null)
                    EventBus.getDefault().post(LoginSuccessEvent())
                } else if ("1" == cash_amount_status && "1" == is_payway) { //跳转到掌众借款页面
                    gotoActivity(mContext, ConfirmBorrowingActivity::class.java, null)
                    finish()
                } else if ("2" == cash_amount_status) { //跳转到跑分等待页面
                    gotoActivity(mContext, EvaluateAmountActivity::class.java, null)
                    finish()
                }
            }

            override fun onFailure(url: String, errorType: Int, errorCode: Int) {

            }
        })
    }

    /**
     * 收到了在注册页面登录成功的消息
     */
    @Subscribe
    fun onLoginSuccess(event: LoginSuccessEvent) {
        backActivity()
    }
}
