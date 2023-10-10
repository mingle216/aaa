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
 * 侧边栏表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_sidebar")
public class SidebarEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 栏目编码,不可重复
         */
        @TableField("column_code")
        private String columnCode;

        /**
         * 栏目名称
         */
        @TableField("column_name")
        private String columnName;

        /**
         * 是否系统自带栏目（0：否；1：是）
         */
        @TableField("is_system")
        private Integer isSystem;

        /**
         * 栏目的图标类型（图标类型  0 字体图标  1 png图标）
         */
        @TableField("icon_type")
        private Integer iconType;

        /**
         * 授权类别（0：游客可见；1：登录后可见；2：授权后可见）
         */
        @TableField("auth_type")
        private Integer authType;

        /**
         * 是否启用（0：否；1：是）
         */
        @TableField("enabled_flag")
        private Integer enabledFlag;

        /**
         * 排序
         */
        @TableField("order_number")
        private Long orderNumber;

        /**
         * 栏目链接地址，非特定栏目有值
         */
        @TableField("link_url")
        private String linkUrl;

        /**
         * 栏目的图标样式名称
         */
        @TableField("icon_url")
        private String iconUrl;

        /**
         * 展示名称
         */
        @TableField("display_name")
        private String displayName;

        /**
         * 方位0:(左侧),1:(右侧)
         */
        @TableField("position_type")
        private Integer positionType;

        /**
         * 展示名称国际化id
         */
        @TableField("display_name_lang_key")
        private String displayNameLangKey;

        /**
         * 展示方案id
         */
        @TableField("programme_id")
        private String programmeId;

        /**
         * 菜单类型0：无链接1：内部页面2：第三方链接
         */
        @TableField("menu_type")
        private Integer menuType;

        /**
         * 打开方式  0:当前页 1:新开窗口
         */
        @TableField("open_type")
        private Integer openType;

        /**
         * 对应页面
         */
        @TableField("page_id")
        private String pageId;

        /**
         * 是否已经删除 0:否 1:是
         */
        @TableField("is_deleted")
        private Integer isDeleted;

        /**
         * 数量提醒地址
         */
        @TableField("count_address")
        private String countAddress;


}
