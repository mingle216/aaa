package com.wisedu.minos.casp.portal.model;

import java.io.Serializable;

public class TaskInfo implements Serializable {
    private String wid;
    private String count;
    private String sourceName;
    private String taskId;
    private String createTime;
    private String appId;
    private String receiveOn;
    private int todoCount;
    private int doneCount;
    private boolean isDisplayData;
    private String subject;
    private boolean isSingleData;
    private String receiveOnReadTime;
    private String processName;
    private String receiveOnStr;
    private String formUrl;
    private String createdByDepts;
    private String author;
    private String distribute;
    private String processInstanceId;
    private int priority;
    private String clickChangeStatus;
    private String formMobileUrl;
    private String bizDomain;
    private String queryTodoTask;
    private String processInstanceImageUrl;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getReceiveOn () {
        return receiveOn;
    }

    public void setReceiveOn (String receiveOn) {
        this.receiveOn = receiveOn;
    }

    public int getTodoCount() {
        return todoCount;
    }

    public void setTodoCount(int todoCount) {
        this.todoCount = todoCount;
    }

    public boolean isDisplayData() {
        return isDisplayData;
    }

    public void setDisplayData(boolean displayData) {
        isDisplayData = displayData;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isSingleData() {
        return isSingleData;
    }

    public void setSingleData(boolean singleData) {
        isSingleData = singleData;
    }

    public String getReceiveOnReadTime() {
        return receiveOnReadTime;
    }

    public void setReceiveOnReadTime(String receiveOnReadTime) {
        this.receiveOnReadTime = receiveOnReadTime;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getReceiveOnStr () {
        return receiveOnStr;
    }

    public void setReceiveOnStr (String receiveOnStr) {
        this.receiveOnStr = receiveOnStr;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getCreatedByDepts() {
        return createdByDepts;
    }

    public void setCreatedByDepts(String createdByDepts) {
        this.createdByDepts = createdByDepts;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDistribute() {
        return distribute;
    }

    public void setDistribute(String distribute) {
        this.distribute = distribute;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getClickChangeStatus () {
        return clickChangeStatus;
    }

    public void setClickChangeStatus (String clickChangeStatus) {
        this.clickChangeStatus = clickChangeStatus;
    }

    public String getFormMobileUrl() {
        return formMobileUrl;
    }

    public void setFormMobileUrl(String formMobileUrl) {
        this.formMobileUrl = formMobileUrl;
    }

    public String getBizDomain() {
        return bizDomain;
    }

    public void setBizDomain(String bizDomain) {
        this.bizDomain = bizDomain;
    }

    public int getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(int doneCount) {
        this.doneCount = doneCount;
    }

    public String getWid () {
        return wid;
    }

    public void setWid (String wid) {
        this.wid = wid;
    }

    public String getCount () {
        return count;
    }

    public void setCount (String count) {
        this.count = count;
    }

    public String getSourceName () {
        return sourceName;
    }

    public void setSourceName (String sourceName) {
        this.sourceName = sourceName;
    }

    public String getQueryTodoTask() {
        return queryTodoTask;
    }

    public void setQueryTodoTask(String queryTodoTask) {
        this.queryTodoTask = queryTodoTask;
    }

    public String getProcessInstanceImageUrl() {
        return processInstanceImageUrl;
    }

    public void setProcessInstanceImageUrl(String processInstanceImageUrl) {
        this.processInstanceImageUrl = processInstanceImageUrl;
    }
}
