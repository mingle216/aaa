package com.wisedu.amp3compatible.model;

/**
 * @Description:
 * @Author: 01120012
 * @Date: 2020/4/3
 */
public class YanBianServiceResponse {
    private String wid;
    private String serviceName;
    private String dutyDept;
    private String serviceContent;
    private String serviceNote;
    private Integer dealLimitDays;
    private Integer onSceneTimes;
    private Integer enabled;
    private String proCondition;
    private String processTime;
    private String serviceBased;
    private Integer listType;
    private Integer dealType;
    private String flowDesc;

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDutyDept() {
        return dutyDept;
    }

    public void setDutyDept(String dutyDept) {
        this.dutyDept = dutyDept;
    }

    public String getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public String getServiceNote() {
        return serviceNote;
    }

    public void setServiceNote(String serviceNote) {
        this.serviceNote = serviceNote;
    }

    public Integer getDealLimitDays() {
        return dealLimitDays;
    }

    public void setDealLimitDays(Integer dealLimitDays) {
        this.dealLimitDays = dealLimitDays;
    }

    public Integer getOnSceneTimes() {
        return onSceneTimes;
    }

    public void setOnSceneTimes(Integer onSceneTimes) {
        this.onSceneTimes = onSceneTimes;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getProCondition() {
        return proCondition;
    }

    public void setProCondition(String proCondition) {
        this.proCondition = proCondition;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getServiceBased() {
        return serviceBased;
    }

    public void setServiceBased(String serviceBased) {
        this.serviceBased = serviceBased;
    }

    public Integer getListType() {
        return listType;
    }

    public void setListType(Integer listType) {
        this.listType = listType;
    }

    public Integer getDealType() {
        return dealType;
    }

    public void setDealType(Integer dealType) {
        this.dealType = dealType;
    }

    public String getFlowDesc() {
        return flowDesc;
    }

    public void setFlowDesc(String flowDesc) {
        this.flowDesc = flowDesc;
    }
}
