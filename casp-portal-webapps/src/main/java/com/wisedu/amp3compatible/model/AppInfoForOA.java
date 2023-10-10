package com.wisedu.amp3compatible.model;

/**
 * Created by 01116267 on 2018/4/16.
 */
public class AppInfoForOA {
    private String appId;
    private String appShortName;
    private String appName;
    private Integer isPcApp;
    private Integer isMobileApp;
    private String accessUrl;
    private String appIcon;
    private LabelEntity[] serviceTypes;

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppShortName() {
        return appShortName;
    }

    public void setAppShortName(String appShortName) {
        this.appShortName = appShortName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getIsPcApp() {
        return isPcApp;
    }

    public void setIsPcApp(Integer isPcApp) {
        this.isPcApp = isPcApp;
    }

    public Integer getIsMobileApp() {
        return isMobileApp;
    }

    public void setIsMobileApp(Integer isMobileApp) {
        this.isMobileApp = isMobileApp;
    }

    public LabelEntity[] getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(LabelEntity[] serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

}
