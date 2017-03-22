package com.maibai.cash.utils;

import com.maibai.cash.R;
import com.maibai.cash.model.MerchantTypeBean;
import com.maibai.cash.model.ShareTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchi on 2016/6/20.
 */
public class HomePageUtil {


    private static String[] shareNames = {"微信好友", "微信朋友圈", "QQ好友", "QQ空间"};
    private static int[] shareImgs = {R.drawable.share_wechat, R.drawable.share_wechat_friends,
            R.drawable.share_qq_friends, R.drawable.share_qq_space};

    public static List<ShareTypeBean> getShareList() {
        List<ShareTypeBean> list = new ArrayList<ShareTypeBean>();
        for (int i = 0; i < shareNames.length; i++) {
            list.add(new ShareTypeBean(shareNames[i], shareImgs[i]));
        }
        return list;
    }
}
