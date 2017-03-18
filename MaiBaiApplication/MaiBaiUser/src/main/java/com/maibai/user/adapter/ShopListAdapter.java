package com.maibai.user.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.NearByMerchantItemBean;
import com.maibai.user.utils.GetTelephoneUtils;
import com.maibai.user.utils.ImageLoaderUtil;
import com.maibai.user.utils.LogUtil;
import com.nostra13.universalimageloader.BGImageViewAware;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by zhangchi on 2016/6/20.
 */
public class ShopListAdapter extends CommonAdapter<NearByMerchantItemBean> {
    private OnShopItemClickListener mListener;
   ImageLoader imageLoader;
    LayoutInflater inflater;
    Context mContext;
    DisplayImageOptions options,options2;
    ImageLoaderUtil imageLoaderUtil;
    public ShopListAdapter(Context context, List<NearByMerchantItemBean> datas, OnShopItemClickListener listener) {
        super(context, datas);
        this.mContext=context;
        inflater=LayoutInflater.from(context);
        this.mListener = listener;
        imageLoaderUtil=new ImageLoaderUtil(mContext);
        imageLoader=imageLoaderUtil.getImageLoader();
        options=imageLoaderUtil.getNormalOption();
        options2 = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
//        BitmapUtils bitmapUtils = null;
        MyClick myClick = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            myClick = new MyClick();
//            bitmapUtils = BitmapUtils.create(mContext);
            convertView = inflater.inflate(R.layout.item_recommended_nearby, parent, false);
            viewHolder.findViews(convertView);
            viewHolder.tv_item_shop_check.setOnClickListener(myClick);
            convertView.setTag(viewHolder);
//            convertView.setTag(R.id.iv_item_shop_img, bitmapUtils);
            convertView.setTag(R.id.tv_item_shop_check, myClick);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            bitmapUtils = (BitmapUtils) convertView.getTag(R.id.iv_item_shop_img);
            myClick = (MyClick) convertView.getTag(R.id.tv_item_shop_check);
        }
        myClick.position = position;
        NearByMerchantItemBean item = mDatas.get(position);
        viewHolder.setDatas(item);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_item_shop_img;
        TextView tv_item_shop_name;
        TextView tv_item_shop_distance;
        TextView tv_item_shop_reduce;
        TextView tv_item_shop_check;
        ImageView iv_item_icon_reduce;

        protected void findViews(View convertView) {
            iv_item_shop_img = (ImageView) convertView.findViewById(R.id.iv_item_shop_img);
            int width=new GetTelephoneUtils(mContext).getWindowWidth()/ GlobalParams.SHOP_ITEM_IMG_PROPORTION;
            int height=width*GlobalParams.IMG_HEIGHT/GlobalParams.IMG_WIDTH;
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,height);
            iv_item_shop_img.setLayoutParams(params);
            tv_item_shop_name = (TextView) convertView.findViewById(R.id.tv_item_shop_name);
            tv_item_shop_distance = (TextView) convertView.findViewById(R.id.tv_item_shop_distance);
            tv_item_shop_reduce = (TextView) convertView.findViewById(R.id.tv_item_shop_reduce);
            tv_item_shop_check = (TextView) convertView.findViewById(R.id.tv_item_shop_check);
            iv_item_icon_reduce = (ImageView) convertView.findViewById(R.id.iv_item_icon_reduce);
            LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(width/6,height/6);
            iv_item_icon_reduce.setLayoutParams(params1);
        }

        protected void setDatas(NearByMerchantItemBean bean) {
            try {
                imageLoader.displayImage(bean.getLogo(), new BGImageViewAware(iv_item_shop_img),options);
            }catch (Exception e){
                Log.e("retloadingImg(商家logo)",e.toString());
                MobclickAgent.reportError(mContext, LogUtil.getException(e));
            }
            try{
                imageLoader.displayImage(bean.getPromotion().getIcon(),iv_item_icon_reduce,options2);
            }catch (Exception e){
                Log.e("retloadingImg",bean.getPromotion().getIcon());
                MobclickAgent.reportError(mContext, LogUtil.getException(e));
            }

            tv_item_shop_name.setText(bean.getName());
            tv_item_shop_distance.setText(bean.getDistance() + "km");
            tv_item_shop_reduce.setText(bean.getPromotion().getTitle());

        }
    }

    class MyClick implements View.OnClickListener {
        protected int position;

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onShopItemClick(this.position);
            }
        }
    }

    public interface OnShopItemClickListener {
        void onShopItemClick(int position);
    }
}
