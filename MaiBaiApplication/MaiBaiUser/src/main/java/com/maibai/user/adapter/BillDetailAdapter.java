package com.maibai.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.ConsumeBigBillItemBean;
import com.maibai.user.utils.GetTelephoneUtils;
import com.maibai.user.utils.ImageLoaderUtil;
import com.maibai.user.utils.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhangchi on 2016/6/24.
 */
public class BillDetailAdapter extends CommonAdapter<ConsumeBigBillItemBean> {
    private BillDetailItemListener mListener = null;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private ImageLoaderUtil imageLoaderUtil;

    public BillDetailAdapter(Context context, List<ConsumeBigBillItemBean> datas, BillDetailItemListener listener) {
        super(context, datas);
        mListener = listener;
        imageLoaderUtil = new ImageLoaderUtil(mContext);
        imageLoader = imageLoaderUtil.getImageLoader();
        options = imageLoaderUtil.getNormalOption();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        MyClick myClick = null;
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bill_detail, parent, false);
                holder = new ViewHolder();
                myClick = new MyClick();
                holder.findViews(convertView, myClick);
                convertView.setTag(holder);
                convertView.setTag(R.id.tv_immediately, myClick);
            } else {
                holder = (ViewHolder) convertView.getTag();
                myClick = (MyClick) convertView.getTag(R.id.tv_immediately);
            }
            holder.setData(mDatas.get(position));
            myClick.position = position;
        } catch (Exception e) {
            // TODO 上传log到服务器
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        View v_line;
        TextView tv_shop_name;
        TextView tv_bill_type;
        TextView tv_total_money;
        TextView tv_repay_amount;
        TextView tv_repay_date;
        TextView tv_consume_date;
        TextView tv_discount_money;
        TextView tv_fine;
        TextView tv_real_pay;
        RelativeLayout tv_immediately;
        RelativeLayout tv_installment;
        ImageView iv_img;

        protected void findViews(View root, MyClick click) {
            v_line = root.findViewById(R.id.v_line);
            tv_shop_name = (TextView) root.findViewById(R.id.tv_shop_name);
            tv_bill_type = (TextView) root.findViewById(R.id.tv_bill_type);
            tv_total_money = (TextView) root.findViewById(R.id.tv_total_money);
            tv_repay_amount = (TextView) root.findViewById(R.id.tv_repay_amount);
            tv_repay_date = (TextView) root.findViewById(R.id.tv_repay_date);
            tv_consume_date = (TextView) root.findViewById(R.id.tv_consume_date);
            tv_real_pay = (TextView) root.findViewById(R.id.tv_real_pay);
            tv_fine = (TextView) root.findViewById(R.id.tv_fine);
            tv_immediately = (RelativeLayout) root.findViewById(R.id.tv_immediately);
            tv_installment = (RelativeLayout) root.findViewById(R.id.tv_installment);
            tv_discount_money = (TextView) root.findViewById(R.id.tv_discount_money);
            iv_img = (ImageView) root.findViewById(R.id.iv_img);
            int width = new GetTelephoneUtils(mContext).getWindowWidth() / GlobalParams.BILL_IMG_PROPORTION;
            int height = width * GlobalParams.IMG_HEIGHT / GlobalParams.IMG_WIDTH;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            iv_img.setLayoutParams(params);
            tv_immediately.setOnClickListener(click);
            tv_installment.setOnClickListener(click);
        }

        protected void setData(ConsumeBigBillItemBean bean) {
            if (bean != null) {
                tv_shop_name.setText(bean.getMerchant_name());
                String totalAmount=bean.getTotal_amount();
                if("".equals(totalAmount)||null==totalAmount){
                    totalAmount="0";
                }
                tv_total_money.setText("¥" + ((Double.valueOf(totalAmount) / 100)));

                // 已经改为格式化字符串
                String repayData=bean.getRepay_date();
                if (null==repayData){
                    repayData="";
                }
                tv_repay_date.setText(repayData);
                String consumData=bean.getConsume_date();
                if (null==consumData){
                    consumData="";
                }
                tv_consume_date.setText(consumData);
                String realPay=bean.getReal_pay();
                if("".equals(realPay)||null==realPay){
                    realPay="0";
                }
                tv_real_pay.setText("¥" + ((Double.valueOf(realPay))) / 100);
                String discount=bean.getDiscount();
                if("".equals(discount)||null==discount){
                    discount="0";
                }
                tv_discount_money.setText("¥" + (Double.valueOf(discount)) / 100);
                String fine=bean.getFine();
                if("".equals(fine)||null==fine){
                    fine="0";
                }
                if (Double.valueOf(fine) > 0) {
                    tv_fine.setText("(含逾期费" + (Double.valueOf(fine)) / 100 + "元）");
                } else {
                    tv_fine.setVisibility(View.GONE);
                }
                String repayAmount=bean.getRepay_amount();
                if("".equals(repayAmount)||null==repayAmount){
                    repayAmount="0";
                }
                tv_repay_amount.setText((((Double.valueOf(repayAmount) + Double.valueOf(fine)) / 100)) + "");
                if (bean.getMerchant_logo() != null) {
                    imageLoader.displayImage(bean.getMerchant_logo(), iv_img, options);
                }
                if (bean.getRepay_type().equals("1")) {
                    tv_bill_type.setText("次月还款");
                    v_line.setVisibility(View.VISIBLE);
                    tv_installment.setVisibility(View.VISIBLE);
                } else if (bean.getRepay_type().equals("2")) {
                    tv_bill_type.setText("分期还款");
                    v_line.setVisibility(View.GONE);
                    tv_installment.setVisibility(View.GONE);
                }
            }
        }
    }

    class MyClick implements View.OnClickListener {
        int position;

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                switch (v.getId()) {
                    case R.id.tv_immediately:
                        mListener.onImmediatelyClick(position);
                        break;
                    case R.id.tv_installment:
                        mListener.onInstallmentClick(position);
                        break;
                }
            }
        }
    }

    public interface BillDetailItemListener {
        void onImmediatelyClick(int position);

        void onInstallmentClick(int position);
    }

    /**
     * 把毫秒转化成日期
     *
     * @param dateFormat (日期格式，例如：MM/ dd/yyyy HH:mm:ss)
     * @param millSec    (毫秒数)
     * @return
     */
    private String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Date date = new Date(millSec);
        return sdf.format(date);
    }
}
