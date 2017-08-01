package com.tianshen.cash.activity

import android.Manifest
import android.content.Context
import android.graphics.Color
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
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.event.LoginSuccessEvent
import com.tianshen.cash.model.TianShenLoginBean
import com.tianshen.cash.net.api.SignIn
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.net.base.UserUtil
import com.tianshen.cash.utils.*
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_login2.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class LoginActivity : BaseActivity() {
    override fun setContentView(): Int = R.layout.activity_login2
    var str = "客服电话: 400-0000-8685"
    var hasTelephonePermission: Boolean? = false
    var mUniqueId: String? = null
    var mobile: String? = null
    var password: String? = null
    var mHandler: Handler? = null
    var mRegIdQueryTimes = 0
    var mSignIn: SignIn? = null
    override fun findViews() {
        initRegisterOptions();
        initSpanString();
    }

    private fun initSpanString() {
        val ss = SpannableStringBuilder(str)
        ss.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                ToastUtil.showToast(this@LoginActivity, "haha")
            }

            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
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
                    val reg_id = JPushInterface.getRegistrationID(mContext)
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
        if (hasTelephonePermission == true && mUniqueId == null) {
            mUniqueId = System.currentTimeMillis().toString() + ""
        }
        try {
            val json = JSONObject()
            json.put("device_id", mUniqueId)
            json.put("mobile", mobile)
            json.put("password", password)

            var jpushId = TianShenUserUtil.getUserJPushId(mContext)
            if (TextUtils.isEmpty(jpushId)) {
                jpushId = JPushInterface.getRegistrationID(mContext)
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
                    val bdLocation = LocationUtil.getInstance(mContext).location
                    if (bdLocation != null) {
                        tags.add(bdLocation.cityCode)
                        tags.add(bdLocation.countryCode)
                        tags.add(bdLocation.province)
                        JPushInterface.setAliasAndTags(mContext, TianShenUserUtil.getUserId(mContext), tags)
                    }

                    gotoActivity(mContext, MainActivity::class.java, null)
                    EventBus.getDefault().post(LoginSuccessEvent())

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

    private fun initRegisterOptions() {
        // 更改状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(android.R.color.white)

            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
        if (RomUtils.isMIUI()) {
            RomUtils.setStatusBarDarkMode(true, this)
        }
    }

    /**
     * 收到了在注册页面登录成功的消息
     */
    @Subscribe
    fun onLoginSuccess(event: LoginSuccessEvent) {
        backActivity()
    }
}
