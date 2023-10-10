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
 * 页面信息表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_page_info")
public class PageInfoEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 页面名称
         */
        @TableField("page_name")
        private String pageName;

        /**
         * 页面代码
         */
        @TableField("page_code")
        private String pageCode;

        /**
         * 运行平台 0:PC 1:Mobile
         */
        @TableField("platform_type")
        private Integer platformType;

        /**
         * 背景底色
         */
        @TableField("background_color")
        private String backgroundColor;

        /**
         * 背景图片
         */
        @TableField("background_image")
        private String backgroundImage;

        /**
         * 模板代码
         */
        @TableField("template_code")
        private String templateCode;

        /**
         * 卡片布局数据
         */
        @TableField("card_layout")
        private String cardLayout;

        /**
         * 父级页面ID
         */
        @TableField("parent_id")
        private String parentId;

        /**
         * 背景图片
         */
        @TableField("page_title")
        private String pageTitle;

        /**
         * 展示方案id
         */
        @TableField("programme_id")
        private String programmeId;

        /**
         * 页面类型0：功能页1：自由页
         */
        @TableField("page_type")
        private Integer pageType;

        /**
         * 页面标题国际化id
         */
        @TableField("page_title_lang_key")
        private String pageTitleLangKey;

        /**
         * 是否启用（0：否；1：是）
         */
        @TableField("enabled_flag")
        private Integer enabledFlag;

        /**
         * 页面配置信息
         */
        @TableField("page_config")
        private String pageConfig;

        /**
         * 是否已经删除 0:否 1:是
         */
        @TableField("is_deleted")
        private Integer isDeleted;


}
