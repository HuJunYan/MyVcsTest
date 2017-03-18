package com.maibei.merchants.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.model.CommisionListBean;
import com.maibei.merchants.model.CommisionStagesItemBean;
import com.maibei.merchants.view.ScrollListView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */

public class CommisionAdapter extends BaseAdapter {
    Context context;
    List<CommisionListBean> datas;
    LayoutInflater inflater;
    public CommisionAdapter(Context context,List<CommisionListBean> datas){
        this.context=context;
        this.datas=datas;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return datas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null==convertView){
            convertView=inflater.inflate(R.layout.commision_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
            viewHolder.tv_amount_arrive=(TextView)convertView.findViewById(R.id.tv_amount_arrive);
            viewHolder.slv_item=(ScrollListView)convertView.findViewById(R.id.slv_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.tv_name.setText(datas.get(position).getCustomer_name());
        viewHolder.tv_time.setText(datas.get(position).getConsume_date());
        String amountArrive=datas.get(position).getCommission_arrive();
        if(null==amountArrive||"".equals(amountArrive)){
            amountArrive="0";
        }
        viewHolder.tv_amount_arrive.setText("¥"+Double.valueOf(amountArrive)/100);
        List<CommisionStagesItemBean> commisionStagesItemBeanList=datas.get(position).getCommision_stages();
        CommisionStagesItemBeanAdapter itemAdapter=new CommisionStagesItemBeanAdapter(context,commisionStagesItemBeanList);
        viewHolder.slv_item.setAdapter(itemAdapter);

//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        ForegroundColorSpan blackSpan = new ForegroundColorSpan(ContextCompat.getColor(context,R.color.black));
//        ForegroundColorSpan greenSpan = new ForegroundColorSpan(ContextCompat.getColor(context,R.color.green_handover_title));
//        ForegroundColorSpan redSpan = new ForegroundColorSpan(ContextCompat.getColor(context,R.color.dark_red));
        /*for(int i=0;i<commisionStagesItemBeanList.size();i++){
            String name=commisionStagesItemBeanList.get(i).getName()+"  ";
            String money=commisionStagesItemBeanList.get(i).getMoney();
            String content=name+money;

            SpannableStringBuilder currendbuilder ;
            switch (commisionStagesItemBeanList.get(i).getStatus()){
                case "0":
                    if (i<commisionStagesItemBeanList.size()-1){
                        content+="\n";
                    }
                    currendbuilder = new SpannableStringBuilder(content);
                    currendbuilder.setSpan(blackSpan,name.length(),name.length()+money.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(currendbuilder);
                    break;
                case "1":

                    content+="(已到账)";
                    if (i<commisionStagesItemBeanList.size()-1){
                        content+="\n";
                    }
                    currendbuilder = new SpannableStringBuilder(content);
                    currendbuilder.setSpan(greenSpan,name.length(),name.length()+money.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(currendbuilder);
                    break;
                case "2":
                    content+="(待到账)";
                    if (i<commisionStagesItemBeanList.size()-1){
                        content+="\n";
                    }
                    currendbuilder = new SpannableStringBuilder(content);
                    currendbuilder.setSpan(blackSpan,name.length(),name.length()+money.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(currendbuilder);
                    break;
                case "3":
                    content+="(逾期)  ";
                    if (i<commisionStagesItemBeanList.size()-1){
                        content+="\n";
                    }
                    currendbuilder = new SpannableStringBuilder(content);
                    currendbuilder.setSpan(redSpan,name.length(),name.length()+money.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(currendbuilder);
                    break;
                case "4":
                    if (i<commisionStagesItemBeanList.size()-1){
                        content+="\n";
                    }
                    currendbuilder =new SpannableStringBuilder(content);
                    currendbuilder.setSpan(blackSpan,name.length(),name.length()+money.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(currendbuilder);
                default:
            }
        }*/
//        viewHolder.tv_content.setText(builder);
        return convertView;
    }

    public class ViewHolder{
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_amount_arrive;
        private ScrollListView slv_item;
    }
}
