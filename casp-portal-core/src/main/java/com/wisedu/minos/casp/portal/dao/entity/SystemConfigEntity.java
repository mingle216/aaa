package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author jdwan
 * @since 2020-09-15
 */
@TableName("t_amp_view_system_config")
public class SystemConfigEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置项key
     */
    @TableField("config_key")
    private String configKey;

    /**
     * 配置项值
     */
    @TableField("config_value")
    private String configValue;

    /**
     * 默认值
     */
    @TableField("default_value")
    private String defaultValue;

    /**
     * 描述
     */
    @TableField("config_desc")
    private String configDesc;

    /**
     * 0:系统配置  1:页面配置 2:其他配置
     */
    @TableField("config_model")
    private Integer configModel;

    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }
    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }
    public Integer getConfigModel() {
        return configModel;
    }

    public void setConfigModel(Integer configModel) {
        this.configModel = configModel;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
