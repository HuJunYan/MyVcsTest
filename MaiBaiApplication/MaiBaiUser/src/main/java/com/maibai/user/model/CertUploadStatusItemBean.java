package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by sbyh on 16/7/5.
 */

public class CertUploadStatusItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type; // 2:工作证, 3:信用卡, 4:户口簿, 5:结婚证, 6:房产证, 7:驾驶证
    private String photo_url;  // 证书下载地址(预留)
    private String status; // 0:未上传, 1:已上传, 2:已上传,审核通过, 3:已上传,审核未通过(0:显示相机, 1,2:对勾, 3:叉叉)

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
