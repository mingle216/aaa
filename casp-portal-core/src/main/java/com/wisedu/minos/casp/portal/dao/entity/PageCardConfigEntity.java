package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 页面卡片配置表
 * </p>
 *
 * @author jdwan
 * @since 2020-09-15
 */
@TableName("t_amp_view_page_card_config")
public class PageCardConfigEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 卡片运行时ID
     */
    @TableField("card_wid")
    private String cardWid;

    /**
     * 页面ID
     */
    @TableField("page_wid")
    private String pageWid;

    /**
     * 卡片ID
     */
    @TableField("card_id")
    private String cardId;

    /**
     * 卡片配置
     */
    @TableField("card_config")
    private String cardConfig;
    /**
     * 是否启用（0：否；1：是）
     */
    @TableField("enabled_flag")
    private Integer enabledFlag;
    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;
    /**
     * 终端类型,0为PC端(默认)，1为移动端
     */
    @TableField("platform_type")
    private Integer platformType;

    public String getCardWid() {
        return cardWid;
    }

    public void setCardWid(String cardWid) {
        this.cardWid = cardWid;
    }
    public String getPageWid() {
        return pageWid;
    }

    public void setPageWid(String pageWid) {
        this.pageWid = pageWid;
    }
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public String getCardConfig() {
        return cardConfig;
    }

    public void setCardConfig(String cardConfig) {
        this.cardConfig = cardConfig;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }
}
