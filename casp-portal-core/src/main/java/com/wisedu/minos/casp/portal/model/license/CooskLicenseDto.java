package com.wisedu.minos.casp.portal.model.license;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * CooskLicenseDto
 */
@Validated

public class CooskLicenseDto   {
  @JsonProperty("caspSim")
  private Boolean caspSim = false;

  @JsonProperty("cooskSimSim")
  private Boolean cooskSimSim = false;

  @JsonProperty("cooskSimEvaluate")
  private Boolean cooskSimEvaluate = false;

  @JsonProperty("cooskSimSort")
  private Boolean cooskSimSort = false;

  @JsonProperty("cooskSimResponsibilities")
  private Boolean cooskSimResponsibilities = false;

  @JsonProperty("cooskSimDeptset")
  private Boolean cooskSimDeptset = false;

  @JsonProperty("cooskSimConfig")
  private Boolean cooskSimConfig = false;

  @JsonProperty("cooskSimObject")
  private Boolean cooskSimObject = false;

  @JsonProperty("cooskSilQuery")
  private Boolean cooskSilQuery = false;

  @JsonProperty("cooskSifFlow")
  private Boolean cooskSifFlow = false;

  @JsonProperty("cooskHokFec")
  private Boolean cooskHokFec = false;

  @JsonProperty("cooskHokhok")
  private Boolean cooskHokhok = false;

  @JsonProperty("cooskSimOneThing")
  private Boolean cooskSimOneThing = false;

  public CooskLicenseDto caspSim(Boolean caspSim) {
    this.caspSim = caspSim;
    return this;
  }

  /**
   * casp事项管理中心
   * @return caspSim
  **/
  @ApiModelProperty(value = "casp事项管理中心")
  
    public Boolean isCaspSim() {
    return caspSim;
  }

  public void setCaspSim(Boolean caspSim) {
    this.caspSim = caspSim;
  }

  public CooskLicenseDto cooskSimSim(Boolean cooskSimSim) {
    this.cooskSimSim = cooskSimSim;
    return this;
  }

  /**
   * coosk事项管理中心事项管理
   * @return cooskSimSim
  **/
  @ApiModelProperty(value = "coosk事项管理中心事项管理")
  
    public Boolean isCooskSimSim() {
    return cooskSimSim;
  }

  public void setCooskSimSim(Boolean cooskSimSim) {
    this.cooskSimSim = cooskSimSim;
  }

  public CooskLicenseDto cooskSimEvaluate(Boolean cooskSimEvaluate) {
    this.cooskSimEvaluate = cooskSimEvaluate;
    return this;
  }

  /**
   * 事项评价
   * @return cooskSimEvaluate
  **/
  @ApiModelProperty(value = "事项评价")
  
    public Boolean isCooskSimEvaluate() {
    return cooskSimEvaluate;
  }

  public void setCooskSimEvaluate(Boolean cooskSimEvaluate) {
    this.cooskSimEvaluate = cooskSimEvaluate;
  }

  public CooskLicenseDto cooskSimSort(Boolean cooskSimSort) {
    this.cooskSimSort = cooskSimSort;
    return this;
  }

  /**
   * 事项排序
   * @return cooskSimSort
  **/
  @ApiModelProperty(value = "事项排序")
  
    public Boolean isCooskSimSort() {
    return cooskSimSort;
  }

  public void setCooskSimSort(Boolean cooskSimSort) {
    this.cooskSimSort = cooskSimSort;
  }

  public CooskLicenseDto cooskSimResponsibilities(Boolean cooskSimResponsibilities) {
    this.cooskSimResponsibilities = cooskSimResponsibilities;
    return this;
  }

  /**
   * 三张清单管理
   * @return cooskSimResponsibilities
  **/
  @ApiModelProperty(value = "三张清单管理")
  
    public Boolean isCooskSimResponsibilities() {
    return cooskSimResponsibilities;
  }

  public void setCooskSimResponsibilities(Boolean cooskSimResponsibilities) {
    this.cooskSimResponsibilities = cooskSimResponsibilities;
  }

  public CooskLicenseDto cooskSimDeptset(Boolean cooskSimDeptset) {
    this.cooskSimDeptset = cooskSimDeptset;
    return this;
  }

  /**
   * 部门事项计划设置
   * @return cooskSimDeptset
  **/
  @ApiModelProperty(value = "部门事项计划设置")
  
    public Boolean isCooskSimDeptset() {
    return cooskSimDeptset;
  }

  public void setCooskSimDeptset(Boolean cooskSimDeptset) {
    this.cooskSimDeptset = cooskSimDeptset;
  }

  public CooskLicenseDto cooskSimConfig(Boolean cooskSimConfig) {
    this.cooskSimConfig = cooskSimConfig;
    return this;
  }

  /**
   * 设置
   * @return cooskSimConfig
  **/
  @ApiModelProperty(value = "设置")
  
    public Boolean isCooskSimConfig() {
    return cooskSimConfig;
  }

  public void setCooskSimConfig(Boolean cooskSimConfig) {
    this.cooskSimConfig = cooskSimConfig;
  }

  public CooskLicenseDto cooskSimObject(Boolean cooskSimObject) {
    this.cooskSimObject = cooskSimObject;
    return this;
  }

  /**
   * 服务对象
   * @return cooskSimObject
  **/
  @ApiModelProperty(value = "服务对象")
  
    public Boolean isCooskSimObject() {
    return cooskSimObject;
  }

  public void setCooskSimObject(Boolean cooskSimObject) {
    this.cooskSimObject = cooskSimObject;
  }

  public CooskLicenseDto cooskSilQuery(Boolean cooskSilQuery) {
    this.cooskSilQuery = cooskSilQuery;
    return this;
  }

  /**
   * 事项模板库
   * @return cooskSilQuery
  **/
  @ApiModelProperty(value = "事项模板库")
  
    public Boolean isCooskSilQuery() {
    return cooskSilQuery;
  }

  public void setCooskSilQuery(Boolean cooskSilQuery) {
    this.cooskSilQuery = cooskSilQuery;
  }

  public CooskLicenseDto cooskSifFlow(Boolean cooskSifFlow) {
    this.cooskSifFlow = cooskSifFlow;
    return this;
  }

  /**
   * 事项全周期
   * @return cooskSifFlow
  **/
  @ApiModelProperty(value = "事项全周期")
  
    public Boolean isCooskSifFlow() {
    return cooskSifFlow;
  }

  public void setCooskSifFlow(Boolean cooskSifFlow) {
    this.cooskSifFlow = cooskSifFlow;
  }

  public CooskLicenseDto cooskHokFec(Boolean cooskHokFec) {
    this.cooskHokFec = cooskHokFec;
    return this;
  }

  /**
   * 效能中心
   * @return cooskHokFec
  **/
  @ApiModelProperty(value = "效能中心")
  
    public Boolean isCooskHokFec() {
    return cooskHokFec;
  }

  public void setCooskHokFec(Boolean cooskHokFec) {
    this.cooskHokFec = cooskHokFec;
  }

  public CooskLicenseDto cooskHokhok(Boolean cooskHokhok) {
    this.cooskHokhok = cooskHokhok;
    return this;
  }

  /**
   * 运营看板
   * @return cooskHokhok
  **/
  @ApiModelProperty(value = "运营看板")
  
    public Boolean isCooskHokhok() {
    return cooskHokhok;
  }

  public void setCooskHokhok(Boolean cooskHokhok) {
    this.cooskHokhok = cooskHokhok;
  }

  public CooskLicenseDto cooskSimOneThing(Boolean cooskSimOneThing) {
    this.cooskSimOneThing = cooskSimOneThing;
    return this;
  }

  /**
   * 一件事管理
   * @return cooskSimOneThing
  **/
  @ApiModelProperty(value = "一件事管理")
  
    public Boolean isCooskSimOneThing() {
    return cooskSimOneThing;
  }

  public void setCooskSimOneThing(Boolean cooskSimOneThing) {
    this.cooskSimOneThing = cooskSimOneThing;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CooskLicenseDto cooskLicenseDto = (CooskLicenseDto) o;
    return Objects.equals(this.caspSim, cooskLicenseDto.caspSim) &&
        Objects.equals(this.cooskSimSim, cooskLicenseDto.cooskSimSim) &&
        Objects.equals(this.cooskSimEvaluate, cooskLicenseDto.cooskSimEvaluate) &&
        Objects.equals(this.cooskSimSort, cooskLicenseDto.cooskSimSort) &&
        Objects.equals(this.cooskSimResponsibilities, cooskLicenseDto.cooskSimResponsibilities) &&
        Objects.equals(this.cooskSimDeptset, cooskLicenseDto.cooskSimDeptset) &&
        Objects.equals(this.cooskSimConfig, cooskLicenseDto.cooskSimConfig) &&
        Objects.equals(this.cooskSimObject, cooskLicenseDto.cooskSimObject) &&
        Objects.equals(this.cooskSilQuery, cooskLicenseDto.cooskSilQuery) &&
        Objects.equals(this.cooskSifFlow, cooskLicenseDto.cooskSifFlow) &&
        Objects.equals(this.cooskHokFec, cooskLicenseDto.cooskHokFec) &&
        Objects.equals(this.cooskHokhok, cooskLicenseDto.cooskHokhok) &&
        Objects.equals(this.cooskSimOneThing, cooskLicenseDto.cooskSimOneThing);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caspSim, cooskSimSim, cooskSimEvaluate, cooskSimSort, cooskSimResponsibilities, cooskSimDeptset, cooskSimConfig, cooskSimObject, cooskSilQuery, cooskSifFlow, cooskHokFec, cooskHokhok, cooskSimOneThing);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CooskLicenseDto {\n");
    
    sb.append("    caspSim: ").append(toIndentedString(caspSim)).append("\n");
    sb.append("    cooskSimSim: ").append(toIndentedString(cooskSimSim)).append("\n");
    sb.append("    cooskSimEvaluate: ").append(toIndentedString(cooskSimEvaluate)).append("\n");
    sb.append("    cooskSimSort: ").append(toIndentedString(cooskSimSort)).append("\n");
    sb.append("    cooskSimResponsibilities: ").append(toIndentedString(cooskSimResponsibilities)).append("\n");
    sb.append("    cooskSimDeptset: ").append(toIndentedString(cooskSimDeptset)).append("\n");
    sb.append("    cooskSimConfig: ").append(toIndentedString(cooskSimConfig)).append("\n");
    sb.append("    cooskSimObject: ").append(toIndentedString(cooskSimObject)).append("\n");
    sb.append("    cooskSilQuery: ").append(toIndentedString(cooskSilQuery)).append("\n");
    sb.append("    cooskSifFlow: ").append(toIndentedString(cooskSifFlow)).append("\n");
    sb.append("    cooskHokFec: ").append(toIndentedString(cooskHokFec)).append("\n");
    sb.append("    cooskHokhok: ").append(toIndentedString(cooskHokhok)).append("\n");
    sb.append("    cooskSimOneThing: ").append(toIndentedString(cooskSimOneThing)).append("\n");
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
