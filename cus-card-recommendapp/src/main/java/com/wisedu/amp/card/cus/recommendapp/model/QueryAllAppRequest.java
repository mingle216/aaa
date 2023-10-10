package com.wisedu.amp.card.cus.recommendapp.model;

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
public class QueryAllAppRequest {
    private String userId;
    private String searchKey;
    private String pageNumber;
    private String pageSize;
    private String serviceStation;
    private boolean console;
    private boolean permission;

    public boolean isConsole () {
        return console;
    }

    public void setConsole (boolean console) {
        this.console = console;
    }

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

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getServiceStation() {
        return serviceStation;
    }

    public void setServiceStation(String serviceStation) {
        this.serviceStation = serviceStation;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }
}
