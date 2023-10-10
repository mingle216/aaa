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
 * 顶部菜单表
 * </p>
 *
 * @author wisedu
 * @since 2022-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_menu")
public class MenuEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 菜单名称
         */
        @TableField("menu_name")
        private String menuName;

        /**
         * 图标
         */
        @TableField("icon_url")
        private String iconUrl;

        /**
         * 排序
         */
        @TableField("order_number")
        private Long orderNumber;

        /**
         * 0:游客可见 1:登录后可见  2:游客及登录后可见 3:授权后可见
         */
        @TableField("auth_type")
        private Integer authType;

        /**
         * 是否启用 0:停用 1:启用
         */
        @TableField("is_enabled")
        private Integer isEnabled;

        /**
         * 打开方式  0:当前页 1:新开窗口
         */
        @TableField("open_type")
        private Integer openType;

        /**
         * 菜单链接
         */
        @TableField("link_url")
        private String linkUrl;

        /**
         * 父级菜单ID
         */
        @TableField("parent_id")
        private String parentId;

        /**
         * 展示方案Id
         */
        @TableField("programme_id")
        private String programmeId;

        /**
         * 菜单类型0：无链接1：内部页面2：第三方链接
         */
        @TableField("menu_type")
        private Integer menuType;

        /**
         * 对应页面
         */
        @TableField("page_id")
        private String pageId;

        /**
         * 图标类型  0 字体图标  1 png图标
         */
        @TableField("icon_type")
        private Integer iconType;

        /**
         * 菜单名称国际化id
         */
        @TableField("menu_name_lang_key")
        private String menuNameLangKey;

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
