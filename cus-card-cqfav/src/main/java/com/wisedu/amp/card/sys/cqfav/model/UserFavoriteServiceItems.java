package com.wisedu.amp.card.sys.cqfav.model;

import java.util.List;

public class UserFavoriteServiceItems {
    /**
     *
     *    "itemWid": "758624970011049984",
     *       "itemName": "测试服务",
     *       "iconLink": "www.baidu.com",
     *       "itemCategory": "主题1", //服务主题
     *       "itemDept": "责任部门"
     */

    private String itemName;
    private String itemWid;
    private String itemDept;
    private String iconLink;
    private String itemCategory;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemWid() {
        return itemWid;
    }

    public void setItemWid(String itemWid) {
        this.itemWid = itemWid;
    }

    public String getItemDept() {
        return itemDept;
    }

    public void setItemDept(String itemDept) {
        this.itemDept = itemDept;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Integer getOnlineServiceType() {
        return onlineServiceType;
    }

    public void setOnlineServiceType(Integer onlineServiceType) {
        this.onlineServiceType = onlineServiceType;
    }
}
