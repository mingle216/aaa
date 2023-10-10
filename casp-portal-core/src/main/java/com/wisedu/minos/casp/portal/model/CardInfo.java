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
 * CardInfo
 */
@Validated

public class CardInfo {
    @JsonProperty("wid")
    private String wid = null;

    @JsonProperty("cardId")
    private String cardId = null;

    @JsonProperty("cardName")
    private String cardName = null;

    @JsonProperty("cardDesc")
    private String cardDesc = null;

    @JsonProperty("imageUrl")
    private String imageUrl = null;

    @JsonProperty("imageUrlMobile")
    private String imageUrlMobile = null;

    @JsonProperty("updateTime")
    private String updateTime = null;

    @JsonProperty("platformType")
    private String platformType = null;

    @JsonProperty("configurableFlag")
    private Integer configurableFlag = null;

    @JsonProperty("configurableRuntimeFlag")
    private Integer configurableRuntimeFlag = null;

    @JsonProperty("cardSystemType")
    private Integer cardSystemType = null;

    @JsonProperty("configureContent")
    private String configureContent = null;

    @JsonProperty("versionNumber")
    private String versionNumber = null;

    @JsonProperty("cardStatus")
    private Integer cardStatus = null;

    @JsonProperty("refPage")
    private Boolean refPage = null;

    public CardInfo wid(String wid) {
        this.wid = wid;
        return this;
    }

    /**
     * 主键wid
     *
     * @return wid
     **/
    @ApiModelProperty(value = "主键wid")

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public CardInfo cardId(String cardId) {
        this.cardId = cardId;
        return this;
    }

    /**
     * 卡片ID
     *
     * @return cardId
     **/
    @ApiModelProperty(value = "卡片ID")

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public CardInfo cardName(String cardName) {
        this.cardName = cardName;
        return this;
    }

    /**
     * 卡片名称
     *
     * @return cardName
     **/
    @ApiModelProperty(value = "卡片名称")

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public CardInfo cardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
        return this;
    }

    /**
     * 卡片描述
     *
     * @return cardDesc
     **/
    @ApiModelProperty(value = "卡片描述")

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }

    public CardInfo imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    /**
     * 截图地址
     *
     * @return imageUrl
     **/
    @ApiModelProperty(value = "截图地址")

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CardInfo imageUrlMobile(String imageUrlMobile) {
        this.imageUrlMobile = imageUrlMobile;
        return this;
    }

    /**
     * 移动端截图地址
     *
     * @return imageUrlMobile
     **/
    @ApiModelProperty(value = "移动端截图地址")

    public String getImageUrlMobile() {
        return imageUrlMobile;
    }

    public void setImageUrlMobile(String imageUrlMobile) {
        this.imageUrlMobile = imageUrlMobile;
    }

    public CardInfo updateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    /**
     * 最近一次更新时间
     *
     * @return updateTime
     **/
    @ApiModelProperty(value = "最近一次更新时间")

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public CardInfo platformType(String platformType) {
        this.platformType = platformType;
        return this;
    }

    /**
     * 运行平台 0:PC 1:Mobile,多个平台用；分割拼接
     *
     * @return platformType
     **/
    @ApiModelProperty(value = "运行平台 0:PC 1:Mobile,多个平台用；分割拼接")

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public CardInfo configurableFlag(Integer configurableFlag) {
        this.configurableFlag = configurableFlag;
        return this;
    }

    /**
     * 是否支持可配置 0:不支持 1:支持
     *
     * @return configurableFlag
     **/
    @ApiModelProperty(value = "是否支持可配置 0:不支持 1:支持")

    public Integer getConfigurableFlag() {
        return configurableFlag;
    }

    public void setConfigurableFlag(Integer configurableFlag) {
        this.configurableFlag = configurableFlag;
    }

    public CardInfo configurableRuntimeFlag(Integer configurableRuntimeFlag) {
        this.configurableRuntimeFlag = configurableRuntimeFlag;
        return this;
    }

    /**
     * 是否支持运行时可配置 0:不支持 1:支持
     *
     * @return configurableRuntimeFlag
     **/
    @ApiModelProperty(value = "是否支持运行时可配置 0:不支持 1:支持")

    public Integer getConfigurableRuntimeFlag() {
        return configurableRuntimeFlag;
    }

    public void setConfigurableRuntimeFlag(Integer configurableRuntimeFlag) {
        this.configurableRuntimeFlag = configurableRuntimeFlag;
    }

    public CardInfo cardSystemType(Integer cardSystemType) {
        this.cardSystemType = cardSystemType;
        return this;
    }

    /**
     * 卡片属性0：系统1：自定义
     *
     * @return cardSystemType
     **/
    @ApiModelProperty(value = "卡片属性0：系统1：自定义")

    public Integer getCardSystemType() {
        return cardSystemType;
    }

    public void setCardSystemType(Integer cardSystemType) {
        this.cardSystemType = cardSystemType;
    }

    public CardInfo configureContent(String configureContent) {
        this.configureContent = configureContent;
        return this;
    }

    /**
     * 配置信息
     *
     * @return configureContent
     **/
    @ApiModelProperty(value = "配置信息")

    public String getConfigureContent() {
        return configureContent;
    }

    public void setConfigureContent(String configureContent) {
        this.configureContent = configureContent;
    }

    public CardInfo versionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
        return this;
    }

    /**
     * 版本号
     *
     * @return versionNumber
     **/
    @ApiModelProperty(value = "版本号")

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public CardInfo cardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
        return this;
    }

    /**
     * 卡片状态 0正常1有更新2不可用
     *
     * @return cardStatus
     **/
    @ApiModelProperty(value = "卡片状态 0正常1有更新2不可用")

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    /**
     * 关联状态 true关联 false未关联
     *
     * @return cardStatus
     **/
    @ApiModelProperty(value = "关联状态 true关联 false未关联")
    public Boolean getRefPage() {
        return refPage;
    }

    public void setRefPage(Boolean refPage) {
        this.refPage = refPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardInfo cardInfo = (CardInfo) o;
        return Objects.equals(this.wid, cardInfo.wid) &&
                Objects.equals(this.cardId, cardInfo.cardId) &&
                Objects.equals(this.cardName, cardInfo.cardName) &&
                Objects.equals(this.cardDesc, cardInfo.cardDesc) &&
                Objects.equals(this.imageUrl, cardInfo.imageUrl) &&
                Objects.equals(this.imageUrlMobile, cardInfo.imageUrlMobile) &&
                Objects.equals(this.updateTime, cardInfo.updateTime) &&
                Objects.equals(this.platformType, cardInfo.platformType) &&
                Objects.equals(this.configurableFlag, cardInfo.configurableFlag) &&
                Objects.equals(this.configurableRuntimeFlag, cardInfo.configurableRuntimeFlag) &&
                Objects.equals(this.cardSystemType, cardInfo.cardSystemType) &&
                Objects.equals(this.configureContent, cardInfo.configureContent) &&
                Objects.equals(this.versionNumber, cardInfo.versionNumber) &&
                Objects.equals(this.cardStatus, cardInfo.cardStatus) &&
                Objects.equals(this.refPage, cardInfo.refPage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wid, cardId, cardName, cardDesc, imageUrl, imageUrlMobile, updateTime, platformType, configurableFlag, configurableRuntimeFlag, cardSystemType, configureContent, versionNumber, cardStatus, refPage);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CardInfo {\n");

        sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
        sb.append("    cardId: ").append(toIndentedString(cardId)).append("\n");
        sb.append("    cardName: ").append(toIndentedString(cardName)).append("\n");
        sb.append("    cardDesc: ").append(toIndentedString(cardDesc)).append("\n");
        sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
        sb.append("    imageUrlMobile: ").append(toIndentedString(imageUrlMobile)).append("\n");
        sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
        sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
        sb.append("    configurableFlag: ").append(toIndentedString(configurableFlag)).append("\n");
        sb.append("    configurableRuntimeFlag: ").append(toIndentedString(configurableRuntimeFlag)).append("\n");
        sb.append("    cardSystemType: ").append(toIndentedString(cardSystemType)).append("\n");
        sb.append("    configureContent: ").append(toIndentedString(configureContent)).append("\n");
        sb.append("    versionNumber: ").append(toIndentedString(versionNumber)).append("\n");
        sb.append("    cardStatus: ").append(toIndentedString(cardStatus)).append("\n");
        sb.append("    refPage: ").append(toIndentedString(refPage)).append("\n");
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
