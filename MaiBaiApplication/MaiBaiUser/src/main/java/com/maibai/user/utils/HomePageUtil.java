package com.maibai.user.utils;

import com.maibai.user.R;
import com.maibai.user.model.MerchantTypeBean;
import com.maibai.user.model.ShareTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchi on 2016/6/20.
 */
public class HomePageUtil {
    private static String[] names = {"饭店餐饮", "水果生鲜", "女性专区", "超市", "电动车", "婚纱摄影",
            "家居建材", "教育培训", "汽车服务", "其他"};
    private static int[] imgs = {R.mipmap.icon_home_restaurant, R.mipmap.icon_home_fruit,
            R.mipmap.icon_woman, R.mipmap.icon_home_supermarket,
            R.mipmap.icon_home_ev, R.mipmap.icon_home_photography,
            R.mipmap.icon_home_home, R.mipmap.icon_home_study,
            R.mipmap.icon_home_car, R.mipmap.icon_home_else};

    public static List<MerchantTypeBean> getImageList() {
        List<MerchantTypeBean> list = new ArrayList<MerchantTypeBean>();
        for (int i = 0; i < names.length; i++) {
            list.add(new MerchantTypeBean(names[i], imgs[i]));
        }
        return list;
    }

    private static String[] shareNames = {"微信好友", "微信朋友圈", "QQ好友", "QQ空间"};
    private static int[] shareImgs = {R.mipmap.share_wechat, R.mipmap.share_wechat_friends,
            R.mipmap.share_qq_friends, R.mipmap.share_qq_space};

    public static List<ShareTypeBean> getShareList() {
        List<ShareTypeBean> list = new ArrayList<ShareTypeBean>();
        for (int i = 0; i < shareNames.length; i++) {
            list.add(new ShareTypeBean(shareNames[i], shareImgs[i]));
        }
        return list;
    }
}
