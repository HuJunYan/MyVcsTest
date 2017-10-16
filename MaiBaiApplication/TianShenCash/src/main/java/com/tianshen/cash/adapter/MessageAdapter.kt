package com.tianshen.cash.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.tianshen.cash.R
import com.tianshen.cash.base.MyApplicationLike
import com.tianshen.cash.model.MessageBean
import com.tianshen.cash.utils.ImageLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.item_message_center.view.*
import java.util.concurrent.TimeUnit

class MessageAdapter(private var messageBeans: MutableList<MessageBean>,
                     val onClick: (MessageBean) -> Unit)
    : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindData(messageBeans[position])
    }

    override fun getItemCount(): Int = messageBeans.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent?.context).inflate(R.layout.item_message_center, parent, false).let {
            ViewHolder(it, onClick)
        }
    }

    class ViewHolder(itemView: View, val onClick: (MessageBean) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindData(itemBean: MessageBean) {
            with(itemBean) {
                itemView.tv_item_message_time.text = msg_time_str
                itemView.tv_item_message_title.text = msg_title
                itemView.tv_item_message_description.text = msg_description

                if (TextUtils.isEmpty(msg_img_url)) {
                    itemView.iv_item_message.visibility = View.GONE
                    itemView.tv_item_message_line.visibility = View.VISIBLE
                } else {
                    itemView.iv_item_message.visibility = View.VISIBLE
                    itemView.tv_item_message_line.visibility = View.GONE
                    ImageLoader.loadCache(MyApplicationLike.getsApplication(), msg_img_url, R.drawable.ic_message_item_empty, itemView.iv_item_message)
                }

                if ("0" == msg_status) {//0未读,1已读
                    itemView.tv_item_message_title.setTextColor(MyApplicationLike.getsApplication().resources.getColor(R.color.global_txt_black4))
                    itemView.tv_item_message_description.setTextColor(MyApplicationLike.getsApplication().resources.getColor(R.color.global_txt_black5))
                } else {
                    itemView.tv_item_message_title.setTextColor(MyApplicationLike.getsApplication().resources.getColor(R.color.edit_text_hint_color))
                    itemView.tv_item_message_description.setTextColor(MyApplicationLike.getsApplication().resources.getColor(R.color.edit_text_hint_color))
                }

                RxView.clicks(itemView.ll_item_message)//1秒钟之内禁用重复点击
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            onClick(this)
                        }

            }
        }
    }

    fun setData(data: List<MessageBean>) {
        messageBeans.clear()
        messageBeans.addAll(data)
    }
}