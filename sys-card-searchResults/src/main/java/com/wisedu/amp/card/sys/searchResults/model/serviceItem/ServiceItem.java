package com.wisedu.amp.card.sys.searchResults.model.serviceItem;

import java.util.List;

public class ServiceItem{

    private String pinyingFirstChar;

    private String itemWid; //服务事项wid
    private String iconLink;//图标
    private String itemName; //服务事项名称
    private String itemPinYin; //名称拼音
    private Integer visitCount; //访问数量
    private String itemCategory; //服务主题
    private String itemDept; //责任部门
    private String score; //评价分数
    private boolean workGuide;//是否展示办事指南
    private boolean favorite;
    private List<String> serviceList;

    private Integer onlineServiceType;

    private String headChar; //首字母
    private String order;//自定义：排序值（用于本地排序）

    public String getItemWid() {
        return itemWid;
    }

    public void setItemWid(String itemWid) {
        this.itemWid = itemWid;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPinYin() {
        return itemPinYin;
    }

    public void setItemPinYin(String itemPinYin) {
        this.itemPinYin = itemPinYin;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemDept() {
        return itemDept;
    }

    public void setItemDept(String itemDept) {
        this.itemDept = itemDept;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isWorkGuide() {
        return workGuide;
    }

    public void setWorkGuide(boolean workGuide) {
        this.workGuide = workGuide;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public List<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<String> serviceList) {
        this.serviceList = serviceList;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPinyingFirstChar() {
        return pinyingFirstChar;
    }

    public void setPinyingFirstChar(String pinyingFirstChar) {
        this.pinyingFirstChar = pinyingFirstChar;
    }

    public String getHeadChar() {
        return headChar;
    }

    public void setHeadChar(String headChar) {
        this.headChar = headChar;
    }

    public Integer getOnlineServiceType() {
        return onlineServiceType;
    }

    public void setOnlineServiceType(Integer onlineServiceType) {
        this.onlineServiceType = onlineServiceType;
    }

}
