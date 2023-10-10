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
 * ProgrammeInfoRes
 */
@Validated

public class ProgrammeInfoRes {
    @JsonProperty("wid")
    private String wid = null;

    @JsonProperty("programmeName")
    private String programmeName = null;

    @JsonProperty("platformType")
    private Integer platformType = null;

    @JsonProperty("templateId")
    private String templateId = null;

    @JsonProperty("pageAmount")
    private String pageAmount = null;

    @JsonProperty("pageStatus")
    private String pageStatus = null;

    @JsonProperty("logoUrl")
    private String logoUrl = null;

    @JsonProperty("templateConfig")
    private String templateConfig = null;

    @JsonProperty("isShowPcService")
    private Integer isShowPcService = null;

    @JsonProperty("sideFlag")
    private Integer sideFlag = null;

    @JsonProperty("siteWid")
    private String siteWid = null;

    @JsonProperty("isPcServiceSupported")
    private Integer isPcServiceSupported = null;

    public ProgrammeInfoRes wid (String wid) {
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

    public ProgrammeInfoRes programmeName (String programmeName) {
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

    public ProgrammeInfoRes platformType (Integer platformType) {
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

    public ProgrammeInfoRes templateId (String templateId) {
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

    public ProgrammeInfoRes pageAmount (String pageAmount) {
        this.pageAmount = pageAmount;
        return this;
    }

    /**
     * 页面数量
     *
     * @return pageAmount
     **/
    @ApiModelProperty(value = "页面数量")

    public String getPageAmount () {
        return pageAmount;
    }

    public void setPageAmount (String pageAmount) {
        this.pageAmount = pageAmount;
    }

    public ProgrammeInfoRes pageStatus (String pageStatus) {
        this.pageStatus = pageStatus;
        return this;
    }

    /**
     * 状态0：启用1：停用
     *
     * @return pageStatus
     **/
    @ApiModelProperty(value = "状态0：启用1：停用")

    public String getPageStatus () {
        return pageStatus;
    }

    public void setPageStatus (String pageStatus) {
        this.pageStatus = pageStatus;
    }

    public ProgrammeInfoRes logoUrl (String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    /**
     * 校徽&LOGO地址
     *
     * @return logoUrl
     **/
    @ApiModelProperty(value = "校徽&LOGO地址")

    public String getLogoUrl () {
        return logoUrl;
    }

    public void setLogoUrl (String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public ProgrammeInfoRes templateConfig (String templateConfig) {
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

    public ProgrammeInfoRes isShowPcService (Integer isShowPcService) {
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

    public ProgrammeInfoRes sideFlag (Integer sideFlag) {
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

    public ProgrammeInfoRes siteWid (String siteWid) {
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
    /**
     * 站点wid
     *
     * @return isPcServiceSupported
     **/
    @ApiModelProperty(value = "移动端支持打开PC应用")
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
        ProgrammeInfoRes programmeInfoRes = ( ProgrammeInfoRes ) o;
        return Objects.equals(this.wid, programmeInfoRes.wid) &&
                Objects.equals(this.programmeName, programmeInfoRes.programmeName) &&
                Objects.equals(this.platformType, programmeInfoRes.platformType) &&
                Objects.equals(this.templateId, programmeInfoRes.templateId) &&
                Objects.equals(this.pageAmount, programmeInfoRes.pageAmount) &&
                Objects.equals(this.pageStatus, programmeInfoRes.pageStatus) &&
                Objects.equals(this.logoUrl, programmeInfoRes.logoUrl) &&
                Objects.equals(this.templateConfig, programmeInfoRes.templateConfig) &&
                Objects.equals(this.isShowPcService, programmeInfoRes.isShowPcService) &&
                Objects.equals(this.sideFlag, programmeInfoRes.sideFlag) &&
                Objects.equals(this.siteWid, programmeInfoRes.siteWid);
    }

    @Override
    public int hashCode () {
        return Objects.hash(wid, programmeName, platformType, templateId, pageAmount, pageStatus, logoUrl, templateConfig, isShowPcService, sideFlag, siteWid);
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("class ProgrammeInfoRes {\n");

        sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
        sb.append("    programmeName: ").append(toIndentedString(programmeName)).append("\n");
        sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
        sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
        sb.append("    pageAmount: ").append(toIndentedString(pageAmount)).append("\n");
        sb.append("    pageStatus: ").append(toIndentedString(pageStatus)).append("\n");
        sb.append("    logoUrl: ").append(toIndentedString(logoUrl)).append("\n");
        sb.append("    templateConfig: ").append(toIndentedString(templateConfig)).append("\n");
        sb.append("    isShowPcService: ").append(toIndentedString(isShowPcService)).append("\n");
        sb.append("    sideFlag: ").append(toIndentedString(sideFlag)).append("\n");
        sb.append("    siteWid: ").append(toIndentedString(siteWid)).append("\n");
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
