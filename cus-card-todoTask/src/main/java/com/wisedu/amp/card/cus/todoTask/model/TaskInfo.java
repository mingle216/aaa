package com.wisedu.amp.card.cus.todoTask.model;

import java.io.Serializable;

public class TaskInfo implements Serializable {
    private String WID;
    private String COUNT;
    private String SOURCE_NAME;
    private String taskId;
    private String createTime;
    private String appId;
    private String receive_on;
    private int todoCount;
    private int doneCount;
    private boolean isDisplayData;
    private String subject;
    private boolean isSingleData;
    private String receiveOnReadTime;
    private String processName;
    private String receive_on_1;
    private String formUrl;
    private String createdByDepts;
    private String author;
    private String distribute;
    private String processInstanceId;
    private int priority;
    private String click_change_status;
    private String formMobileUrl;
    private String bizDomain;
    private String queryTodoTask;
    private String processInstanceImageUrl;
    private Integer isRead;
    private Integer isNew;
    private Integer isTop;
    private Boolean isFavorite;

    public Boolean getFavorite () {
        return isFavorite;
    }

    public void setIsFavorite (Boolean favorite) {
        isFavorite = favorite;
    }

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

    public String getReceive_on() {
        return receive_on;
    }

    public void setReceive_on(String receive_on) {
        this.receive_on = receive_on;
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

    public String getReceive_on_1() {
        return receive_on_1;
    }

    public void setReceive_on_1(String receive_on_1) {
        this.receive_on_1 = receive_on_1;
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

    public String getClick_change_status() {
        return click_change_status;
    }

    public void setClick_change_status(String click_change_status) {
        this.click_change_status = click_change_status;
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

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
    }

    public String getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(String COUNT) {
        this.COUNT = COUNT;
    }

    public String getSOURCE_NAME() {
        return SOURCE_NAME;
    }

    public void setSOURCE_NAME(String SOURCE_NAME) {
        this.SOURCE_NAME = SOURCE_NAME;
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

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Integer getIsNew () {
        return isNew;
    }

    public void setIsNew (Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getIsTop () {
        return isTop;
    }

    public void setIsTop (Integer isTop) {
        this.isTop = isTop;
    }
}
