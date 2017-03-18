package com.maibai.user.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.MerchantTypeItemBean;
import com.maibai.user.utils.GetTelephoneUtils;
import com.maibai.user.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zhangchi on 2016/7/9.
 */
public class MerchantAdapter extends CommonAdapter<MerchantTypeItemBean> {
    private DisplayImageOptions options,options2;
    private ImageLoader imageLoader;
    private ImageLoaderUtil imageLoaderUtil;
    public MerchantAdapter(Context context, List<MerchantTypeItemBean> datas) {
        super(context, datas);
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
            holder.findViews(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDatas != null && mDatas.get(position) != null) {
            holder.setDatas(mDatas.get(position));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_item_icon;
        TextView iv_item_name;

        protected void findViews(View convertView) {
            iv_item_icon = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            iv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
        }

        protected void setDatas(MerchantTypeItemBean bean) {
            int width=(int)(new GetTelephoneUtils(mContext).getWindowWidth()/ GlobalParams.SHOP_TYPE_PROPORTION);
            int height=width*GlobalParams.IMG_HEIGHT/GlobalParams.IMG_WIDTH;
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,height);
            iv_item_icon.setLayoutParams(params);
            imageLoader.displayImage(bean.getType_img(), iv_item_icon,options);
            String typeName=bean.getType_name();
            if (null==typeName){
                typeName="";
            }
            iv_item_name.setText(typeName);
            iv_item_name.setTextSize(11.5f);
        }
    }
}
