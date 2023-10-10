package com.wisedu.amp.card.sys.searchResults.model.serviceItem;

public class CheckUserServiceGrantReq {
    private String userId;
    private String serviceWid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServiceWid() {
        return serviceWid;
    }

    public void setServiceWid(String serviceWid) {
        this.serviceWid = serviceWid;
    }
}
