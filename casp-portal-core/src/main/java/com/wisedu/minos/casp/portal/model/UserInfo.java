package com.wisedu.minos.casp.portal.model;

import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.i18n.Lang;
import com.wisedu.minos.casp.portal.model.DubboGroupBeanInfo;
import com.wisedu.minos.casp.portal.model.DubboOrgBeanInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UserInfo
 */
@Validated

public class UserInfo implements Serializable {
  private static final long serialVersionUID = -4035754716493701436L;
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("personWid")
  private String personWid = null;

  @JsonProperty("categoryWid")
  private String categoryWid = null;

  @JsonProperty("categoryName")
  private String categoryName = null;

  @JsonProperty("userAccount")
  private String userAccount = null;

  @JsonProperty("userName")
  private String userName = null;

  @JsonProperty("userAlias")
  private String userAlias = null;

  @JsonProperty("enterSchoolDate")
  private String enterSchoolDate = null;

  @JsonProperty("certTypeWid")
  private String certTypeWid = null;

  @JsonProperty("certCode")
  private String certCode = null;

  @JsonProperty("phone")
  private String phone = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("userStatus")
  private String userStatus = null;

  @JsonProperty("lifeCycle")
  private String lifeCycle = null;

  @JsonProperty("lifeCycleExpire")
  private String lifeCycleExpire = null;

  @JsonProperty("deptWid")
  private String deptWid = null;

  @JsonProperty("deptName")
  private String deptName = null;

  @JsonProperty("sexCode")
  private String sexCode = null;

  @JsonProperty("birthday")
  private String birthday = null;

  @JsonProperty("isPrimary")
  private String isPrimary = null;

  @JsonProperty("userIcon")
  private String userIcon = null;

  @JsonProperty("preferredLanguage")
  private String preferredLanguage = null;

  @JsonProperty("userTag")
  private String userTag = null;

  @JsonProperty("orgs")
  @Valid
  private List<DubboOrgBeanInfo> orgs = null;

  @JsonProperty("groups")
  @Valid
  private List<DubboGroupBeanInfo> groups = null;

  @JsonProperty("supportLanguages")
  @Valid
  private List<Lang> supportLanguages = null;

  public UserInfo wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 人员主键wid
   * @return wid
  **/
  @ApiModelProperty(value = "人员主键wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public UserInfo personWid(String personWid) {
    this.personWid = personWid;
    return this;
  }

  /**
   * 人员表中的WID
   * @return personWid
  **/
  @ApiModelProperty(value = "人员表中的WID")
  
    public String getPersonWid() {
    return personWid;
  }

  public void setPersonWid(String personWid) {
    this.personWid = personWid;
  }

  public UserInfo categoryWid(String categoryWid) {
    this.categoryWid = categoryWid;
    return this;
  }

  /**
   * 人员表中的WID
   * @return categoryWid
  **/
  @ApiModelProperty(value = "人员表中的WID")
  
    public String getCategoryWid() {
    return categoryWid;
  }

  public void setCategoryWid(String categoryWid) {
    this.categoryWid = categoryWid;
  }

  public UserInfo categoryName(String categoryName) {
    this.categoryName = categoryName;
    return this;
  }

  /**
   * 用户分类名称
   * @return categoryName
  **/
  @ApiModelProperty(value = "用户分类名称")
  
    public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public UserInfo userAccount(String userAccount) {
    this.userAccount = userAccount;
    return this;
  }

  /**
   * 用户名(账户工号)
   * @return userAccount
  **/
  @ApiModelProperty(value = "用户名(账户工号)")
  
    public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
  }

  public UserInfo userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * 姓名
   * @return userName
  **/
  @ApiModelProperty(value = "姓名")
  
    public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public UserInfo userAlias(String userAlias) {
    this.userAlias = userAlias;
    return this;
  }

  /**
   * 别名
   * @return userAlias
  **/
  @ApiModelProperty(value = "别名")
  
    public String getUserAlias() {
    return userAlias;
  }

  public void setUserAlias(String userAlias) {
    this.userAlias = userAlias;
  }

  public UserInfo enterSchoolDate(String enterSchoolDate) {
    this.enterSchoolDate = enterSchoolDate;
    return this;
  }

  /**
   * 入校年份
   * @return enterSchoolDate
  **/
  @ApiModelProperty(value = "入校年份")
  
    public String getEnterSchoolDate() {
    return enterSchoolDate;
  }

  public void setEnterSchoolDate(String enterSchoolDate) {
    this.enterSchoolDate = enterSchoolDate;
  }

  public UserInfo certTypeWid(String certTypeWid) {
    this.certTypeWid = certTypeWid;
    return this;
  }

  /**
   * 身份证件类型代码 0:居民身份证,1:护照,2:港澳台居民身份证,4:旅行证据,5:其他
   * @return certTypeWid
  **/
  @ApiModelProperty(value = "身份证件类型代码 0:居民身份证,1:护照,2:港澳台居民身份证,4:旅行证据,5:其他")
  
    public String getCertTypeWid() {
    return certTypeWid;
  }

  public void setCertTypeWid(String certTypeWid) {
    this.certTypeWid = certTypeWid;
  }

  public UserInfo certCode(String certCode) {
    this.certCode = certCode;
    return this;
  }

  /**
   * 身份证件号
   * @return certCode
  **/
  @ApiModelProperty(value = "身份证件号")
  
    public String getCertCode() {
    return certCode;
  }

  public void setCertCode(String certCode) {
    this.certCode = certCode;
  }

  public UserInfo phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * 手机号
   * @return phone
  **/
  @ApiModelProperty(value = "手机号")
  
    public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public UserInfo email(String email) {
    this.email = email;
    return this;
  }

  /**
   * 邮箱
   * @return email
  **/
  @ApiModelProperty(value = "邮箱")
  
    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserInfo userStatus(String userStatus) {
    this.userStatus = userStatus;
    return this;
  }

  /**
   * 用户状态 0 未激活 1 正常 2 冻结 3 锁定 4 禁用
   * @return userStatus
  **/
  @ApiModelProperty(value = "用户状态 0 未激活 1 正常 2 冻结 3 锁定 4 禁用")
  
    public String getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(String userStatus) {
    this.userStatus = userStatus;
  }

  public UserInfo lifeCycle(String lifeCycle) {
    this.lifeCycle = lifeCycle;
    return this;
  }

  /**
   * 生命周期 0 未入校 1 在校 2 已离校
   * @return lifeCycle
  **/
  @ApiModelProperty(value = "生命周期 0 未入校 1 在校 2 已离校")
  
    public String getLifeCycle() {
    return lifeCycle;
  }

  public void setLifeCycle(String lifeCycle) {
    this.lifeCycle = lifeCycle;
  }

  public UserInfo lifeCycleExpire(String lifeCycleExpire) {
    this.lifeCycleExpire = lifeCycleExpire;
    return this;
  }

  /**
   * 生命周期阶段到期时间
   * @return lifeCycleExpire
  **/
  @ApiModelProperty(value = "生命周期阶段到期时间")
  
    public String getLifeCycleExpire() {
    return lifeCycleExpire;
  }

  public void setLifeCycleExpire(String lifeCycleExpire) {
    this.lifeCycleExpire = lifeCycleExpire;
  }

  public UserInfo deptWid(String deptWid) {
    this.deptWid = deptWid;
    return this;
  }

  /**
   * 部门WID
   * @return deptWid
  **/
  @ApiModelProperty(value = "部门WID")
  
    public String getDeptWid() {
    return deptWid;
  }

  public void setDeptWid(String deptWid) {
    this.deptWid = deptWid;
  }

  public UserInfo deptName(String deptName) {
    this.deptName = deptName;
    return this;
  }

  /**
   * 所在部门名称多级使用-分割
   * @return deptName
  **/
  @ApiModelProperty(value = "所在部门名称多级使用-分割")
  
    public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public UserInfo sexCode(String sexCode) {
    this.sexCode = sexCode;
    return this;
  }

  /**
   * 性别代码 0 男 1 女
   * @return sexCode
  **/
  @ApiModelProperty(value = "性别代码 0 男 1 女")
  
    public String getSexCode() {
    return sexCode;
  }

  public void setSexCode(String sexCode) {
    this.sexCode = sexCode;
  }

  public UserInfo birthday(String birthday) {
    this.birthday = birthday;
    return this;
  }

  /**
   * 出生日期
   * @return birthday
  **/
  @ApiModelProperty(value = "出生日期")
  
    public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public UserInfo isPrimary(String isPrimary) {
    this.isPrimary = isPrimary;
    return this;
  }

  /**
   * 是否是主帐号 1 是 0 否
   * @return isPrimary
  **/
  @ApiModelProperty(value = "是否是主帐号 1 是 0 否")
  
    public String getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(String isPrimary) {
    this.isPrimary = isPrimary;
  }

  public UserInfo userIcon(String userIcon) {
    this.userIcon = userIcon;
    return this;
  }

  /**
   * 用户头像的Base64编码
   * @return userIcon
  **/
  @ApiModelProperty(value = "用户头像的Base64编码")
  
    public String getUserIcon() {
    return userIcon;
  }

  public void setUserIcon(String userIcon) {
    this.userIcon = userIcon;
  }

  public UserInfo preferredLanguage(String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
    return this;
  }

  /**
   * 首选语言
   * @return preferredLanguage
  **/
  @ApiModelProperty(value = "首选语言")
  
    public String getPreferredLanguage() {
    return preferredLanguage;
  }

  public void setPreferredLanguage(String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
  }

  public UserInfo userTag(String userTag) {
    this.userTag = userTag;
    return this;
  }

  /**
   * 人员标签
   * @return userTag
  **/
  @ApiModelProperty(value = "人员标签")
  
    public String getUserTag() {
    return userTag;
  }

  public void setUserTag(String userTag) {
    this.userTag = userTag;
  }

  public UserInfo orgs(List<DubboOrgBeanInfo> orgs) {
    this.orgs = orgs;
    return this;
  }

  public UserInfo addOrgsItem(DubboOrgBeanInfo orgsItem) {
    if (this.orgs == null) {
      this.orgs = new ArrayList<DubboOrgBeanInfo>();
    }
    this.orgs.add(orgsItem);
    return this;
  }

  /**
   * 所属机构
   * @return orgs
  **/
  @ApiModelProperty(value = "所属机构")
      @Valid
    public List<DubboOrgBeanInfo> getOrgs() {
    return orgs;
  }

  public void setOrgs(List<DubboOrgBeanInfo> orgs) {
    this.orgs = orgs;
  }

  public UserInfo groups(List<DubboGroupBeanInfo> groups) {
    this.groups = groups;
    return this;
  }

  public UserInfo addGroupsItem(DubboGroupBeanInfo groupsItem) {
    if (this.groups == null) {
      this.groups = new ArrayList<DubboGroupBeanInfo>();
    }
    this.groups.add(groupsItem);
    return this;
  }

  /**
   * 所属用户组
   * @return groups
  **/
  @ApiModelProperty(value = "所属用户组")
      @Valid
    public List<DubboGroupBeanInfo> getGroups() {
    return groups;
  }

  public void setGroups(List<DubboGroupBeanInfo> groups) {
    this.groups = groups;
  }

  /**
   * 门户支持的语言
   * @return groups
  **/
  @ApiModelProperty(value = "门户支持的语言")
      @Valid
    public List<Lang> getSupportLanguages() {
    return supportLanguages;
  }

  public void setSupportLanguages(List<Lang> supportLanguages) {
    this.supportLanguages = supportLanguages;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfo userInfo = (UserInfo) o;
    return Objects.equals(this.wid, userInfo.wid) &&
        Objects.equals(this.personWid, userInfo.personWid) &&
        Objects.equals(this.categoryWid, userInfo.categoryWid) &&
        Objects.equals(this.categoryName, userInfo.categoryName) &&
        Objects.equals(this.userAccount, userInfo.userAccount) &&
        Objects.equals(this.userName, userInfo.userName) &&
        Objects.equals(this.userAlias, userInfo.userAlias) &&
        Objects.equals(this.enterSchoolDate, userInfo.enterSchoolDate) &&
        Objects.equals(this.certTypeWid, userInfo.certTypeWid) &&
        Objects.equals(this.certCode, userInfo.certCode) &&
        Objects.equals(this.phone, userInfo.phone) &&
        Objects.equals(this.email, userInfo.email) &&
        Objects.equals(this.userStatus, userInfo.userStatus) &&
        Objects.equals(this.lifeCycle, userInfo.lifeCycle) &&
        Objects.equals(this.lifeCycleExpire, userInfo.lifeCycleExpire) &&
        Objects.equals(this.deptWid, userInfo.deptWid) &&
        Objects.equals(this.deptName, userInfo.deptName) &&
        Objects.equals(this.sexCode, userInfo.sexCode) &&
        Objects.equals(this.birthday, userInfo.birthday) &&
        Objects.equals(this.isPrimary, userInfo.isPrimary) &&
        Objects.equals(this.userIcon, userInfo.userIcon) &&
        Objects.equals(this.preferredLanguage, userInfo.preferredLanguage) &&
        Objects.equals(this.userTag, userInfo.userTag) &&
        Objects.equals(this.orgs, userInfo.orgs) &&
        Objects.equals(this.groups, userInfo.groups)&&
        Objects.equals(this.supportLanguages, userInfo.supportLanguages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, personWid, categoryWid, categoryName, userAccount, userName, userAlias, enterSchoolDate, certTypeWid, certCode, phone, email, userStatus, lifeCycle, lifeCycleExpire, deptWid, deptName, sexCode, birthday, isPrimary, userIcon, preferredLanguage, userTag, orgs, groups,supportLanguages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    personWid: ").append(toIndentedString(personWid)).append("\n");
    sb.append("    categoryWid: ").append(toIndentedString(categoryWid)).append("\n");
    sb.append("    categoryName: ").append(toIndentedString(categoryName)).append("\n");
    sb.append("    userAccount: ").append(toIndentedString(userAccount)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    userAlias: ").append(toIndentedString(userAlias)).append("\n");
    sb.append("    enterSchoolDate: ").append(toIndentedString(enterSchoolDate)).append("\n");
    sb.append("    certTypeWid: ").append(toIndentedString(certTypeWid)).append("\n");
    sb.append("    certCode: ").append(toIndentedString(certCode)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    userStatus: ").append(toIndentedString(userStatus)).append("\n");
    sb.append("    lifeCycle: ").append(toIndentedString(lifeCycle)).append("\n");
    sb.append("    lifeCycleExpire: ").append(toIndentedString(lifeCycleExpire)).append("\n");
    sb.append("    deptWid: ").append(toIndentedString(deptWid)).append("\n");
    sb.append("    deptName: ").append(toIndentedString(deptName)).append("\n");
    sb.append("    sexCode: ").append(toIndentedString(sexCode)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    isPrimary: ").append(toIndentedString(isPrimary)).append("\n");
    sb.append("    userIcon: ").append(toIndentedString(userIcon)).append("\n");
    sb.append("    preferredLanguage: ").append(toIndentedString(preferredLanguage)).append("\n");
    sb.append("    userTag: ").append(toIndentedString(userTag)).append("\n");
    sb.append("    orgs: ").append(toIndentedString(orgs)).append("\n");
    sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
    sb.append("    supportLanguages: ").append(toIndentedString(supportLanguages)).append("\n");
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
