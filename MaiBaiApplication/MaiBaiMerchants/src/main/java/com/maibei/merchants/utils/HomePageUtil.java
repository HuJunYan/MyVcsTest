package com.maibei.merchants.utils;

import com.maibei.merchants.model.OrderBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxiao on 2016/6/21.
 */
public class HomePageUtil {


    public static List<OrderBean> getOrderList() {
        List<OrderBean> list = new ArrayList<OrderBean>();
        for (int i = 0; i < 11; i++) {
            list.add(new OrderBean("2016-08-11 18:39:21","-1000.0"));
        }
        return list;
    }
}
