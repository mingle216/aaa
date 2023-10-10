package com.wisedu.amp.card.cus.cucRecommendapp.model;

import java.io.Serializable;

public class AppInfoBiz extends AppInfo implements Serializable {
    private String serviceWid;

    private String serviceName;

    private String iconLink;

    private String mobileIconLink;

    private boolean favorite;

    private boolean permission;

    private String pcAccessUrl;
    private String mobileAccessUrl;
    private Integer serviceStation;

    public String getServiceWid() {
        return serviceWid;
    }

    public void setServiceWid(String serviceWid) {
        this.serviceWid = serviceWid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    @Override
    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
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

    public String getMobileIconLink() {
        return mobileIconLink;
    }

    public void setMobileIconLink(String mobileIconLink) {
        this.mobileIconLink = mobileIconLink;
    }

    public Integer getServiceStation() {
        return serviceStation;
    }

    public void setServiceStation(Integer serviceStation) {
        this.serviceStation = serviceStation;
    }
}
