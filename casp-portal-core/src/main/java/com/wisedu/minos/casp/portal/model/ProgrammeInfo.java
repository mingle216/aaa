package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ProgrammeInfo
 */
@Validated

public class ProgrammeInfo {
    @JsonProperty("wid")
    private String wid = null;

    @JsonProperty("programmeName")
    private String programmeName = null;

    @JsonProperty("platformType")
    private Integer platformType = null;

    @JsonProperty("templateId")
    private String templateId = null;

    @JsonProperty("pageAmount")
    private Integer pageAmount = null;

    @JsonProperty("pageStatus")
    private Integer pageStatus = null;

    @JsonProperty("logoUrl")
    private String logoUrl = null;

    @JsonProperty("templateConfig")
    private String templateConfig = null;

    @JsonProperty("updateTime")
    private String updateTime = null;

    @JsonProperty("createTime")
    private String createTime = null;

    @JsonProperty("sideFlag")
    private Integer sideFlag = null;

    @JsonProperty("isShowPcService")
    private Integer isShowPcService = null;

    @JsonProperty("siteWid")
    private String siteWid = null;

    @JsonProperty("isPcServiceSupported")
    private Integer isPcServiceSupported = null;


    public ProgrammeInfo wid (String wid) {
        this.wid = wid;
        return this;
    }

    /**
     * 主键wid
     *
     * @return wid
     **/
    @ApiModelProperty(value = "主键wid")

    public String getWid () {
        return wid;
    }

    public void setWid (String wid) {
        this.wid = wid;
    }

    public ProgrammeInfo programmeName (String programmeName) {
        this.programmeName = programmeName;
        return this;
    }

    /**
     * 展示方案名称
     *
     * @return programmeName
     **/
    @ApiModelProperty(value = "展示方案名称")

    public String getProgrammeName () {
        return programmeName;
    }

    public void setProgrammeName (String programmeName) {
        this.programmeName = programmeName;
    }

    public ProgrammeInfo platformType (Integer platformType) {
        this.platformType = platformType;
        return this;
    }

    /**
     * 适配终端0：PC1：移动端
     *
     * @return platformType
     **/
    @ApiModelProperty(value = "适配终端0：PC1：移动端")

    public Integer getPlatformType () {
        return platformType;
    }

    public void setPlatformType (Integer platformType) {
        this.platformType = platformType;
    }

    public ProgrammeInfo templateId (String templateId) {
        this.templateId = templateId;
        return this;
    }

    /**
     * 模板id
     *
     * @return templateId
     **/
    @ApiModelProperty(value = "模板id")

    public String getTemplateId () {
        return templateId;
    }

    public void setTemplateId (String templateId) {
        this.templateId = templateId;
    }

    public ProgrammeInfo pageAmount (Integer pageAmount) {
        this.pageAmount = pageAmount;
        return this;
    }

    /**
     * 页面数量
     *
     * @return pageAmount
     **/
    @ApiModelProperty(value = "页面数量")

    public Integer getPageAmount () {
        return pageAmount;
    }

    public void setPageAmount (Integer pageAmount) {
        this.pageAmount = pageAmount;
    }

    public ProgrammeInfo pageStatus (Integer pageStatus) {
        this.pageStatus = pageStatus;
        return this;
    }

    /**
     * 状态0：启用1：停用
     *
     * @return pageStatus
     **/
    @ApiModelProperty(value = "状态0：启用1：停用")

    public Integer getPageStatus () {
        return pageStatus;
    }

    public void setPageStatus (Integer pageStatus) {
        this.pageStatus = pageStatus;
    }

    public ProgrammeInfo logoUrl (String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    /**
     * 校徽/LOGO地址
     *
     * @return logoUrl
     **/
    @ApiModelProperty(value = "校徽/LOGO地址")

    public String getLogoUrl () {
        return logoUrl;
    }

    public void setLogoUrl (String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public ProgrammeInfo templateConfig (String templateConfig) {
        this.templateConfig = templateConfig;
        return this;
    }

    /**
     * 模板配置
     *
     * @return templateConfig
     **/
    @ApiModelProperty(value = "模板配置")

    public String getTemplateConfig () {
        return templateConfig;
    }

    public void setTemplateConfig (String templateConfig) {
        this.templateConfig = templateConfig;
    }

    public ProgrammeInfo updateTime (String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    /**
     * 更新时间
     *
     * @return updateTime
     **/
    @ApiModelProperty(value = "更新时间")

    public String getUpdateTime () {
        return updateTime;
    }

    public void setUpdateTime (String updateTime) {
        this.updateTime = updateTime;
    }

    public ProgrammeInfo createTime (String createTime) {
        this.createTime = createTime;
        return this;
    }

    /**
     * 创建时间
     *
     * @return createTime
     **/
    @ApiModelProperty(value = "创建时间")

    public String getCreateTime () {
        return createTime;
    }

    public void setCreateTime (String createTime) {
        this.createTime = createTime;
    }

    public ProgrammeInfo sideFlag (Integer sideFlag) {
        this.sideFlag = sideFlag;
        return this;
    }

    /**
     * 是否展示左/右侧栏0都不展示 1只展示左侧栏2只展示右侧栏3、都展示
     *
     * @return sideFlag
     **/
    @ApiModelProperty(value = "是否展示左/右侧栏0都不展示 1只展示左侧栏2只展示右侧栏3、都展示")

    public Integer getSideFlag () {
        return sideFlag;
    }

    public void setSideFlag (Integer sideFlag) {
        this.sideFlag = sideFlag;
    }

    public ProgrammeInfo isShowPcService (Integer isShowPcService) {
        this.isShowPcService = isShowPcService;
        return this;
    }

    /**
     * 是否显示pc端服务
     *
     * @return isShowPcService
     **/
    @ApiModelProperty(value = "是否显示pc端服务")

    public Integer getIsShowPcService () {
        return isShowPcService;
    }

    public void setIsShowPcService (Integer isShowPcService) {
        this.isShowPcService = isShowPcService;
    }

    public ProgrammeInfo siteWid (String siteWid) {
        this.siteWid = siteWid;
        return this;
    }

    /**
     * 站点wid
     *
     * @return siteWid
     **/
    @ApiModelProperty(value = "站点wid")

    public String getSiteWid () {
        return siteWid;
    }

    public void setSiteWid (String siteWid) {
        this.siteWid = siteWid;
    }

    public ProgrammeInfo getIsPcServiceSupported (Integer getIsPcServiceSupported) {
        this.isPcServiceSupported = getIsPcServiceSupported;
        return this;
    }

    /**
     * 是否支持打开PC应用 1:支持 0: 不支持
     *
     * @return isPcServiceSupported
     */
    @ApiModelProperty(value = "是否支持打开PC应用 1:支持 0: 不支持")
    public Integer getIsPcServiceSupported () {
        return isPcServiceSupported;
    }

    public void setIsPcServiceSupported (Integer isPcServiceSupported) {
        this.isPcServiceSupported = isPcServiceSupported;
    }


    @Override
    public boolean equals (Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        ProgrammeInfo programmeInfo = ( ProgrammeInfo ) o;
        return Objects.equals(this.wid, programmeInfo.wid) &&
                Objects.equals(this.programmeName, programmeInfo.programmeName) &&
                Objects.equals(this.platformType, programmeInfo.platformType) &&
                Objects.equals(this.templateId, programmeInfo.templateId) &&
                Objects.equals(this.pageAmount, programmeInfo.pageAmount) &&
                Objects.equals(this.pageStatus, programmeInfo.pageStatus) &&
                Objects.equals(this.logoUrl, programmeInfo.logoUrl) &&
                Objects.equals(this.templateConfig, programmeInfo.templateConfig) &&
                Objects.equals(this.updateTime, programmeInfo.updateTime) &&
                Objects.equals(this.createTime, programmeInfo.createTime) &&
                Objects.equals(this.sideFlag, programmeInfo.sideFlag) &&
                Objects.equals(this.isShowPcService, programmeInfo.isShowPcService) &&
                Objects.equals(this.isPcServiceSupported, programmeInfo.isPcServiceSupported) &&
                Objects.equals(this.siteWid, programmeInfo.siteWid);
    }

    @Override
    public int hashCode () {
        return Objects.hash(wid, programmeName, platformType, templateId, pageAmount, pageStatus, logoUrl, templateConfig, updateTime, createTime, sideFlag, isShowPcService, siteWid, isPcServiceSupported);
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("class ProgrammeInfo {\n");

        sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
        sb.append("    programmeName: ").append(toIndentedString(programmeName)).append("\n");
        sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
        sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
        sb.append("    pageAmount: ").append(toIndentedString(pageAmount)).append("\n");
        sb.append("    pageStatus: ").append(toIndentedString(pageStatus)).append("\n");
        sb.append("    logoUrl: ").append(toIndentedString(logoUrl)).append("\n");
        sb.append("    templateConfig: ").append(toIndentedString(templateConfig)).append("\n");
        sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
        sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
        sb.append("    sideFlag: ").append(toIndentedString(sideFlag)).append("\n");
        sb.append("    isShowPcService: ").append(toIndentedString(isShowPcService)).append("\n");
        sb.append("    siteWid: ").append(toIndentedString(siteWid)).append("\n");
        sb.append("    isPcServiceSupported: ").append(toIndentedString(isPcServiceSupported)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString (Object o) {
        if ( o == null ) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
