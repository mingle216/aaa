package com.wisedu.amp.card.sys.mygorydetail.model;

public class ServiceItemCategoryRequest {

    //用户id 游客为guest
    private String userId;

    //是否与我相关
    private int isRelate;

    //服务分类wid
    private String categoryWid;

    //服务对象wid
    private String roleWid;

    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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
}
