package com.wisedu.amp.card.cus.hotApp.model;

import java.io.Serializable;

public class AppInfo implements Serializable {
    private String appId;
    private String appName;
    private String appIcon;
    private String isCanUse;
    private boolean favorite;
    private String middleIcon;
    private Integer useCount;
    private String pcAccessUrl;
    private String mobileAccessUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getIsCanUse() {
        return isCanUse;
    }

    public void setIsCanUse(String isCanUse) {
        this.isCanUse = isCanUse;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getMiddleIcon() {
        return middleIcon;
    }

    public void setMiddleIcon(String middleIcon) {
        this.middleIcon = middleIcon;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public String getPcAccessUrl() {
        return pcAccessUrl;
    }

    public void setPcAccessUrl(String pcAccessUrl) {
        this.pcAccessUrl = pcAccessUrl;
    }

    public String getMobileAccessUrl() {
        return mobileAccessUrl;
    }

    public void setMobileAccessUrl(String mobileAccessUrl) {
        this.mobileAccessUrl = mobileAccessUrl;
    }
}
