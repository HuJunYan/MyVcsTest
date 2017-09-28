package com.tianshen.cash.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tianshen.cash.R
import com.tianshen.cash.model.MessageBean
import kotlinx.android.synthetic.main.item_message_center.view.*

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
                var title = itemBean.msg_title
                itemView.tv_item_message_title.text = title
            }
        }
    }

    fun setData(data: List<MessageBean>) {
        messageBeans.clear()
        messageBeans.addAll(data)
    }
}