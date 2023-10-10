package com.wisedu.amp.card.cus.detailsofserviceitems.model;

public class EvaluationRequest {
    private String userId;
    private String userName;
    private String serviceWid;
    private Integer serviceManner;
    private Integer serviceSpeed;
    private Integer totalSatisfy;
    private Integer eventIntegrity;
    private String suggest;
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getServiceWid() {
        return serviceWid;
    }

    public void setServiceWid(String serviceWid) {
        this.serviceWid = serviceWid;
    }

    public int getServiceManner() {
        return serviceManner;
    }

    public void setServiceManner(Integer serviceManner) {
        this.serviceManner = serviceManner;
    }

    public int getServiceSpeed() {
        return serviceSpeed;
    }

    public void setServiceSpeed(Integer serviceSpeed) {
        this.serviceSpeed = serviceSpeed;
    }

    public int getTotalSatisfy() {
        return totalSatisfy;
    }

    public void setTotalSatisfy(Integer totalSatisfy) {
        this.totalSatisfy = totalSatisfy;
    }

    public int getEventIntegrity() {
        return eventIntegrity;
    }

    public void setEventIntegrity(Integer eventIntegrity) {
        this.eventIntegrity = eventIntegrity;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
