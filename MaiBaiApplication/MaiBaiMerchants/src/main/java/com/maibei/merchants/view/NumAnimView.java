package com.maibei.merchants.view;

import android.widget.TextView;

import com.maibei.merchants.utils.NumUtil;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by 14658 on 2016/6/28.
 */
public class NumAnimView {
    private static final int COUNTPERS = 100;

    public static void startAnim(TextView textV, double num) {
        startAnim(textV, num, 500);
    }

    public static void startAnim(TextView textV, double num, long time) {
        if (num == 0) {
            textV.setText(NumUtil.NumberFormat(num, 2));
            return;
        }

        Double[] nums = splitnum(num, (int)((time/1000)*COUNTPERS));

        Counter counter = new Counter(textV, nums, time);

        textV.removeCallbacks(counter);
        textV.post(counter);
    }

    private static Double[] splitnum(double num, int count) {
        Random random = new Random();
        double numtemp = num;
        double sum = 0;
        LinkedList<Double> nums = new LinkedList<Double>();
        nums.add(0.0);
        while (true) {
            float nextFloat = NumUtil.NumberFormatFloat(
                    (random.nextFloat()*num*2f)/(float)count,
                    2);
            System.out.println("next:" + nextFloat);
            if (numtemp - nextFloat >= 0) {
                sum = NumUtil.NumberFormatFloat(sum + nextFloat, 2);
                nums.add(sum);
                numtemp -= nextFloat;
            } else {
                nums.add(num);
                return nums.toArray(new Double[0]);
            }
        }
    }

    static class Counter implements Runnable {

        private final TextView view;
        private Double[] nums;
        private long pertime;

        private int i = 0;

        Counter(TextView view,Double[] nums,long time) {
            this.view = view;
            this.nums = nums;
            this.pertime = time/nums.length;
        }

        @Override
        public void run() {
            if (i>nums.length-1) {
                view.removeCallbacks(Counter.this);
                return;
            }
            view.setText(NumUtil.NumberFormat(nums[i++],2));
            view.removeCallbacks(Counter.this);
            view.postDelayed(Counter.this, pertime);
        }
    }
}
