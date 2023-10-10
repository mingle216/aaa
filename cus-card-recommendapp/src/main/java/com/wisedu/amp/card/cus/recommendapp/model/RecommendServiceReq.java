package com.wisedu.amp.card.cus.recommendapp.model;

import java.util.List;

public class RecommendServiceReq {

    private String userAccount = null;

    private List<String> serviceRangeList = null;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public List<String> getServiceRangeList() {
        return serviceRangeList;
    }

    public void setServiceRangeList(List<String> serviceRangeList) {
        this.serviceRangeList = serviceRangeList;
    }
}
