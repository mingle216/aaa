package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 版本管理表
 * </p>
 *
 * @author jdwan
 * @since 2020-09-15
 */
@TableName("t_amp_view_version_management")
public class VersionManagementEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本号
     */
    @TableField("version_number")
    private String versionNumber;

    /**
     * 类型0：模板1：卡片
     */
    @TableField("version_type")
    private Integer versionType;

    /**
     * 名称
     */
    @TableField("version_name")
    private String versionName;

    /**
     * 状态 1：在用0：停用
     */
    @TableField("version_status")
    private Integer versionStatus;

    /**
     * 模板/卡片id
     */
    @TableField("foreign_key")
    private String foreignKey;

    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Integer getVersionType() {
        return versionType;
    }

    public void setVersionType(Integer versionType) {
        this.versionType = versionType;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(Integer versionStatus) {
        this.versionStatus = versionStatus;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
