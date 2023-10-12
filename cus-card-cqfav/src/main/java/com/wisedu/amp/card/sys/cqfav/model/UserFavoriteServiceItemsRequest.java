package com.wisedu.amp.card.sys.cqfav.model;

import java.io.Serializable;

public class UserFavoriteServiceItemsRequest implements Serializable {
    private String userId;
    private String userAccount;
    private Integer isRelate;

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

    public Integer getIsRelate() {
        return isRelate;
    }

    public void setIsRelate(Integer isRelate) {
        this.isRelate = isRelate;
    }
}
