package com.wisedu.amp.card.cus.hotApp.model;

import java.io.Serializable;

public class HotAppInfo implements Serializable {
    private String pcAccessUrl;
    private String mobileAccessUrl;

    /**
     * 服务wid
     */
    private String serviceWid;
    /**
     * 服务Id
     */
    private String serviceId;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 是否有权限
     */
    private boolean permission;

    /**
     * 访问数量
     */
    private Integer visitCount;

    /**
     * 是否收藏
     */
    private boolean favorite;

    /**
     * 图标
     */
    private String iconLink;

    private Integer serviceStation;

    private String mobileIconLink;

    public String getServiceId () {
        return serviceId;
    }

    public void setServiceId (String serviceId) {
        this.serviceId = serviceId;
    }

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

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Integer getServiceStation() {
        return serviceStation;
    }

    public void setServiceStation(Integer serviceStation) {
        this.serviceStation = serviceStation;
    }

    public String getMobileIconLink() {
        return mobileIconLink;
    }

    public void setMobileIconLink(String mobileIconLink) {
        this.mobileIconLink = mobileIconLink;
    }
}
