package com.tianshen.cash.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tianshen.cash.R
import com.tianshen.cash.adapter.MessageAdapter
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.constant.GlobalParams
import com.tianshen.cash.model.MessageBean
import com.tianshen.cash.model.MessageDataBean
import com.tianshen.cash.model.PostDataBean
import com.tianshen.cash.net.api.GetMessageCenter
import com.tianshen.cash.net.api.UpdateMessageStatus
import com.tianshen.cash.net.base.BaseNetCallBack
import com.tianshen.cash.utils.TianShenUserUtil
import kotlinx.android.synthetic.main.activity_message_center.*
import org.json.JSONException
import org.json.JSONObject

class MessageCenterActivity : BaseActivity() {

    private var mAdapter: MessageAdapter? = null
    private var mMessageBeanList: MutableList<MessageBean> = mutableListOf()

    private var page = 1

    override fun setContentView(): Int = R.layout.activity_message_center

    override fun findViews() {
    }

    override fun setListensers() {
        tv_message_center_back.setOnClickListener { backActivity() }
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
            updateMessageStatus(it)
        })
        refreshLayout.setOnRefreshListener { getMessages(true) }
        refreshLayout.setOnLoadmoreListener { getMessages(false) }
        refreshLayout.refreshHeader = ClassicsHeader(this) // header
        refreshLayout.refreshFooter = ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale) //footer
        xrecyclerview_message_center.layoutManager = LinearLayoutManager(this@MessageCenterActivity, LinearLayoutManager.VERTICAL, false)
        xrecyclerview_message_center.adapter = mAdapter

    }

    private fun getMessages(isRefresh: Boolean) {

        try {
            val jsonObject = JSONObject()
            val userId = TianShenUserUtil.getUserId(mContext)
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
            jsonObject.put("count", GlobalParams.CONSUMPTIONRECORD_LOAD_LENGTH)


            if (isRefresh) {
                page = 1
            } else {
                page++
            }
            jsonObject.put("page", "$page")

            val messageCenter = GetMessageCenter(mContext)
            messageCenter.getMessages(jsonObject, null, false, object : BaseNetCallBack<MessageDataBean> {
                override fun onSuccess(paramT: MessageDataBean) {

                    if (isRefresh) { //下拉刷新
                        mMessageBeanList.clear()
                        mMessageBeanList = paramT.data.message_list
                        mAdapter?.setData(mMessageBeanList)
                        refreshLayout.isLoadmoreFinished = false
                    } else {//上拉加载更多
                        mMessageBeanList.addAll(paramT.data.message_list)
                        mAdapter?.setData(mMessageBeanList)
                    }

                    if (mMessageBeanList.size == 0) {
                        refreshLayout.visibility = View.GONE
                        rl_message_empty.visibility = View.VISIBLE
                        return
                    }

                    if (paramT.data.message_list.size == 0) {
                        refreshLayout.isLoadmoreFinished = true
                        refreshLayout.finishRefresh()
                        refreshLayout.finishLoadmore()
                        return
                    }

                    mAdapter?.notifyDataSetChanged()
                    if (isRefresh) {
                        refreshLayout.finishRefresh()
                    } else {
                        refreshLayout.finishLoadmore()
                    }
                }

                override fun onFailure(url: String, errorType: Int, errorCode: Int) {

                    refreshLayout.visibility = View.GONE
                    rl_message_empty.visibility = View.VISIBLE

                }
            })


        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun updateMessageStatus(msg: MessageBean) {

        try {
            val jsonObject = JSONObject()
            val userId = TianShenUserUtil.getUserId(mContext)
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId)
            jsonObject.put("msg_id", msg.msg_id)
            jsonObject.put("msg_type", msg.msg_type)

            val updateMessageStatus = UpdateMessageStatus(mContext)
            updateMessageStatus.update(jsonObject, null, false, object : BaseNetCallBack<PostDataBean> {
                override fun onSuccess(paramT: PostDataBean) {

                    //"msg_type":"0",//0活动类型(原生页面),1活动类型(H5页面),2阅读类型(H5页面)
                    when (msg.msg_type) {
                        "0" -> {
                            val bundle = Bundle()
                            bundle.putString(GlobalParams.ACTIVITY_ID, msg.activity_id)
                            gotoActivity(mContext, InviteFriendsActivity::class.java, bundle)
                        }
                        else -> {
                            val bundle = Bundle()
                            bundle.putString(GlobalParams.WEB_FROM, GlobalParams.FROM_MESSAGE)
                            bundle.putSerializable(GlobalParams.WEB_MSG_DATA_KEY, msg)
                            gotoActivity(mContext, WebActivity::class.java, bundle)
                        }
                    }
                }

                override fun onFailure(url: String, errorType: Int, errorCode: Int) {
                }
            })


        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

}
