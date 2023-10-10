package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.sql.Clob;
import java.io.Serializable;

/**
 * <p>
 * card
 * </p>
 *
 * @author jdwan
 * @since 2020-09-15
 */
@TableName("t_amp_view_card")
public class CardEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 卡片ID
     */
    @TableField("card_id")
    private String cardId;

    /**
     * 卡片名称
     */
    @TableField("card_name")
    private String cardName;

    /**
     * 截图地址
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 运行平台 0:PC 1:Mobile
     */
    @TableField("platform_type")
    private String platformType;

    /**
     * 是否支持可配置 0:不支持 1:支持
     */
    @TableField("configurable_flag")
    private Integer configurableFlag;
    /**
     * 是否支持运行时可配置 0:不支持 1:支持
     */
    @TableField("configurable_runtime_flag")
    private Integer configurableRuntimeFlag;
    /**
     * 卡片描述
     */
    @TableField("card_desc")
    private String cardDesc;

    /**
     * 卡片系统类型0：系统1：自定义
     */
    @TableField("card_system_type")
    private Integer cardSystemType;

    /**
     * 数据源配置信息
     */
    @TableField("configure_content")
    private String configureContent;

    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 卡片状态 0正常1有更新2不可用
     */
    @TableField("card_status")
    private Integer cardStatus;

    /**
     * 版本号
     */
    @TableField("version_number")
    private String versionNumber;


    /**
     * 学校代码
     */
    @TableField(exist = false)
    private String schoolId;

    /**
     * 移动端截图地址
     */
    @TableField("image_url_mobile")
    private String imageUrlMobile;

    public String getImageUrlMobile() {
        return imageUrlMobile;
    }

    public void setImageUrlMobile(String imageUrlMobile) {
        this.imageUrlMobile = imageUrlMobile;
    }

    public String getSchoolId () {
        return schoolId;
    }

    public void setSchoolId (String schoolId) {
        this.schoolId = schoolId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public Integer getConfigurableFlag() {
        return configurableFlag;
    }

    public void setConfigurableFlag(Integer configurableFlag) {
        this.configurableFlag = configurableFlag;
    }

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }
    public Integer getCardSystemType() {
        return cardSystemType;
    }

    public void setCardSystemType(Integer cardSystemType) {
        this.cardSystemType = cardSystemType;
    }

    public String getConfigureContent() {
        return configureContent;
    }

    public void setConfigureContent(String configureContent) {
        this.configureContent = configureContent;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Integer getConfigurableRuntimeFlag() {
        return configurableRuntimeFlag;
    }

    public void setConfigurableRuntimeFlag(Integer configurableRuntimeFlag) {
        this.configurableRuntimeFlag = configurableRuntimeFlag;
    }
}
