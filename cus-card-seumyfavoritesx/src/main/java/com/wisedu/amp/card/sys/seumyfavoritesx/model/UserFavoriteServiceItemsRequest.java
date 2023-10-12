package com.wisedu.amp.card.sys.seumyfavoritesx.model;

import java.io.Serializable;

public class UserFavoriteServiceItemsRequest implements Serializable {
    private String userId;
    private Integer isRelate;

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
