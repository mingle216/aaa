package com.wisedu.amp3compatible.model;

/**
 * Created by glhan on 2016/6/21.
 */
public class GetGroupByUserAndAppRequest {
    private String userId;
    private String appId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
