package com.wisedu.amp3compatible.model;

/**
 * Created by glhan on 2016/6/21.
 */
public class AppListConditionRequest {
    private String[] appIds;
    private Integer appType; // 0:网页+移动  1:网页  2:移动
    private String serviceTypeName;

    public String[] getAppIds() {
        return appIds;
    }

    public void setAppIds(String[] appIds) {
        this.appIds = appIds;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }
}
