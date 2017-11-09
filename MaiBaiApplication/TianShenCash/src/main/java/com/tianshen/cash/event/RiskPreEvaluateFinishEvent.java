package com.tianshen.cash.event;

/**
 * Created by wang on 2017/11/9.
 * 额度测评完毕后  关闭一些页面
 */

public class RiskPreEvaluateFinishEvent {
    public String type;
    public RiskPreEvaluateFinishEvent(String type){
        this.type = type;
    }
}
