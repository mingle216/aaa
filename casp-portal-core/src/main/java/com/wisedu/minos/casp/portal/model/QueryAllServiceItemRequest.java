package com.wisedu.minos.casp.portal.model;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title QueryAllAppRequest
 * @Author: hlchen02
 * @Date: 2020/9/8
 */
public class QueryAllServiceItemRequest {
    private String userId;
    private String searchKey;
    private String pageNumber;
    private String pageSize;
    private String srvDeptWids;
    private String categoryWids;
    private String deptWids;
    private String roleWids;
    private Integer isRelate;
    private String wids;

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getSearchKey () {
        return searchKey;
    }

    public void setSearchKey (String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSrvDeptWids () {
        return srvDeptWids;
    }

    public void setSrvDeptWids (String srvDeptWids) {
        this.srvDeptWids = srvDeptWids;
    }

    public String getCategoryWids () {
        return categoryWids;
    }

    public void setCategoryWids (String categoryWids) {
        this.categoryWids = categoryWids;
    }

    public String getDeptWids () {
        return deptWids;
    }

    public void setDeptWids (String deptWids) {
        this.deptWids = deptWids;
    }

    public String getRoleWids () {
        return roleWids;
    }

    public void setRoleWids (String roleWids) {
        this.roleWids = roleWids;
    }

    public Integer getIsRelate () {
        return isRelate;
    }

    public void setIsRelate (Integer isRelate) {
        this.isRelate = isRelate;
    }

    public String getPageNumber () {
        return pageNumber;
    }

    public void setPageNumber (String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageSize () {
        return pageSize;
    }

    public void setPageSize (String pageSize) {
        this.pageSize = pageSize;
    }

    public String getWids() {
        return wids;
    }

    public void setWids(String wids) {
        this.wids = wids;
    }
}
