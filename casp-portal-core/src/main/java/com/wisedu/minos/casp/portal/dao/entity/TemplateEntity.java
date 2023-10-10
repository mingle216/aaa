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
 * 模板表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_template")
public class TemplateEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 模板id
         */
        @TableField("template_id")
        private String templateId;

        /**
         * 模板系统类型0：系统模板1：自定义模板
         */
        @TableField("template_system_type")
        private Integer templateSystemType;

        /**
         * 模板名称
         */
        @TableField("template_name")
        private String templateName;

        /**
         * 模板展示图URL
         */
        @TableField("template_img_url")
        private String templateImgUrl;

        /**
         * 模板描述
         */
        @TableField("template_desc")
        private String templateDesc;

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
         * 配置信息
         */
        @TableField("configure_content")
        private String configureContent;

        /**
         * 运行平台 0:PC 1:Mobile,多个平台用；分割拼接
         */
        @TableField("platform_type")
        private String platformType;

        /**
         * 状态0正常1有更新2不可用
         */
        @TableField("template_status")
        private Integer templateStatus;

        /**
         * 版本号
         */
        @TableField("version_number")
        private String versionNumber;

        /**
         * 是否已经删除 0:否 1:是
         */
        @TableField("is_deleted")
        private Integer isDeleted;

        /**
         * 移动端模板主图地址
         */
        @TableField("template_img_mobile_url")
        private String templateImgMobileUrl;


}
