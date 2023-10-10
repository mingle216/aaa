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
 * card
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_card")
public class CardEntity extends BasicEntity implements java.io.Serializable {

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
         * 运行平台 0:PC 1:Mobile,多个平台用；分割拼接
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
         * 配置信息
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
         * 移动端卡片截图地址
         */
        @TableField("image_url_mobile")
        private String imageUrlMobile;


}
