package com.wisedu.amp.card.sys.searchResults.model.serviceItem;

public class AllserviceItemRequest {
    private String userId;
    private String searchKey;
    private String categoryWids;
    private String deptWids;
    private String roleWids;
    private Integer isRelate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getCategoryWids() {
        return categoryWids;
    }

    public void setCategoryWids(String categoryWids) {
        this.categoryWids = categoryWids;
    }

    public String getDeptWids() {
        return deptWids;
    }

    public void setDeptWids(String deptWids) {
        this.deptWids = deptWids;
    }

    public String getRoleWids() {
        return roleWids;
    }

    public void setRoleWids(String roleWids) {
        this.roleWids = roleWids;
    }

    public Integer getIsRelate() {
        return isRelate;
    }

    public void setIsRelate(Integer isRelate) {
        this.isRelate = isRelate;
    }
}



