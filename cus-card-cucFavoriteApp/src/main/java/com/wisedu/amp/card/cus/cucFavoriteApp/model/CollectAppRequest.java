package com.wisedu.amp.card.cus.cucFavoriteApp.model;

public class CollectAppRequest {
    private String userId;
    /**
     * 服务事项id
     */
    private String serviceItemId;
    /**
     * 0：取消收藏 1：收藏
     */
    private String flag;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(String serviceItemId) {
        this.serviceItemId = serviceItemId;
    }
}
