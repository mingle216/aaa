package com.wisedu.amp.card.cus.serviceItemCategory1.model;

public class ItemModel {

    private String itemWid;

    private String itemName;

    private String iconLink;

    private Integer isShow;

    private Integer isEnabled;

    private Integer onlineServiceType;

    public Integer getIsEnabled () {
        return isEnabled;
    }

    public void setIsEnabled (Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getIsShow () {
        return isShow;
    }

    public void setIsShow (Integer isShow) {
        this.isShow = isShow;
    }

    public String getItemWid() {
        return itemWid;
    }

    public void setItemWid(String itemWid) {
        this.itemWid = itemWid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public Integer getOnlineServiceType() {
        return onlineServiceType;
    }

    public void setOnlineServiceType(Integer onlineServiceType) {
        this.onlineServiceType = onlineServiceType;
    }
}
