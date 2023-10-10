package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.OrgInfosBiz;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * OrgInfosBiz
 */
@Validated

public class OrgInfosBiz   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("pWid")
  private String pWid = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("childList")
  @Valid
  private List<OrgInfosBiz> childList = null;

  public OrgInfosBiz wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 机构Id
   * @return wid
  **/
  @ApiModelProperty(value = "机构Id")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public OrgInfosBiz pWid(String pWid) {
    this.pWid = pWid;
    return this;
  }

  /**
   * 父机构Id
   * @return pWid
  **/
  @ApiModelProperty(value = "父机构Id")
  
    public String getPWid() {
    return pWid;
  }

  public void setPWid(String pWid) {
    this.pWid = pWid;
  }

  public OrgInfosBiz name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 机构名称
   * @return name
  **/
  @ApiModelProperty(value = "机构名称")
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OrgInfosBiz code(String code) {
    this.code = code;
    return this;
  }

  /**
   * 机构代码
   * @return code
  **/
  @ApiModelProperty(value = "机构代码")
  
    public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public OrgInfosBiz childList(List<OrgInfosBiz> childList) {
    this.childList = childList;
    return this;
  }

  public OrgInfosBiz addChildListItem(OrgInfosBiz childListItem) {
    if (this.childList == null) {
      this.childList = new ArrayList<OrgInfosBiz>();
    }
    this.childList.add(childListItem);
    return this;
  }

  /**
   * 子机构树信息
   * @return childList
  **/
  @ApiModelProperty(value = "子机构树信息")
      @Valid
    public List<OrgInfosBiz> getChildList() {
    return childList;
  }

  public void setChildList(List<OrgInfosBiz> childList) {
    this.childList = childList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrgInfosBiz orgInfosBiz = (OrgInfosBiz) o;
    return Objects.equals(this.wid, orgInfosBiz.wid) &&
        Objects.equals(this.pWid, orgInfosBiz.pWid) &&
        Objects.equals(this.name, orgInfosBiz.name) &&
        Objects.equals(this.code, orgInfosBiz.code) &&
        Objects.equals(this.childList, orgInfosBiz.childList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, pWid, name, code, childList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrgInfosBiz {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    pWid: ").append(toIndentedString(pWid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    childList: ").append(toIndentedString(childList)).append("\n");
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
