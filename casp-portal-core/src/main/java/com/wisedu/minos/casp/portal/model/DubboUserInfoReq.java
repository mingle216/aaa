package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.DubboExtInfoReq;
import com.wisedu.minos.casp.portal.model.DubboGroupInfo;
import com.wisedu.minos.casp.portal.model.DubboOrgBeanInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DubboUserInfoReq
 */
@Validated

public class DubboUserInfoReq   {
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

  @JsonProperty("activeStatus")
  private String activeStatus = null;

  @JsonProperty("accountStatus")
  private String accountStatus = null;

  @JsonProperty("identityStatus")
  private String identityStatus = null;

  @JsonProperty("userSpeciality")
  private String userSpeciality = null;

  @JsonProperty("userClass")
  private String userClass = null;

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

  @JsonProperty("activateNow")
  private Integer activateNow = null;

  @JsonProperty("pwd")
  private String pwd = null;

  @JsonProperty("pwdExpirePolicy")
  private String pwdExpirePolicy = null;

  @JsonProperty("pwdStrength")
  private String pwdStrength = null;

  @JsonProperty("userIcon")
  private String userIcon = null;

  @JsonProperty("userIconPath")
  private String userIconPath = null;

  @JsonProperty("userIconName")
  private String userIconName = null;

  @JsonProperty("isPrimary")
  private Integer isPrimary = null;

  @JsonProperty("isCreated")
  private Integer isCreated = null;

  @JsonProperty("phoneVerify")
  private Integer phoneVerify = null;

  @JsonProperty("emailVerify")
  private Integer emailVerify = null;

  @JsonProperty("preferredLanguage")
  private String preferredLanguage = null;

  @JsonProperty("accountValidity")
  private String accountValidity = null;

  @JsonProperty("orgWid")
  private String orgWid = null;

  @JsonProperty("orgName")
  private String orgName = null;

  @JsonProperty("orgiName")
  private String orgiName = null;

  @JsonProperty("leaveSchTime")
  private String leaveSchTime = null;

  @JsonProperty("orgs")
  @Valid
  private List<DubboOrgBeanInfo> orgs = null;

  @JsonProperty("groups")
  @Valid
  private List<DubboGroupInfo> groups = null;

  @JsonProperty("ext")
  @Valid
  private List<DubboExtInfoReq> ext = null;

  public DubboUserInfoReq wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 主键Wid
   * @return wid
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public DubboUserInfoReq personWid(String personWid) {
    this.personWid = personWid;
    return this;
  }

  /**
   * 人员主键
   * @return personWid
  **/
  @ApiModelProperty(value = "人员主键")
  
    public String getPersonWid() {
    return personWid;
  }

  public void setPersonWid(String personWid) {
    this.personWid = personWid;
  }

  public DubboUserInfoReq categoryWid(String categoryWid) {
    this.categoryWid = categoryWid;
    return this;
  }

  /**
   * 分类Wid
   * @return categoryWid
  **/
  @ApiModelProperty(value = "分类Wid")
  
    public String getCategoryWid() {
    return categoryWid;
  }

  public void setCategoryWid(String categoryWid) {
    this.categoryWid = categoryWid;
  }

  public DubboUserInfoReq categoryName(String categoryName) {
    this.categoryName = categoryName;
    return this;
  }

  /**
   * 分类名称
   * @return categoryName
  **/
  @ApiModelProperty(value = "分类名称")
  
    public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public DubboUserInfoReq userAccount(String userAccount) {
    this.userAccount = userAccount;
    return this;
  }

  /**
   * 用户账号
   * @return userAccount
  **/
  @ApiModelProperty(value = "用户账号")
  
    public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
  }

  public DubboUserInfoReq userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * 用户名称
   * @return userName
  **/
  @ApiModelProperty(value = "用户名称")
  
    public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public DubboUserInfoReq userAlias(String userAlias) {
    this.userAlias = userAlias;
    return this;
  }

  /**
   * 用户别名
   * @return userAlias
  **/
  @ApiModelProperty(value = "用户别名")
  
    public String getUserAlias() {
    return userAlias;
  }

  public void setUserAlias(String userAlias) {
    this.userAlias = userAlias;
  }

  public DubboUserInfoReq enterSchoolDate(String enterSchoolDate) {
    this.enterSchoolDate = enterSchoolDate;
    return this;
  }

  /**
   * 入学日期
   * @return enterSchoolDate
  **/
  @ApiModelProperty(value = "入学日期")
  
    public String getEnterSchoolDate() {
    return enterSchoolDate;
  }

  public void setEnterSchoolDate(String enterSchoolDate) {
    this.enterSchoolDate = enterSchoolDate;
  }

  public DubboUserInfoReq certTypeWid(String certTypeWid) {
    this.certTypeWid = certTypeWid;
    return this;
  }

  /**
   * 证书类型Wid
   * @return certTypeWid
  **/
  @ApiModelProperty(value = "证书类型Wid")
  
    public String getCertTypeWid() {
    return certTypeWid;
  }

  public void setCertTypeWid(String certTypeWid) {
    this.certTypeWid = certTypeWid;
  }

  public DubboUserInfoReq certCode(String certCode) {
    this.certCode = certCode;
    return this;
  }

  /**
   * 主键Wid
   * @return certCode
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getCertCode() {
    return certCode;
  }

  public void setCertCode(String certCode) {
    this.certCode = certCode;
  }

  public DubboUserInfoReq phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * 主键Wid
   * @return phone
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public DubboUserInfoReq email(String email) {
    this.email = email;
    return this;
  }

  /**
   * 主键Wid
   * @return email
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public DubboUserInfoReq userStatus(String userStatus) {
    this.userStatus = userStatus;
    return this;
  }

  /**
   * 主键Wid
   * @return userStatus
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(String userStatus) {
    this.userStatus = userStatus;
  }

  public DubboUserInfoReq activeStatus(String activeStatus) {
    this.activeStatus = activeStatus;
    return this;
  }

  /**
   * 主键Wid
   * @return activeStatus
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getActiveStatus() {
    return activeStatus;
  }

  public void setActiveStatus(String activeStatus) {
    this.activeStatus = activeStatus;
  }

  public DubboUserInfoReq accountStatus(String accountStatus) {
    this.accountStatus = accountStatus;
    return this;
  }

  /**
   * 主键Wid
   * @return accountStatus
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(String accountStatus) {
    this.accountStatus = accountStatus;
  }

  public DubboUserInfoReq identityStatus(String identityStatus) {
    this.identityStatus = identityStatus;
    return this;
  }

  /**
   * 主键Wid
   * @return identityStatus
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getIdentityStatus() {
    return identityStatus;
  }

  public void setIdentityStatus(String identityStatus) {
    this.identityStatus = identityStatus;
  }

  public DubboUserInfoReq userSpeciality(String userSpeciality) {
    this.userSpeciality = userSpeciality;
    return this;
  }

  /**
   * 主键Wid
   * @return userSpeciality
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getUserSpeciality() {
    return userSpeciality;
  }

  public void setUserSpeciality(String userSpeciality) {
    this.userSpeciality = userSpeciality;
  }

  public DubboUserInfoReq userClass(String userClass) {
    this.userClass = userClass;
    return this;
  }

  /**
   * 主键Wid
   * @return userClass
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getUserClass() {
    return userClass;
  }

  public void setUserClass(String userClass) {
    this.userClass = userClass;
  }

  public DubboUserInfoReq lifeCycle(String lifeCycle) {
    this.lifeCycle = lifeCycle;
    return this;
  }

  /**
   * 主键Wid
   * @return lifeCycle
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getLifeCycle() {
    return lifeCycle;
  }

  public void setLifeCycle(String lifeCycle) {
    this.lifeCycle = lifeCycle;
  }

  public DubboUserInfoReq lifeCycleExpire(String lifeCycleExpire) {
    this.lifeCycleExpire = lifeCycleExpire;
    return this;
  }

  /**
   * 主键Wid
   * @return lifeCycleExpire
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getLifeCycleExpire() {
    return lifeCycleExpire;
  }

  public void setLifeCycleExpire(String lifeCycleExpire) {
    this.lifeCycleExpire = lifeCycleExpire;
  }

  public DubboUserInfoReq deptWid(String deptWid) {
    this.deptWid = deptWid;
    return this;
  }

  /**
   * 主键Wid
   * @return deptWid
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getDeptWid() {
    return deptWid;
  }

  public void setDeptWid(String deptWid) {
    this.deptWid = deptWid;
  }

  public DubboUserInfoReq deptName(String deptName) {
    this.deptName = deptName;
    return this;
  }

  /**
   * 主键Wid
   * @return deptName
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public DubboUserInfoReq sexCode(String sexCode) {
    this.sexCode = sexCode;
    return this;
  }

  /**
   * 主键Wid
   * @return sexCode
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getSexCode() {
    return sexCode;
  }

  public void setSexCode(String sexCode) {
    this.sexCode = sexCode;
  }

  public DubboUserInfoReq birthday(String birthday) {
    this.birthday = birthday;
    return this;
  }

  /**
   * 主键Wid
   * @return birthday
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public DubboUserInfoReq activateNow(Integer activateNow) {
    this.activateNow = activateNow;
    return this;
  }

  /**
   * 主键Wid
   * @return activateNow
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public Integer getActivateNow() {
    return activateNow;
  }

  public void setActivateNow(Integer activateNow) {
    this.activateNow = activateNow;
  }

  public DubboUserInfoReq pwd(String pwd) {
    this.pwd = pwd;
    return this;
  }

  /**
   * 主键Wid
   * @return pwd
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public DubboUserInfoReq pwdExpirePolicy(String pwdExpirePolicy) {
    this.pwdExpirePolicy = pwdExpirePolicy;
    return this;
  }

  /**
   * 主键Wid
   * @return pwdExpirePolicy
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getPwdExpirePolicy() {
    return pwdExpirePolicy;
  }

  public void setPwdExpirePolicy(String pwdExpirePolicy) {
    this.pwdExpirePolicy = pwdExpirePolicy;
  }

  public DubboUserInfoReq pwdStrength(String pwdStrength) {
    this.pwdStrength = pwdStrength;
    return this;
  }

  /**
   * 主键Wid
   * @return pwdStrength
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getPwdStrength() {
    return pwdStrength;
  }

  public void setPwdStrength(String pwdStrength) {
    this.pwdStrength = pwdStrength;
  }

  public DubboUserInfoReq userIcon(String userIcon) {
    this.userIcon = userIcon;
    return this;
  }

  /**
   * 人员授权机构类型：1-教职工 2-学生 3-其他人员
   * @return userIcon
  **/
  @ApiModelProperty(value = "人员授权机构类型：1-教职工 2-学生 3-其他人员")
  
    public String getUserIcon() {
    return userIcon;
  }

  public void setUserIcon(String userIcon) {
    this.userIcon = userIcon;
  }

  public DubboUserInfoReq userIconPath(String userIconPath) {
    this.userIconPath = userIconPath;
    return this;
  }

  /**
   * 是否以树形数据展示 1：是  0：否
   * @return userIconPath
  **/
  @ApiModelProperty(value = "是否以树形数据展示 1：是  0：否")
  
    public String getUserIconPath() {
    return userIconPath;
  }

  public void setUserIconPath(String userIconPath) {
    this.userIconPath = userIconPath;
  }

  public DubboUserInfoReq userIconName(String userIconName) {
    this.userIconName = userIconName;
    return this;
  }

  /**
   * 主键Wid
   * @return userIconName
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getUserIconName() {
    return userIconName;
  }

  public void setUserIconName(String userIconName) {
    this.userIconName = userIconName;
  }

  public DubboUserInfoReq isPrimary(Integer isPrimary) {
    this.isPrimary = isPrimary;
    return this;
  }

  /**
   * 主键Wid
   * @return isPrimary
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public Integer getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(Integer isPrimary) {
    this.isPrimary = isPrimary;
  }

  public DubboUserInfoReq isCreated(Integer isCreated) {
    this.isCreated = isCreated;
    return this;
  }

  /**
   * 主键Wid
   * @return isCreated
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public Integer getIsCreated() {
    return isCreated;
  }

  public void setIsCreated(Integer isCreated) {
    this.isCreated = isCreated;
  }

  public DubboUserInfoReq phoneVerify(Integer phoneVerify) {
    this.phoneVerify = phoneVerify;
    return this;
  }

  /**
   * 主键Wid
   * @return phoneVerify
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public Integer getPhoneVerify() {
    return phoneVerify;
  }

  public void setPhoneVerify(Integer phoneVerify) {
    this.phoneVerify = phoneVerify;
  }

  public DubboUserInfoReq emailVerify(Integer emailVerify) {
    this.emailVerify = emailVerify;
    return this;
  }

  /**
   * 主键Wid
   * @return emailVerify
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public Integer getEmailVerify() {
    return emailVerify;
  }

  public void setEmailVerify(Integer emailVerify) {
    this.emailVerify = emailVerify;
  }

  public DubboUserInfoReq preferredLanguage(String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
    return this;
  }

  /**
   * 主键Wid
   * @return preferredLanguage
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getPreferredLanguage() {
    return preferredLanguage;
  }

  public void setPreferredLanguage(String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
  }

  public DubboUserInfoReq accountValidity(String accountValidity) {
    this.accountValidity = accountValidity;
    return this;
  }

  /**
   * 主键Wid
   * @return accountValidity
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getAccountValidity() {
    return accountValidity;
  }

  public void setAccountValidity(String accountValidity) {
    this.accountValidity = accountValidity;
  }

  public DubboUserInfoReq orgWid(String orgWid) {
    this.orgWid = orgWid;
    return this;
  }

  /**
   * 主键Wid
   * @return orgWid
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getOrgWid() {
    return orgWid;
  }

  public void setOrgWid(String orgWid) {
    this.orgWid = orgWid;
  }

  public DubboUserInfoReq orgName(String orgName) {
    this.orgName = orgName;
    return this;
  }

  /**
   * 主键Wid
   * @return orgName
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public DubboUserInfoReq orgiName(String orgiName) {
    this.orgiName = orgiName;
    return this;
  }

  /**
   * 主键Wid
   * @return orgiName
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getOrgiName() {
    return orgiName;
  }

  public void setOrgiName(String orgiName) {
    this.orgiName = orgiName;
  }

  public DubboUserInfoReq leaveSchTime(String leaveSchTime) {
    this.leaveSchTime = leaveSchTime;
    return this;
  }

  /**
   * 主键Wid
   * @return leaveSchTime
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getLeaveSchTime() {
    return leaveSchTime;
  }

  public void setLeaveSchTime(String leaveSchTime) {
    this.leaveSchTime = leaveSchTime;
  }

  public DubboUserInfoReq orgs(List<DubboOrgBeanInfo> orgs) {
    this.orgs = orgs;
    return this;
  }

  public DubboUserInfoReq addOrgsItem(DubboOrgBeanInfo orgsItem) {
    if (this.orgs == null) {
      this.orgs = new ArrayList<DubboOrgBeanInfo>();
    }
    this.orgs.add(orgsItem);
    return this;
  }

  /**
   * Get orgs
   * @return orgs
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<DubboOrgBeanInfo> getOrgs() {
    return orgs;
  }

  public void setOrgs(List<DubboOrgBeanInfo> orgs) {
    this.orgs = orgs;
  }

  public DubboUserInfoReq groups(List<DubboGroupInfo> groups) {
    this.groups = groups;
    return this;
  }

  public DubboUserInfoReq addGroupsItem(DubboGroupInfo groupsItem) {
    if (this.groups == null) {
      this.groups = new ArrayList<DubboGroupInfo>();
    }
    this.groups.add(groupsItem);
    return this;
  }

  /**
   * Get groups
   * @return groups
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<DubboGroupInfo> getGroups() {
    return groups;
  }

  public void setGroups(List<DubboGroupInfo> groups) {
    this.groups = groups;
  }

  public DubboUserInfoReq ext(List<DubboExtInfoReq> ext) {
    this.ext = ext;
    return this;
  }

  public DubboUserInfoReq addExtItem(DubboExtInfoReq extItem) {
    if (this.ext == null) {
      this.ext = new ArrayList<DubboExtInfoReq>();
    }
    this.ext.add(extItem);
    return this;
  }

  /**
   * Get ext
   * @return ext
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<DubboExtInfoReq> getExt() {
    return ext;
  }

  public void setExt(List<DubboExtInfoReq> ext) {
    this.ext = ext;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DubboUserInfoReq dubboUserInfoReq = (DubboUserInfoReq) o;
    return Objects.equals(this.wid, dubboUserInfoReq.wid) &&
        Objects.equals(this.personWid, dubboUserInfoReq.personWid) &&
        Objects.equals(this.categoryWid, dubboUserInfoReq.categoryWid) &&
        Objects.equals(this.categoryName, dubboUserInfoReq.categoryName) &&
        Objects.equals(this.userAccount, dubboUserInfoReq.userAccount) &&
        Objects.equals(this.userName, dubboUserInfoReq.userName) &&
        Objects.equals(this.userAlias, dubboUserInfoReq.userAlias) &&
        Objects.equals(this.enterSchoolDate, dubboUserInfoReq.enterSchoolDate) &&
        Objects.equals(this.certTypeWid, dubboUserInfoReq.certTypeWid) &&
        Objects.equals(this.certCode, dubboUserInfoReq.certCode) &&
        Objects.equals(this.phone, dubboUserInfoReq.phone) &&
        Objects.equals(this.email, dubboUserInfoReq.email) &&
        Objects.equals(this.userStatus, dubboUserInfoReq.userStatus) &&
        Objects.equals(this.activeStatus, dubboUserInfoReq.activeStatus) &&
        Objects.equals(this.accountStatus, dubboUserInfoReq.accountStatus) &&
        Objects.equals(this.identityStatus, dubboUserInfoReq.identityStatus) &&
        Objects.equals(this.userSpeciality, dubboUserInfoReq.userSpeciality) &&
        Objects.equals(this.userClass, dubboUserInfoReq.userClass) &&
        Objects.equals(this.lifeCycle, dubboUserInfoReq.lifeCycle) &&
        Objects.equals(this.lifeCycleExpire, dubboUserInfoReq.lifeCycleExpire) &&
        Objects.equals(this.deptWid, dubboUserInfoReq.deptWid) &&
        Objects.equals(this.deptName, dubboUserInfoReq.deptName) &&
        Objects.equals(this.sexCode, dubboUserInfoReq.sexCode) &&
        Objects.equals(this.birthday, dubboUserInfoReq.birthday) &&
        Objects.equals(this.activateNow, dubboUserInfoReq.activateNow) &&
        Objects.equals(this.pwd, dubboUserInfoReq.pwd) &&
        Objects.equals(this.pwdExpirePolicy, dubboUserInfoReq.pwdExpirePolicy) &&
        Objects.equals(this.pwdStrength, dubboUserInfoReq.pwdStrength) &&
        Objects.equals(this.userIcon, dubboUserInfoReq.userIcon) &&
        Objects.equals(this.userIconPath, dubboUserInfoReq.userIconPath) &&
        Objects.equals(this.userIconName, dubboUserInfoReq.userIconName) &&
        Objects.equals(this.isPrimary, dubboUserInfoReq.isPrimary) &&
        Objects.equals(this.isCreated, dubboUserInfoReq.isCreated) &&
        Objects.equals(this.phoneVerify, dubboUserInfoReq.phoneVerify) &&
        Objects.equals(this.emailVerify, dubboUserInfoReq.emailVerify) &&
        Objects.equals(this.preferredLanguage, dubboUserInfoReq.preferredLanguage) &&
        Objects.equals(this.accountValidity, dubboUserInfoReq.accountValidity) &&
        Objects.equals(this.orgWid, dubboUserInfoReq.orgWid) &&
        Objects.equals(this.orgName, dubboUserInfoReq.orgName) &&
        Objects.equals(this.orgiName, dubboUserInfoReq.orgiName) &&
        Objects.equals(this.leaveSchTime, dubboUserInfoReq.leaveSchTime) &&
        Objects.equals(this.orgs, dubboUserInfoReq.orgs) &&
        Objects.equals(this.groups, dubboUserInfoReq.groups) &&
        Objects.equals(this.ext, dubboUserInfoReq.ext);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, personWid, categoryWid, categoryName, userAccount, userName, userAlias, enterSchoolDate, certTypeWid, certCode, phone, email, userStatus, activeStatus, accountStatus, identityStatus, userSpeciality, userClass, lifeCycle, lifeCycleExpire, deptWid, deptName, sexCode, birthday, activateNow, pwd, pwdExpirePolicy, pwdStrength, userIcon, userIconPath, userIconName, isPrimary, isCreated, phoneVerify, emailVerify, preferredLanguage, accountValidity, orgWid, orgName, orgiName, leaveSchTime, orgs, groups, ext);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DubboUserInfoReq {\n");
    
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
    sb.append("    activeStatus: ").append(toIndentedString(activeStatus)).append("\n");
    sb.append("    accountStatus: ").append(toIndentedString(accountStatus)).append("\n");
    sb.append("    identityStatus: ").append(toIndentedString(identityStatus)).append("\n");
    sb.append("    userSpeciality: ").append(toIndentedString(userSpeciality)).append("\n");
    sb.append("    userClass: ").append(toIndentedString(userClass)).append("\n");
    sb.append("    lifeCycle: ").append(toIndentedString(lifeCycle)).append("\n");
    sb.append("    lifeCycleExpire: ").append(toIndentedString(lifeCycleExpire)).append("\n");
    sb.append("    deptWid: ").append(toIndentedString(deptWid)).append("\n");
    sb.append("    deptName: ").append(toIndentedString(deptName)).append("\n");
    sb.append("    sexCode: ").append(toIndentedString(sexCode)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    activateNow: ").append(toIndentedString(activateNow)).append("\n");
    sb.append("    pwd: ").append(toIndentedString(pwd)).append("\n");
    sb.append("    pwdExpirePolicy: ").append(toIndentedString(pwdExpirePolicy)).append("\n");
    sb.append("    pwdStrength: ").append(toIndentedString(pwdStrength)).append("\n");
    sb.append("    userIcon: ").append(toIndentedString(userIcon)).append("\n");
    sb.append("    userIconPath: ").append(toIndentedString(userIconPath)).append("\n");
    sb.append("    userIconName: ").append(toIndentedString(userIconName)).append("\n");
    sb.append("    isPrimary: ").append(toIndentedString(isPrimary)).append("\n");
    sb.append("    isCreated: ").append(toIndentedString(isCreated)).append("\n");
    sb.append("    phoneVerify: ").append(toIndentedString(phoneVerify)).append("\n");
    sb.append("    emailVerify: ").append(toIndentedString(emailVerify)).append("\n");
    sb.append("    preferredLanguage: ").append(toIndentedString(preferredLanguage)).append("\n");
    sb.append("    accountValidity: ").append(toIndentedString(accountValidity)).append("\n");
    sb.append("    orgWid: ").append(toIndentedString(orgWid)).append("\n");
    sb.append("    orgName: ").append(toIndentedString(orgName)).append("\n");
    sb.append("    orgiName: ").append(toIndentedString(orgiName)).append("\n");
    sb.append("    leaveSchTime: ").append(toIndentedString(leaveSchTime)).append("\n");
    sb.append("    orgs: ").append(toIndentedString(orgs)).append("\n");
    sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
    sb.append("    ext: ").append(toIndentedString(ext)).append("\n");
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
