package com.wisedu.amp.card.sys.cqdetail.model;

public class DetailRequest {
    private String serviceItemWid;
    private String langCountry;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServiceItemWid() {
        return serviceItemWid;
    }

    public void setServiceItemWid(String serviceItemWid) {
        this.serviceItemWid = serviceItemWid;
    }

    public String getLangCountry() {
        return langCountry;
    }

    public void setLangCountry(String langCountry) {
        this.langCountry = langCountry;
    }
}
