package com.tianshen.cash.event;

/**
 * Created by wang on 2017/11/9.
 * 额度测评完毕后  关闭一些页面
 */

public class RiskPreEvaluateFinishEvent {
    public String is_payway;
    public RiskPreEvaluateFinishEvent(String is_payway){
        this.is_payway = is_payway;
    }
}
