package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author wisedu
 * @since 2022-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_system_config")
public class SystemConfigEntity extends BasicEntity implements java.io.Serializable {

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

        /**
         * 配置值多语言
         */
        @TableField("value_lang_key")
        private String valueLangKey;

        @TableField("page_show")
        private Integer pageShow;


}
