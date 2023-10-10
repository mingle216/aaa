package com.wisedu.amp.card.cus.serviceItemCategory1.model;

public class ServiceItemCategoryRequest {

    //用户id 游客为guest
    private String userId;
    //用户账号
    private String userAccount;

    //是否与我相关
    private int isRelate;

    //服务分类wid
    private String categoryWid;

    //服务对象wid
    private String roleWid;

    // 是否按照热门排序
    private  boolean orderByVisitCount;

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

    public int getIsRelate() {
        return isRelate;
    }

    public void setIsRelate(int isRelate) {
        this.isRelate = isRelate;
    }

    public String getCategoryWid() {
        return categoryWid;
    }

    public void setCategoryWid(String categoryWid) {
        this.categoryWid = categoryWid;
    }

    public String getRoleWid() {
        return roleWid;
    }

    public void setRoleWid(String roleWid) {
        this.roleWid = roleWid;
    }

    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isOrderByVisitCount () {
        return orderByVisitCount;
    }

    public void setOrderByVisitCount (boolean orderByVisitCount) {
        this.orderByVisitCount = orderByVisitCount;
    }
}
