package com.tianshen.cash.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.moxie.client.manager.MoxieCallBack
import com.moxie.client.manager.MoxieSDK
import com.moxie.client.model.MxParam
import com.tianshen.cash.R
import com.tianshen.cash.adapter.AuthNotRequiredAdapter
import com.tianshen.cash.adapter.AuthRequiredAdapter
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.AuthCreditBean
import com.tianshen.cash.model.RequiredBean
import com.tianshen.cash.net.api.GetCreditConf
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.ImageLoader
import com.tianshen.cash.utils.LogUtil
import com.tianshen.cash.utils.TianShenUserUtil
import com.tianshen.cash.utils.ToastUtil
import com.tianshen.cash.view.CustomerItemDecoration
import kotlinx.android.synthetic.main.activity_auth_credit.*
import org.json.JSONObject

class AuthCreditActivity : BaseActivity() {

    private val RESTCODEPHONE = 1002
    private val RESTCODEZGIMA = 1003

    var mAuthCreditBean: AuthCreditBean? = null
    var mAuthRequiredAdapter: AuthRequiredAdapter? = null
    var mAuthNotRequiredAdapter: AuthNotRequiredAdapter? = null

    override fun setContentView(): Int = R.layout.activity_auth_credit

    override fun findViews() {
    }

    override fun setListensers() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        val jsonObject = JSONObject()
        val userId = TianShenUserUtil.getUserId(mContext)
        jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
        val getCreditConf = GetCreditConf(mContext)
        getCreditConf.getData(jsonObject, object : BaseNetCallBack<AuthCreditBean> {

            override fun onSuccess(data: AuthCreditBean?) {
                mAuthCreditBean = data
                refreshUI()
            }

            override fun onFailure(url: String?, errorType: Int, errorCode: Int) {

            }

        })
    }

    private fun refreshUI() {

        //设置必填项
        if (mAuthCreditBean!!.data.required.size > 0) {
            ll_credit_required_title.visibility = View.VISIBLE
            ImageLoader.load(mContext, mAuthCreditBean!!.data!!.required_background, iv_auth_credit_required)
            tv_auth_credit_required.text = mAuthCreditBean!!.data!!.required_content
            if (mAuthRequiredAdapter == null) {
                mAuthRequiredAdapter = AuthRequiredAdapter(mAuthCreditBean!!.data.required, {
                    checkTypeGoAuth(it)
                })
                recyclerview_credit_required.layoutManager = LinearLayoutManager(this@AuthCreditActivity, LinearLayoutManager.VERTICAL, false)
                recyclerview_credit_required.adapter = mAuthRequiredAdapter
                recyclerview_credit_required.addItemDecoration(CustomerItemDecoration(this))
            } else {
                mAuthRequiredAdapter?.setData(mAuthCreditBean!!.data.required)
                mAuthRequiredAdapter?.notifyDataSetChanged()
            }

        } else {
            ll_credit_required_title.visibility = View.GONE
        }

        //设置选填项
        if (mAuthCreditBean!!.data.not_required.size > 0) {
            ll_credit_not_required_title.visibility = View.VISIBLE
            credit_not_required_line_1.visibility = View.VISIBLE
            credit_not_required_line_2.visibility = View.VISIBLE
            ImageLoader.load(mContext, mAuthCreditBean!!.data!!.not_required_background, iv_auth_credit_not_required)
            tv_auth_credit_not_required.text = mAuthCreditBean!!.data!!.not_required_content

            if (mAuthNotRequiredAdapter == null) {
                mAuthNotRequiredAdapter = AuthNotRequiredAdapter(mAuthCreditBean!!.data.not_required, {
                    checkTypeGoAuth(it)
                })
                recyclerview_credit_not_required.layoutManager = LinearLayoutManager(this@AuthCreditActivity, LinearLayoutManager.VERTICAL, false)
                recyclerview_credit_not_required.adapter = mAuthNotRequiredAdapter
                recyclerview_credit_not_required.addItemDecoration(CustomerItemDecoration(this))
            } else {
                mAuthNotRequiredAdapter?.setData(mAuthCreditBean!!.data.not_required)
                mAuthNotRequiredAdapter?.notifyDataSetChanged()
            }
        } else {
            ll_credit_not_required_title.visibility = View.GONE
            credit_not_required_line_1.visibility = View.GONE
            credit_not_required_line_2.visibility = View.GONE
        }

    }


    /**
     * 跳转到手机认证和芝麻信用认证
     *
     * @param url   webview加载的url
     * @param title webView标题
     * @param type  区分是手机认证还是芝麻信用认证
     */
    private fun checkTypeGoAuth(requiredBean: RequiredBean) {

        requiredBean.is_click == "1"

        (mAuthCreditBean!!.data.required).forEach {
            it.is_click = "1"
        }


        LogUtil.d("abc", "requiredBean.is_click ->" + requiredBean.is_click)

        if ("1" == requiredBean.is_click) {
            return
        }

        ////1手机运营商，2芝麻信用,3淘宝，4京东，5信用卡 ，6社保 ,7公积金 ,8学信网
        when (requiredBean.flag) {
            "1" -> gotoChinaMobileActivity(requiredBean.url, "手机认证", "3")
            "2" -> gotoChinaMobileActivity(requiredBean.url, "芝麻信用", "4")
            "3" -> gotoMoXieActivity(requiredBean)
            "4" -> gotoMoXieActivity(requiredBean)
            "5" -> gotoMoXieActivity(requiredBean)
            "6" -> gotoMoXieActivity(requiredBean)
            "7" -> gotoMoXieActivity(requiredBean)
            "8" -> gotoMoXieActivity(requiredBean)
        }
    }

    /**
     * 跳转到手机认证和芝麻信用认证
     * @param url   webview加载的url
     * @param title webView标题
     * @param type  区分是手机认证还是芝麻信用认证
     */
    private fun gotoChinaMobileActivity(url: String, title: String, type: String) {
        val bundle = Intent(this, ChinaMobileActivity::class.java)
        bundle.putExtra(GlobalParams.CHINA_MOBILE_URL_KEY, url)
        bundle.putExtra(GlobalParams.CHINA_MOBILE_TITLE_KEY, title)
        if ("3" == type) {
            startActivityForResult(bundle, RESTCODEPHONE)
        } else {
            startActivityForResult(bundle, RESTCODEZGIMA)
        }
    }

    private fun gotoMoXieActivity(requiredBean: RequiredBean) {

        val userId = TianShenUserUtil.getUserId(mContext)
//        String apiKey = "012a5b3a9bf94ac984fbb7c400c460aa"; //正式key --- 对应pro
        val apiKey = "c916e9aac6a244c2aa47552669c5a1e0" //测试key --- 对应dev

        val mxParam = MxParam()
        mxParam.userId = userId
        mxParam.apiKey = apiKey

        //手机运营商，2芝麻信用,3淘宝，4京东，5信用卡 ，6社保 ,7公积金 ,8学信网
        when (requiredBean.flag) {
            "3" -> mxParam.function = MxParam.PARAM_FUNCTION_TAOBAO
            "4" -> mxParam.function = MxParam.PARAM_FUNCTION_JINGDONG
            "5" -> mxParam.function = MxParam.PARAM_FUNCTION_EMAIL
            "6" -> mxParam.function = MxParam.PARAM_FUNCTION_SECURITY
            "7" -> mxParam.function = MxParam.PARAM_FUNCTION_FUND
            "8" -> mxParam.function = MxParam.PARAM_FUNCTION_CHSI
        }

        MoxieSDK.getInstance().start(this@AuthCreditActivity, mxParam, MoxieCallBack { moxieContext, moxieCallBackData ->
            if (moxieCallBackData != null) {
                when (moxieCallBackData.code) {
                    MxParam.ResultCode.IMPORTING -> {
                    }
                    MxParam.ResultCode.IMPORT_UNSTART -> {
                    }
                    MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR -> {
                        ToastUtil.showToast(mContext, "认证失败!")
                        refreshData(requiredBean.flag, "0")
                        moxieContext.finish()
                    }
                    MxParam.ResultCode.MOXIE_SERVER_ERROR -> {
                        ToastUtil.showToast(mContext, "认证失败!")
                        refreshData(requiredBean.flag, "0")
                        moxieContext.finish()
                    }
                    MxParam.ResultCode.USER_INPUT_ERROR -> {
                        ToastUtil.showToast(mContext, "认证失败!")
                        refreshData(requiredBean.flag, "0")
                        moxieContext.finish()
                    }
                    MxParam.ResultCode.IMPORT_FAIL -> {
                        ToastUtil.showToast(mContext, "认证失败!")
                        refreshData(requiredBean.flag, "0")
                        moxieContext.finish()
                    }
                    MxParam.ResultCode.IMPORT_SUCCESS -> {
                        ToastUtil.showToast(mContext, "认证成功!")
                        refreshData(requiredBean.flag, "1")
                        moxieContext.finish()
                        return@MoxieCallBack true
                    }
                }
            }
            false
        })
    }

    override fun onActivityResult(arg0: Int, arg1: Int, intent: Intent?) {
        super.onActivityResult(arg0, arg1, intent)
        when (arg0) {
            RESTCODEPHONE -> { //手机认证返回
                if (intent != null) {
                    val status = intent.getStringExtra("RESULTSTATUE")
                    if ("1" == status) { //认证通过
                        refreshData("1", "1")
                    } else { //认证不通过
                        refreshData("1", "0")
                    }
                }
            }
            RESTCODEZGIMA -> { //芝麻认证返回
                if (intent != null) {
                    val status = intent.getStringExtra("RESULTSTATUE")
                    if ("1" == status) { //认证通过
                        refreshData("2", "1")
                    } else { //认证不通过
                        refreshData("2", "0")
                    }
                }
            }
        }
    }

    fun refreshData(flag: String, newStatus: String) {
        (mAuthCreditBean!!.data.required).forEach {
            if (it.flag == flag) {
                it.status = newStatus
                if (newStatus == "1") {//如果认证成功，设置不能点击
                    it.is_click = "1"
                }
            }
        }
        (mAuthCreditBean!!.data.not_required).forEach {
            if (it.flag == flag) {
                it.status = newStatus
                if (newStatus == "1") {//如果认证成功，设置不能点击
                    it.is_click = "1"
                }
            }
        }
        refreshUI()
    }

}