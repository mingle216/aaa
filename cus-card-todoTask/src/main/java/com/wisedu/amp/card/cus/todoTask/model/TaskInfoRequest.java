package com.wisedu.amp.card.cus.todoTask.model;

import java.util.List;

public class TaskInfoRequest {
    private String userId;
    private String flag;
    private String sourceWid;
    private int pageSize;
    private String appId;
    private String processInstanceId;
    private String searchkeyword;
    private String state;
    private int pageNumber;
    private List<String> runPlatformList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRunPlatformList() {
        return runPlatformList;
    }

    public void setRunPlatformList(List<String> runPlatformList) {
        this.runPlatformList = runPlatformList;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSourceWid() {
        return sourceWid;
    }

    public void setSourceWid(String sourceWid) {
        this.sourceWid = sourceWid;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getSearchkeyword() {
        return searchkeyword;
    }

    public void setSearchkeyword(String searchkeyword) {
        this.searchkeyword = searchkeyword;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
