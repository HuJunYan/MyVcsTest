package com.tianshen.cash.activity

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowManager
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.constant.NetConstantValue
import com.tianshen.cash.model.SignUpBean
import com.tianshen.cash.model.VerifyCodeBean
import com.tianshen.cash.net.api.GetVerifyCode
import com.tianshen.cash.net.api.SignUp
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.LogUtil
import com.tianshen.cash.utils.RegexUtil
import com.tianshen.cash.utils.RomUtils
import com.tianshen.cash.utils.ToastUtil
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_registe2.*
import org.json.JSONException
import org.json.JSONObject

class RegisteActivity2 : BaseActivity() {


    var str = "注册代表你同意\n天神贷用户服务协议"
    override fun setContentView(): Int = R.layout.activity_registe2
    override fun findViews() {
        initRegisterOptions();
        initSpanString();
    }

    private fun initSpanString() {
        val ss = SpannableStringBuilder(str)
        ss.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                gotoWebActivity()
            }

            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
                ds?.isUnderlineText = false;
            }
        }, 11, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //span 字体颜色
        ss.setSpan(ForegroundColorSpan(resources.getColor(R.color.global_popular_color)), 11, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // span 默认背景颜色
        ss.setSpan(BackgroundColorSpan(Color.TRANSPARENT), 11, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_argument.highlightColor = Color.TRANSPARENT
        tv_argument.setText(ss);
        tv_argument.movementMethod = LinkMovementMethod.getInstance()
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

    override fun setListensers() {
        tv_login.setOnClickListener { register() }
        et_phone_number.setListener { getVerityCode() }
        in_back.setOnClickListener { backActivity() }
    }

    private fun register() {
        if (TextUtils.isEmpty(et_phone_number.text.trim()) || !RegexUtil.IsTelephone(et_phone_number.text.trim())) {
            ToastUtil.showToast(mContext, "请输入正确的手机号")
            return
        }
        if (TextUtils.isEmpty(et_verify_code.text.trim())) {
            ToastUtil.showToast(mContext, "请输入验证码")
            return
        }
        if (TextUtils.isEmpty(et_login_pwd.text.trim())) {
            ToastUtil.showToast(mContext, "请输入密码")
            return
        }
        if (et_login_pwd.text.trim().length < 6 || et_login_pwd.text.trim().length > 18) {
            ToastUtil.showToast(mContext, "请输入6~18位有效密码")
            return
        }
        try {
            val jsonObject = JSONObject()
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                val TelephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                jsonObject.put("device_id", TelephonyMgr.deviceId)
            }
            val signUp = SignUp(mContext);
            jsonObject.put("mobile", et_phone_number.text.trim())
            jsonObject.put("password", et_login_pwd.text.trim())
            jsonObject.put("verify_code", et_verify_code.text.trim())
            signUp.signUp(jsonObject, tv_login, true, object : BaseNetCallBack<SignUpBean> {
                override fun onSuccess(paramT: SignUpBean) {
                    ToastUtil.showToast(mContext, "注册成功")
                    backActivity()
                    //                            gotoActivity(mContext, LoginActivity.class, null);
                    //                            finish();
                    //                           login(et_mobile.getEditTextString().trim(),et_password.getEditTextString().trim());
                }

                override fun onFailure(url: String, errorType: Int, errorCode: Int) {

                }
            })
        } catch (e: Exception) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e))
        }

    }

    private fun gotoWebActivity() {
        val userServiceProtocolURL = NetConstantValue.getUserServiceProtocolURL()
        val bundle = Bundle()
        bundle.putString(GlobalParams.WEB_URL_KEY, userServiceProtocolURL)
        gotoActivity(mContext, WebActivity::class.java, bundle)
    }

    private fun getVerityCode(): Boolean {
        if (TextUtils.isEmpty(et_phone_number.text.trim()) || !RegexUtil.IsTelephone(et_phone_number.text.trim())) {
            ToastUtil.showToast(mContext, "请输入正确的手机号")
            return false;
        }
        try {
            val json = JSONObject()
            json.put("mobile", et_phone_number.text.trim())
            json.put("type", 4.toString())
            val mGetVerifyCodeAction = GetVerifyCode(mContext)
            mGetVerifyCodeAction.getVerifyCode(json, object : BaseNetCallBack<VerifyCodeBean> {
                override fun onSuccess(verifyCodeBean: VerifyCodeBean) {
                    ToastUtil.showToast(mContext, "验证码发送成功")
                }

                override fun onFailure(url: String, errorType: Int, errorCode: Int) {
                    et_phone_number.finishTimer()
                }
            })
        } catch (e: JSONException) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e))
            e.printStackTrace()
        }

        return true
    }


}
