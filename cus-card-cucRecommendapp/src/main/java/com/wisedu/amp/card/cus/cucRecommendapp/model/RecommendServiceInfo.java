package com.wisedu.amp.card.cus.cucRecommendapp.model;

import lombok.Data;

public class RecommendServiceInfo {

    private String serviceWid = null;

    private Double rating = null;

    public String getServiceWid() {
        return serviceWid;
    }

    public void setServiceWid(String serviceWid) {
        this.serviceWid = serviceWid;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
