package com.maibai.cash.utils;

import com.maibai.cash.model.WithdrawalsBillItemBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by m on 16-10-20.
 */
public class CashBillListUtil {
    private static Comparator<WithdrawalsBillItemBean> mBillSortByConsumeId;

    public static void sortByConsumeId(List<WithdrawalsBillItemBean> list) {
        if (mBillSortByConsumeId == null) {
            mBillSortByConsumeId = new Comparator<WithdrawalsBillItemBean>() {
                @Override
                public int compare(WithdrawalsBillItemBean bean1, WithdrawalsBillItemBean bean2) {
                    return Integer.parseInt(bean2.getConsume_id(), 10) - Integer.parseInt(bean1.getConsume_id(), 10);
                }
            };
        }
        Collections.sort(list, mBillSortByConsumeId);
    }

}
