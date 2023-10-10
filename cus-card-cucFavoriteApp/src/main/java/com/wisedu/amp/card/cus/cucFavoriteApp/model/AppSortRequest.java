package com.wisedu.amp.card.cus.cucFavoriteApp.model;

public class AppSortRequest {

    private String userId;
    /**
     * 服务id
     */
    private String[] serviceItemIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getServiceItemIds() {
        return serviceItemIds;
    }

    public void setServiceItemIds(String[] serviceItemIds) {
        this.serviceItemIds = serviceItemIds;
    }

}
