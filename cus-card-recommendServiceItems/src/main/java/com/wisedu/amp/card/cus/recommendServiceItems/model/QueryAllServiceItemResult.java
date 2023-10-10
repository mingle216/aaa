package com.wisedu.amp.card.cus.recommendServiceItems.model;

import com.wisedu.minos.casp.portal.model.RecommendServiceItems;

import java.util.List;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title QueryAllAppResult
 * @Author: hlchen02
 * @Date: 2020/9/8
 */
public class QueryAllServiceItemResult {
    private String hasLogin;
    private String contextPath;
    private String pageNumber;
    private String pageSize;
    private String totalRecords;
    private List<RecommendServiceItems> appList;

    public String getHasLogin () {
        return hasLogin;
    }

    public void setHasLogin (String hasLogin) {
        this.hasLogin = hasLogin;
    }

    public String getContextPath () {
        return contextPath;
    }

    public void setContextPath (String contextPath) {
        this.contextPath = contextPath;
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

    public String getTotalRecords () {
        return totalRecords;
    }

    public void setTotalRecords (String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<RecommendServiceItems> getAppList () {
        return appList;
    }

    public void setAppList (List<RecommendServiceItems> appList) {
        this.appList = appList;
    }
}
