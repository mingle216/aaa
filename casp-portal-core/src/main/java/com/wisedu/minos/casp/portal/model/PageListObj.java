package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.PageListObj;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PageListObj
 */
@Validated

public class PageListObj   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("pageName")
  private String pageName = null;

  @JsonProperty("pageCode")
  private String pageCode = null;

  @JsonProperty("pageTitle")
  private String pageTitle = null;

  @JsonProperty("parentId")
  private String parentId = null;

  @JsonProperty("flag")
  private Integer flag = null;

  @JsonProperty("enabledFlag")
  private Integer enabledFlag = null;

  @JsonProperty("pageType")
  private Integer pageType = null;

  @JsonProperty("pageList")
  @Valid
  private List<PageListObj> pageList = null;

  public PageListObj wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 主键wid
   * @return wid
  **/
  @ApiModelProperty(value = "主键wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public PageListObj pageName(String pageName) {
    this.pageName = pageName;
    return this;
  }

  /**
   * 页面名称
   * @return pageName
  **/
  @ApiModelProperty(value = "页面名称")
  
    public String getPageName() {
    return pageName;
  }

  public void setPageName(String pageName) {
    this.pageName = pageName;
  }

  public PageListObj pageCode(String pageCode) {
    this.pageCode = pageCode;
    return this;
  }

  /**
   * 页面代码
   * @return pageCode
  **/
  @ApiModelProperty(value = "页面代码")
  
    public String getPageCode() {
    return pageCode;
  }

  public void setPageCode(String pageCode) {
    this.pageCode = pageCode;
  }

  public PageListObj pageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
    return this;
  }

  /**
   * 浏览器标题
   * @return pageTitle
  **/
  @ApiModelProperty(value = "浏览器标题")
  
    public String getPageTitle() {
    return pageTitle;
  }

  public void setPageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
  }

  public PageListObj parentId(String parentId) {
    this.parentId = parentId;
    return this;
  }

  /**
   * 父级id
   * @return parentId
  **/
  @ApiModelProperty(value = "父级id")
  
    public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public PageListObj flag(Integer flag) {
    this.flag = flag;
    return this;
  }

  /**
   * 是否可选0是 1否
   * @return flag
  **/
  @ApiModelProperty(value = "是否可选0是 1否")
  
    public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  public PageListObj enabledFlag(Integer enabledFlag) {
    this.enabledFlag = enabledFlag;
    return this;
  }

  /**
   * 是否启用（0：否；1：是）
   * @return enabledFlag
  **/
  @ApiModelProperty(value = "是否启用（0：否；1：是）")
  
    public Integer getEnabledFlag() {
    return enabledFlag;
  }

  public void setEnabledFlag(Integer enabledFlag) {
    this.enabledFlag = enabledFlag;
  }

  public PageListObj pageType(Integer pageType) {
    this.pageType = pageType;
    return this;
  }

  /**
   * 页面类型0：功能页1：自由页
   * @return pageType
  **/
  @ApiModelProperty(value = "页面类型0：功能页1：自由页")
  
    public Integer getPageType() {
    return pageType;
  }

  public void setPageType(Integer pageType) {
    this.pageType = pageType;
  }

  public PageListObj pageList(List<PageListObj> pageList) {
    this.pageList = pageList;
    return this;
  }

  public PageListObj addPageListItem(PageListObj pageListItem) {
    if (this.pageList == null) {
      this.pageList = new ArrayList<PageListObj>();
    }
    this.pageList.add(pageListItem);
    return this;
  }

  /**
   * 卡片配置
   * @return pageList
  **/
  @ApiModelProperty(value = "卡片配置")
      @Valid
    public List<PageListObj> getPageList() {
    return pageList;
  }

  public void setPageList(List<PageListObj> pageList) {
    this.pageList = pageList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageListObj pageListObj = (PageListObj) o;
    return Objects.equals(this.wid, pageListObj.wid) &&
        Objects.equals(this.pageName, pageListObj.pageName) &&
        Objects.equals(this.pageCode, pageListObj.pageCode) &&
        Objects.equals(this.pageTitle, pageListObj.pageTitle) &&
        Objects.equals(this.parentId, pageListObj.parentId) &&
        Objects.equals(this.flag, pageListObj.flag) &&
        Objects.equals(this.enabledFlag, pageListObj.enabledFlag) &&
        Objects.equals(this.pageType, pageListObj.pageType) &&
        Objects.equals(this.pageList, pageListObj.pageList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, pageName, pageCode, pageTitle, parentId, flag, enabledFlag, pageType, pageList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageListObj {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    pageName: ").append(toIndentedString(pageName)).append("\n");
    sb.append("    pageCode: ").append(toIndentedString(pageCode)).append("\n");
    sb.append("    pageTitle: ").append(toIndentedString(pageTitle)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    flag: ").append(toIndentedString(flag)).append("\n");
    sb.append("    enabledFlag: ").append(toIndentedString(enabledFlag)).append("\n");
    sb.append("    pageType: ").append(toIndentedString(pageType)).append("\n");
    sb.append("    pageList: ").append(toIndentedString(pageList)).append("\n");
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
