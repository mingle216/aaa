package com.wisedu.amp.card.cuscuc.detailsofserviceitems.model;

public class FavoriteRequest {
    private String userId;
    private String userAccount;
    private String isRelate;

    public String getUserAccount () {
        return userAccount;
    }

    public void setUserAccount (String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsRelate() {
        return isRelate;
    }

    public void setIsRelate(String isRelate) {
        this.isRelate = isRelate;
    }
}
