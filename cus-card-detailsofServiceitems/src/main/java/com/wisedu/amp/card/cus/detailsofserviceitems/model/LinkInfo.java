package com.wisedu.amp.card.cus.detailsofserviceitems.model;

/**
 * @author 01319115
 */
public class LinkInfo {
    private String serviceName;
    private String wid;
    private String serviceId;
    private String pcIconUrl;
    private String pcAccessUrl;
    private String serviceVisibility;
    private String guestVisibility;
    private String loginVisibility;
    private String serviceStation;

    public String getServiceId () {
        return serviceId;
    }

    public void setServiceId (String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getPcIconUrl() {
        return pcIconUrl;
    }

    public void setPcIconUrl(String pcIconUrl) {
        this.pcIconUrl = pcIconUrl;
    }

    public String getPcAccessUrl() {
        return pcAccessUrl;
    }

    public void setPcAccessUrl(String pcAccessUrl) {
        this.pcAccessUrl = pcAccessUrl;
    }

    public String getServiceVisibility() {
        return serviceVisibility;
    }

    public void setServiceVisibility(String serviceVisibility) {
        this.serviceVisibility = serviceVisibility;
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

    public String getServiceStation() {
        return serviceStation;
    }

    public void setServiceStation(String serviceStation) {
        this.serviceStation = serviceStation;
    }
}
