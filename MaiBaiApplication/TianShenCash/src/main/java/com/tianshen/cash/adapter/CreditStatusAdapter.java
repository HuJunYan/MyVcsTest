package com.tianshen.cash.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.OrderRefreshBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.model.VerifyStepBean;
import com.tianshen.cash.net.api.AgainVerify;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.TianShenUserUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chunpengguo on 2016/12/27.
 */

public class CreditStatusAdapter extends BaseAdapter {

    Context context;
    OrderRefreshBean data;
    LayoutInflater inflater;
    AgainVerifyInterface againVerifyInterface;

    public CreditStatusAdapter(Context context, OrderRefreshBean data) {
        this.context = context;
        this.data = data;
        this.againVerifyInterface = (AgainVerifyInterface) context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (null == data) {
            return 0;
        }
        if (null == data.getData()) {
            return 0;
        }
        if (null == data.getData().getStatus()) {
            return 0;
        }
        return data.getData().getStep().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        VerifyStepBean stepBean = data.getData().getStep().get(i);
        ViewHolder viewHolder;
        if (null == view) {
            view = inflater.inflate(R.layout.step_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            viewHolder.tv_person = (TextView) view.findViewById(R.id.tv_person);
            viewHolder.v_line_top = (View) view.findViewById(R.id.v_line_top);
            viewHolder.v_line_bottom = (View) view.findViewById(R.id.v_line_bottom);
            viewHolder.iv_circle = (ImageView) view.findViewById(R.id.iv_circle);
            viewHolder.iv_zhang = (ImageView) view.findViewById(R.id.iv_zhang);
            viewHolder.v_line_bianjie = (View) view.findViewById(R.id.v_line_bianjie);
            viewHolder.tv_had_again_verify = (TextView) view.findViewById(R.id.tv_had_again_verify);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String display = stepBean.getDisplay();
        if (null == display) {
            display = "";
        }
        viewHolder.tv_content.setText(display);
        String dateTime = stepBean.getDate_time();
        if (null == dateTime) {
            dateTime = "";
        }
        viewHolder.tv_time.setText(dateTime);
        String oprName = stepBean.getOpr_name();
        if (null == oprName) {
            oprName = "";
        }
        viewHolder.tv_person.setText(oprName);
        if (i == 0) {
            viewHolder.v_line_top.setVisibility(View.INVISIBLE);
        }
        int dataSize = data.getData().getStep().size();
        if (i == dataSize - 1) {
            viewHolder.v_line_bottom.setVisibility(View.INVISIBLE);
            viewHolder.v_line_bianjie.setVisibility(View.INVISIBLE);
        }
        int currentStep = Integer.valueOf(data.getData().getCur_step());
        if (1 == i) {
            if ("1".equals(data.getData().getDiscount())) {
                viewHolder.iv_zhang.setVisibility(View.VISIBLE);
                viewHolder.iv_zhang.setImageResource(R.drawable.preferential_single);
            } else {
                viewHolder.iv_zhang.setVisibility(View.GONE);
            }
        }
        if (i == currentStep) {
            viewHolder.v_line_top.setBackgroundColor(ContextCompat.getColor(context, R.color.verify_step_line_red));
            viewHolder.v_line_bottom.setBackgroundColor(ContextCompat.getColor(context, R.color.verify_step_line_gray));
            viewHolder.iv_circle.setImageResource(R.drawable.red_two);
            if ("1".equals(data.getData().getErr_code())) {
                viewHolder.tv_had_again_verify.setVisibility(View.VISIBLE);
                viewHolder.tv_content.setTextColor(ContextCompat.getColor(context, R.color.had_again_certify));
                viewHolder.tv_had_again_verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hadAgainVerify();
                    }
                });
            } else {
                if ("3".equals(data.getData().getErr_code())) {
                    viewHolder.tv_content.setTextColor(ContextCompat.getColor(context, R.color.verify_error_color));
                }
            }
        } else if (i < currentStep) {
            viewHolder.v_line_top.setBackgroundColor(ContextCompat.getColor(context, R.color.verify_step_line_red));
            viewHolder.v_line_bottom.setBackgroundColor(ContextCompat.getColor(context, R.color.verify_step_line_red));
            viewHolder.iv_circle.setImageResource(R.drawable.red);
        } else if (i > currentStep) {
            viewHolder.v_line_top.setBackgroundColor(ContextCompat.getColor(context, R.color.verify_step_line_gray));
            viewHolder.v_line_bottom.setBackgroundColor(ContextCompat.getColor(context, R.color.verify_step_line_gray));
            viewHolder.iv_circle.setImageResource(R.drawable.gurgle);
        }
        return view;
    }

    private class ViewHolder {
        TextView tv_content;
        TextView tv_time;
        TextView tv_person;
        View v_line_top;
        View v_line_bottom;
        ImageView iv_circle;
        ImageView iv_zhang;
        View v_line_bianjie;
        TextView tv_had_again_verify;
    }

    private void hadAgainVerify() {

        try {
            AgainVerify againVerify = new AgainVerify(context);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(context));
            String consumeId = data.getData().getConsume_id();
            if (null == consumeId) {
                consumeId = "";
            }
            jsonObject.put("consume_id", consumeId);
            againVerify.againVerify(jsonObject, null, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    againVerifyInterface.againSuccess();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface AgainVerifyInterface {
        void againSuccess();
    }
}
