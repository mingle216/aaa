package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.DubboGroupRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DubboGroupInfo
 */
@Validated

public class DubboGroupInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("groupId")
  private String groupId = null;

  @JsonProperty("groupType")
  private String groupType = null;

  @JsonProperty("categoryWid")
  private String categoryWid = null;

  @JsonProperty("dynamicSql")
  private String dynamicSql = null;

  @JsonProperty("datasourceWid")
  private String datasourceWid = null;

  @JsonProperty("datasourceDeleted")
  private Boolean datasourceDeleted = null;

  @JsonProperty("datasourceName")
  private String datasourceName = null;

  @JsonProperty("domainWid")
  private String domainWid = null;

  @JsonProperty("peopleNumber")
  private Integer peopleNumber = null;

  @JsonProperty("orderIndex")
  private Integer orderIndex = null;

  @JsonProperty("groupRemark")
  private String groupRemark = null;

  @JsonProperty("autoSyn")
  private Integer autoSyn = null;

  @JsonProperty("syncPeriod")
  private Integer syncPeriod = null;

  @JsonProperty("syncPeriodUnit")
  private String syncPeriodUnit = null;

  @JsonProperty("firstSyncTime")
  private String firstSyncTime = null;

  @JsonProperty("lastSyncTime")
  private String lastSyncTime = null;

  @JsonProperty("containRefList")
  private Integer containRefList = null;

  @JsonProperty("ruleDesc")
  private String ruleDesc = null;

  @JsonProperty("userRefList")
  @Valid
  private List<String> userRefList = null;

  @JsonProperty("ruleList")
  @Valid
  private List<DubboGroupRule> ruleList = null;

  public DubboGroupInfo wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 主键id
   * @return wid
  **/
  @ApiModelProperty(value = "主键id")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public DubboGroupInfo name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 用户组名称
   * @return name
  **/
  @ApiModelProperty(value = "用户组名称")
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DubboGroupInfo groupId(String groupId) {
    this.groupId = groupId;
    return this;
  }

  /**
   * 用户组id
   * @return groupId
  **/
  @ApiModelProperty(value = "用户组id")
  
    public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public DubboGroupInfo groupType(String groupType) {
    this.groupType = groupType;
    return this;
  }

  /**
   * 用户组类型
   * @return groupType
  **/
  @ApiModelProperty(value = "用户组类型")
  
    public String getGroupType() {
    return groupType;
  }

  public void setGroupType(String groupType) {
    this.groupType = groupType;
  }

  public DubboGroupInfo categoryWid(String categoryWid) {
    this.categoryWid = categoryWid;
    return this;
  }

  /**
   * Get categoryWid
   * @return categoryWid
  **/
  @ApiModelProperty(value = "")
  
    public String getCategoryWid() {
    return categoryWid;
  }

  public void setCategoryWid(String categoryWid) {
    this.categoryWid = categoryWid;
  }

  public DubboGroupInfo dynamicSql(String dynamicSql) {
    this.dynamicSql = dynamicSql;
    return this;
  }

  /**
   * Get dynamicSql
   * @return dynamicSql
  **/
  @ApiModelProperty(value = "")
  
    public String getDynamicSql() {
    return dynamicSql;
  }

  public void setDynamicSql(String dynamicSql) {
    this.dynamicSql = dynamicSql;
  }

  public DubboGroupInfo datasourceWid(String datasourceWid) {
    this.datasourceWid = datasourceWid;
    return this;
  }

  /**
   * Get datasourceWid
   * @return datasourceWid
  **/
  @ApiModelProperty(value = "")
  
    public String getDatasourceWid() {
    return datasourceWid;
  }

  public void setDatasourceWid(String datasourceWid) {
    this.datasourceWid = datasourceWid;
  }

  public DubboGroupInfo datasourceDeleted(Boolean datasourceDeleted) {
    this.datasourceDeleted = datasourceDeleted;
    return this;
  }

  /**
   * Get datasourceDeleted
   * @return datasourceDeleted
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isDatasourceDeleted() {
    return datasourceDeleted;
  }

  public void setDatasourceDeleted(Boolean datasourceDeleted) {
    this.datasourceDeleted = datasourceDeleted;
  }

  public DubboGroupInfo datasourceName(String datasourceName) {
    this.datasourceName = datasourceName;
    return this;
  }

  /**
   * Get datasourceName
   * @return datasourceName
  **/
  @ApiModelProperty(value = "")
  
    public String getDatasourceName() {
    return datasourceName;
  }

  public void setDatasourceName(String datasourceName) {
    this.datasourceName = datasourceName;
  }

  public DubboGroupInfo domainWid(String domainWid) {
    this.domainWid = domainWid;
    return this;
  }

  /**
   * Get domainWid
   * @return domainWid
  **/
  @ApiModelProperty(value = "")
  
    public String getDomainWid() {
    return domainWid;
  }

  public void setDomainWid(String domainWid) {
    this.domainWid = domainWid;
  }

  public DubboGroupInfo peopleNumber(Integer peopleNumber) {
    this.peopleNumber = peopleNumber;
    return this;
  }

  /**
   * Get peopleNumber
   * @return peopleNumber
  **/
  @ApiModelProperty(value = "")
  
    public Integer getPeopleNumber() {
    return peopleNumber;
  }

  public void setPeopleNumber(Integer peopleNumber) {
    this.peopleNumber = peopleNumber;
  }

  public DubboGroupInfo orderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
    return this;
  }

  /**
   * Get orderIndex
   * @return orderIndex
  **/
  @ApiModelProperty(value = "")
  
    public Integer getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
  }

  public DubboGroupInfo groupRemark(String groupRemark) {
    this.groupRemark = groupRemark;
    return this;
  }

  /**
   * Get groupRemark
   * @return groupRemark
  **/
  @ApiModelProperty(value = "")
  
    public String getGroupRemark() {
    return groupRemark;
  }

  public void setGroupRemark(String groupRemark) {
    this.groupRemark = groupRemark;
  }

  public DubboGroupInfo autoSyn(Integer autoSyn) {
    this.autoSyn = autoSyn;
    return this;
  }

  /**
   * Get autoSyn
   * @return autoSyn
  **/
  @ApiModelProperty(value = "")
  
    public Integer getAutoSyn() {
    return autoSyn;
  }

  public void setAutoSyn(Integer autoSyn) {
    this.autoSyn = autoSyn;
  }

  public DubboGroupInfo syncPeriod(Integer syncPeriod) {
    this.syncPeriod = syncPeriod;
    return this;
  }

  /**
   * Get syncPeriod
   * @return syncPeriod
  **/
  @ApiModelProperty(value = "")
  
    public Integer getSyncPeriod() {
    return syncPeriod;
  }

  public void setSyncPeriod(Integer syncPeriod) {
    this.syncPeriod = syncPeriod;
  }

  public DubboGroupInfo syncPeriodUnit(String syncPeriodUnit) {
    this.syncPeriodUnit = syncPeriodUnit;
    return this;
  }

  /**
   * Get syncPeriodUnit
   * @return syncPeriodUnit
  **/
  @ApiModelProperty(value = "")
  
    public String getSyncPeriodUnit() {
    return syncPeriodUnit;
  }

  public void setSyncPeriodUnit(String syncPeriodUnit) {
    this.syncPeriodUnit = syncPeriodUnit;
  }

  public DubboGroupInfo firstSyncTime(String firstSyncTime) {
    this.firstSyncTime = firstSyncTime;
    return this;
  }

  /**
   * Get firstSyncTime
   * @return firstSyncTime
  **/
  @ApiModelProperty(value = "")
  
    public String getFirstSyncTime() {
    return firstSyncTime;
  }

  public void setFirstSyncTime(String firstSyncTime) {
    this.firstSyncTime = firstSyncTime;
  }

  public DubboGroupInfo lastSyncTime(String lastSyncTime) {
    this.lastSyncTime = lastSyncTime;
    return this;
  }

  /**
   * Get lastSyncTime
   * @return lastSyncTime
  **/
  @ApiModelProperty(value = "")
  
    public String getLastSyncTime() {
    return lastSyncTime;
  }

  public void setLastSyncTime(String lastSyncTime) {
    this.lastSyncTime = lastSyncTime;
  }

  public DubboGroupInfo containRefList(Integer containRefList) {
    this.containRefList = containRefList;
    return this;
  }

  /**
   * Get containRefList
   * @return containRefList
  **/
  @ApiModelProperty(value = "")
  
    public Integer getContainRefList() {
    return containRefList;
  }

  public void setContainRefList(Integer containRefList) {
    this.containRefList = containRefList;
  }

  public DubboGroupInfo ruleDesc(String ruleDesc) {
    this.ruleDesc = ruleDesc;
    return this;
  }

  /**
   * Get ruleDesc
   * @return ruleDesc
  **/
  @ApiModelProperty(value = "")
  
    public String getRuleDesc() {
    return ruleDesc;
  }

  public void setRuleDesc(String ruleDesc) {
    this.ruleDesc = ruleDesc;
  }

  public DubboGroupInfo userRefList(List<String> userRefList) {
    this.userRefList = userRefList;
    return this;
  }

  public DubboGroupInfo addUserRefListItem(String userRefListItem) {
    if (this.userRefList == null) {
      this.userRefList = new ArrayList<String>();
    }
    this.userRefList.add(userRefListItem);
    return this;
  }

  /**
   * Get userRefList
   * @return userRefList
  **/
  @ApiModelProperty(value = "")
  
    public List<String> getUserRefList() {
    return userRefList;
  }

  public void setUserRefList(List<String> userRefList) {
    this.userRefList = userRefList;
  }

  public DubboGroupInfo ruleList(List<DubboGroupRule> ruleList) {
    this.ruleList = ruleList;
    return this;
  }

  public DubboGroupInfo addRuleListItem(DubboGroupRule ruleListItem) {
    if (this.ruleList == null) {
      this.ruleList = new ArrayList<DubboGroupRule>();
    }
    this.ruleList.add(ruleListItem);
    return this;
  }

  /**
   * Get ruleList
   * @return ruleList
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<DubboGroupRule> getRuleList() {
    return ruleList;
  }

  public void setRuleList(List<DubboGroupRule> ruleList) {
    this.ruleList = ruleList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DubboGroupInfo dubboGroupInfo = (DubboGroupInfo) o;
    return Objects.equals(this.wid, dubboGroupInfo.wid) &&
        Objects.equals(this.name, dubboGroupInfo.name) &&
        Objects.equals(this.groupId, dubboGroupInfo.groupId) &&
        Objects.equals(this.groupType, dubboGroupInfo.groupType) &&
        Objects.equals(this.categoryWid, dubboGroupInfo.categoryWid) &&
        Objects.equals(this.dynamicSql, dubboGroupInfo.dynamicSql) &&
        Objects.equals(this.datasourceWid, dubboGroupInfo.datasourceWid) &&
        Objects.equals(this.datasourceDeleted, dubboGroupInfo.datasourceDeleted) &&
        Objects.equals(this.datasourceName, dubboGroupInfo.datasourceName) &&
        Objects.equals(this.domainWid, dubboGroupInfo.domainWid) &&
        Objects.equals(this.peopleNumber, dubboGroupInfo.peopleNumber) &&
        Objects.equals(this.orderIndex, dubboGroupInfo.orderIndex) &&
        Objects.equals(this.groupRemark, dubboGroupInfo.groupRemark) &&
        Objects.equals(this.autoSyn, dubboGroupInfo.autoSyn) &&
        Objects.equals(this.syncPeriod, dubboGroupInfo.syncPeriod) &&
        Objects.equals(this.syncPeriodUnit, dubboGroupInfo.syncPeriodUnit) &&
        Objects.equals(this.firstSyncTime, dubboGroupInfo.firstSyncTime) &&
        Objects.equals(this.lastSyncTime, dubboGroupInfo.lastSyncTime) &&
        Objects.equals(this.containRefList, dubboGroupInfo.containRefList) &&
        Objects.equals(this.ruleDesc, dubboGroupInfo.ruleDesc) &&
        Objects.equals(this.userRefList, dubboGroupInfo.userRefList) &&
        Objects.equals(this.ruleList, dubboGroupInfo.ruleList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, name, groupId, groupType, categoryWid, dynamicSql, datasourceWid, datasourceDeleted, datasourceName, domainWid, peopleNumber, orderIndex, groupRemark, autoSyn, syncPeriod, syncPeriodUnit, firstSyncTime, lastSyncTime, containRefList, ruleDesc, userRefList, ruleList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DubboGroupInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    groupType: ").append(toIndentedString(groupType)).append("\n");
    sb.append("    categoryWid: ").append(toIndentedString(categoryWid)).append("\n");
    sb.append("    dynamicSql: ").append(toIndentedString(dynamicSql)).append("\n");
    sb.append("    datasourceWid: ").append(toIndentedString(datasourceWid)).append("\n");
    sb.append("    datasourceDeleted: ").append(toIndentedString(datasourceDeleted)).append("\n");
    sb.append("    datasourceName: ").append(toIndentedString(datasourceName)).append("\n");
    sb.append("    domainWid: ").append(toIndentedString(domainWid)).append("\n");
    sb.append("    peopleNumber: ").append(toIndentedString(peopleNumber)).append("\n");
    sb.append("    orderIndex: ").append(toIndentedString(orderIndex)).append("\n");
    sb.append("    groupRemark: ").append(toIndentedString(groupRemark)).append("\n");
    sb.append("    autoSyn: ").append(toIndentedString(autoSyn)).append("\n");
    sb.append("    syncPeriod: ").append(toIndentedString(syncPeriod)).append("\n");
    sb.append("    syncPeriodUnit: ").append(toIndentedString(syncPeriodUnit)).append("\n");
    sb.append("    firstSyncTime: ").append(toIndentedString(firstSyncTime)).append("\n");
    sb.append("    lastSyncTime: ").append(toIndentedString(lastSyncTime)).append("\n");
    sb.append("    containRefList: ").append(toIndentedString(containRefList)).append("\n");
    sb.append("    ruleDesc: ").append(toIndentedString(ruleDesc)).append("\n");
    sb.append("    userRefList: ").append(toIndentedString(userRefList)).append("\n");
    sb.append("    ruleList: ").append(toIndentedString(ruleList)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
