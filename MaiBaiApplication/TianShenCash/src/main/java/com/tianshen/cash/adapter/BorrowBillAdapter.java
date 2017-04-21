package com.tianshen.cash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.UserConfig;
import com.tianshen.cash.model.WithdrawalsRecordItemBean;
import com.tianshen.cash.utils.GetTelephoneUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */

public class BorrowBillAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;

    private List<WithdrawalsRecordItemBean> data;
    public BorrowBillAdapter(Context context, List<WithdrawalsRecordItemBean>data) {
        this.mContext = context;
        inflater=LayoutInflater.from(context);
        this.data=data;


    }

    @Override
    public int getCount() {
        return null==data?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.consumption_item,null);
            viewHolder.tv_consume_date=(TextView)convertView.findViewById(R.id.tv_consume_date);
            viewHolder.tv_repay_type=(TextView)convertView.findViewById(R.id.tv_repay_type);
            viewHolder.iv_merchan_logo=(ImageView)convertView.findViewById(R.id.iv_merchan_logo);
            viewHolder.tv_total_money=(TextView)convertView.findViewById(R.id.tv_total_money);
            viewHolder.tv_merchant_name=(TextView)convertView.findViewById(R.id.tv_merchant_name);
            viewHolder.rl_bt_holder=(RelativeLayout)convertView.findViewById(R.id.rl_bt_holder);
            viewHolder.tv_consume_status=(TextView)convertView.findViewById(R.id.tv_consume_status);
            int width=new GetTelephoneUtils(mContext).getWindowWidth()/ GlobalParams.CONSUMPTION_IMG_PROPORTION;
            int height=width* GlobalParams.IMG_HEIGHT/GlobalParams.IMG_WIDTH;
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,height);
            viewHolder.iv_merchan_logo.setLayoutParams(params);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String amount=data.get(position).getAmount();
        if("".equals(amount)||null==amount){
            amount="0";
        }
        viewHolder.tv_total_money.setText("¥"+Double.valueOf(amount)/100);
        String consumTime=data.get(position).getConsume_time();
        if (null==consumTime){
            consumTime="";
        }
        viewHolder.tv_consume_date.setText(consumTime);
        String time=data.get(position).getRepay_times();
        if(null==time){
            time="";
        }
        viewHolder.tv_repay_type.setText("共"+time+"期");
        viewHolder.tv_merchant_name.setText("天神贷现金贷平台");
        viewHolder.rl_bt_holder.setVisibility(View.GONE);


        String status = data.get(position).getStatus();
        switch (status) {
            case "0"://0:新用户，没有提交过订单；
                break;
            case "1"://1:订单待审核；
                viewHolder.tv_consume_status.setText("订单待审核");
                break;
            case "2"://2:审核通过；
                viewHolder.tv_consume_status.setText("审核通过");
                break;
            case "3"://3:放款成功（钱已经到银行卡）；
                viewHolder.tv_consume_status.setText("放款成功");
                break;
            case "4"://4:审核失败
                viewHolder.tv_consume_status.setText("审核失败");
                break;
            case "5"://5:放款失败
                viewHolder.tv_consume_status.setText("放款失败");
                break;
            case "6"://6:放款中
                viewHolder.tv_consume_status.setText("放款中");
                break;
            case "7"://7:已还款
                viewHolder.tv_consume_status.setText("已还款");
                break;
            case "8": //8:已经提交还款（还款金额还没到账
                viewHolder.tv_consume_status.setText("已经提交还款");
                break;
            case "9": //9:决策失败

                break;
        }

        return convertView;
    }
    private class ViewHolder{
        private TextView tv_consume_date;
        private TextView tv_repay_type;
        private ImageView iv_merchan_logo;
        private TextView tv_merchant_name;
        private TextView tv_total_money;
        private TextView tv_consume_status;
        private RelativeLayout rl_bt_holder;

    }
}
