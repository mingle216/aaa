package com.wisedu.amp.card.sys.searchResults.model.services;


import java.util.List;

public class ServiceInfo {

    private String serviceWid;
    private String serviceName;
    private String iconLink;
    private boolean favorite;
    private boolean permission;
    private List<ClassifyInfos> classifyInfos;
    private String  pcAccessUrl;
    private String mobileAccessUrl;

    private String guestVisibility;
    private String loginVisibility;

    private String headChar; //首字母
    private String order;//自定义：排序值（用于本地排序）

    public String getPcAccessUrl() {
        return pcAccessUrl;
    }

    public void setPcAccessUrl(String pcAccessUrl) {
        this.pcAccessUrl = pcAccessUrl;
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

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public List<ClassifyInfos> getClassifyInfos() {
        return classifyInfos;
    }

    public void setClassifyInfos(List<ClassifyInfos> classifyInfos) {
        this.classifyInfos = classifyInfos;
    }

    public String getMobileAccessUrl() {
        return mobileAccessUrl;
    }

    public void setMobileAccessUrl(String mobileAccessUrl) {
        this.mobileAccessUrl = mobileAccessUrl;
    }

    public String getHeadChar() {
        return headChar;
    }

    public void setHeadChar(String headChar) {
        this.headChar = headChar;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getGuestVisibility() {
        return guestVisibility;
    }

    public void setGuestVisibility(String guestVisibility) {
        this.guestVisibility = guestVisibility;
    }

    public String getLoginVisibility() {
        return loginVisibility;
    }

    public void setLoginVisibility(String loginVisibility) {
        this.loginVisibility = loginVisibility;
    }
}
