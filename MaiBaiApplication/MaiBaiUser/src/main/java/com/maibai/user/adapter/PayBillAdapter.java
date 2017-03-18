package com.maibai.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.ConsumeItemBean;
import com.maibai.user.utils.GetTelephoneUtils;
import com.maibai.user.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class PayBillAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private ImageLoaderUtil imageLoaderUtil;
    private ClickFunction clickFunction;
    private  List<ConsumeItemBean> consumeItemBeanList;
    public PayBillAdapter(Context context, List<ConsumeItemBean> consumeItemBeanList, ClickFunction clickFunction) {
        this.mContext = context;
        inflater=LayoutInflater.from(context);
        this.clickFunction=clickFunction;
        this.consumeItemBeanList=consumeItemBeanList;
        imageLoaderUtil=new ImageLoaderUtil(mContext);
        imageLoader=imageLoaderUtil.getImageLoader();
        options=imageLoaderUtil.getNormalOption();
    }

    @Override
    public int getCount() {
        return consumeItemBeanList.size();
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
            viewHolder.tv_cancel=(TextView)convertView.findViewById(R.id.tv_cancel);
            viewHolder.tv_pay_down=(TextView)convertView.findViewById(R.id.tv_pay_down);
            viewHolder.tv_confirm_order=(TextView)convertView.findViewById(R.id.tv_confirm_order);
            int width=new GetTelephoneUtils(mContext).getWindowWidth()/GlobalParams.CONSUMPTION_IMG_PROPORTION;
            int height=width* GlobalParams.IMG_HEIGHT/GlobalParams.IMG_WIDTH;
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,height);
            viewHolder.iv_merchan_logo.setLayoutParams(params);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String showAmount=consumeItemBeanList.get(position).getShow_amount();
        if("".equals(showAmount)||null==showAmount){
            showAmount="0";
        }
        viewHolder.tv_total_money.setText("¥"+Double.valueOf(showAmount)/100);
        String consumeData=consumeItemBeanList.get(position).getConsume_date();
        if (null==consumeData){
            consumeData="";
        }
        viewHolder.tv_consume_date.setText(consumeData);
        if("1".equals(consumeItemBeanList.get(position).getRepay_type())){
            viewHolder.tv_repay_type.setText("次月还款");
        }else{
            viewHolder.tv_repay_type.setText("分期还款");
        }
        viewHolder.tv_merchant_name.setText(consumeItemBeanList.get(position).getMerchant_name());
        if(GlobalParams.HAVE_BEEN_VERIFY.equals(consumeItemBeanList.get(position).getStatus()) ||
                GlobalParams.REPAY_MONEY.equals(consumeItemBeanList.get(position).getStatus()) ||
                GlobalParams.NEXT_MONTH_TRANSFORM_STAGE.equals(consumeItemBeanList.get(position).getStatus())){
            viewHolder.rl_bt_holder.setVisibility(View.GONE);
        }else if(GlobalParams.WAIT_PAY_SHOUFU.equals(consumeItemBeanList.get(position).getStatus())){
            viewHolder.rl_bt_holder.setVisibility(View.VISIBLE);
            viewHolder.tv_confirm_order.setVisibility(View.GONE);
            viewHolder.tv_pay_down.setVisibility(View.VISIBLE);
            viewHolder.tv_cancel.setVisibility(View.VISIBLE);
        }else if(GlobalParams.WAIT_FOR_PAY.equals(consumeItemBeanList.get(position).getStatus())){
            viewHolder.rl_bt_holder.setVisibility(View.VISIBLE);
            viewHolder.tv_confirm_order.setVisibility(View.VISIBLE);
            viewHolder.tv_pay_down.setVisibility(View.GONE);
            if(Double.valueOf(consumeItemBeanList.get(position).getDown_payment())>0.0){
                viewHolder.tv_cancel.setVisibility(View.GONE);
            }else{
                viewHolder.tv_cancel.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFunction.clickCancel(position);
            }
        });
        viewHolder.tv_pay_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFunction.clickPayDown(position);
            }
        });
        viewHolder.tv_confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFunction.clickConfirmOrder(position);
            }
        });

        imageLoader.displayImage(consumeItemBeanList.get(position).getMerchant_logo(), viewHolder.iv_merchan_logo,options);
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_consume_date;
        private TextView tv_repay_type;
        private ImageView iv_merchan_logo;
        private TextView tv_merchant_name;
        private TextView tv_total_money;
        private RelativeLayout rl_bt_holder;
        private TextView tv_cancel;
        private TextView tv_pay_down;
        private TextView tv_confirm_order;

    }
    public interface ClickFunction{
        public void clickCancel(int position);
        public void clickConfirmOrder(int position);
        public void clickPayDown(int position);
    }
}
