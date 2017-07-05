package com.tianshen.cash.event;

/**
 * 强制升级的消息
 */

public class UpdateEvent {

    private String introduction;
    private String download_url;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }


}
