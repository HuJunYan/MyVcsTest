package com.tianshen.cash.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tianshen.cash.R
import com.tianshen.cash.adapter.MessageAdapter
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.MessageBean
import com.tianshen.cash.model.MessageDataBean
import com.tianshen.cash.net.api.GetMessageCenter
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.TianShenUserUtil
import kotlinx.android.synthetic.main.activity_message_center.*
import org.json.JSONException
import org.json.JSONObject

class MessageCenterActivity : BaseActivity() {

    private var mAdapter: MessageAdapter? = null
    private var mMessageBeanList: MutableList<MessageBean>? = null

    private var page = 1

    override fun setContentView(): Int = R.layout.activity_message_center

    override fun findViews() {
    }

    override fun setListensers() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initXRecyclerview()
        getMessages(true)
    }

    /**
     * 初始化XRecyclerview
     */
    private fun initXRecyclerview() {

        mAdapter = MessageAdapter(mutableListOf<MessageBean>(), {
        })
        refreshLayout.setOnRefreshListener { getMessages(true) }
        refreshLayout.setOnLoadmoreListener { getMessages(false) }
        refreshLayout.refreshHeader = ClassicsHeader(this) // header
        refreshLayout.refreshFooter = ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale) //footer
        xrecyclerview_message_center.layoutManager = LinearLayoutManager(this@MessageCenterActivity, LinearLayoutManager.VERTICAL, false)
        xrecyclerview_message_center.adapter = mAdapter
    }

    private fun getMessages(isRefresh: Boolean) {
        mMessageBeanList = mutableListOf()
        for (i in 1..5) {
            val message = MessageBean()
            message.msg_description = "描述" + i
            message.msg_time_str = "1989-01-01 10:30"
            message.msg_title = "标题" + i
            mMessageBeanList?.add(message)
        }
        mAdapter?.setData(mMessageBeanList!!)
    }

//    private fun getMessages(isRefresh: Boolean) {
//
//        try {
//            val jsonObject = JSONObject()
//            val userId = TianShenUserUtil.getUserId(mContext)
//            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
//            jsonObject.put("count", GlobalParams.CONSUMPTIONRECORD_LOAD_LENGTH)
//
//
//            if (isRefresh) {
//                page = 1
//            } else {
//                page++
//            }
//            jsonObject.put("page", "$page")
//
//            val messageCenter = GetMessageCenter(mContext)
//            messageCenter.getMessages(jsonObject, null, true, object : BaseNetCallBack<MessageDataBean> {
//                override fun onSuccess(paramT: MessageDataBean) {
//
//                    if (isRefresh) { //下拉刷新
//                        mMessageBeanList?.clear()
//                        mMessageBeanList = paramT.data.message_list
//                        mAdapter?.setData(mMessageBeanList!!)
//                        refreshLayout.isLoadmoreFinished = false
//                    } else {//上拉加载更多
//                        mMessageBeanList?.addAll(paramT.data.message_list)
//                        mAdapter?.setData(mMessageBeanList!!)
//                    }
//
//                    if (mMessageBeanList == null || mMessageBeanList?.size == 0) {
//                        refreshLayout.isLoadmoreFinished = true
//                        return
//                    }
//
//                    mAdapter?.notifyDataSetChanged()
//                    if (isRefresh) {
//                        refreshLayout.finishRefresh()
//                    } else {
//                        refreshLayout.finishLoadmore()
//                    }
//                }
//
//                override fun onFailure(url: String, errorType: Int, errorCode: Int) {
//                }
//            })
//
//
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//    }

}
