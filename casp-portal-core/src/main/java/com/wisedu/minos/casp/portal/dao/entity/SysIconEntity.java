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
 * 系统图标库
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_sys_icon")
public class SysIconEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 图标名称
         */
        @TableField("icon_name")
        private String iconName;

        /**
         * 图标类型  0 字体图标  1 png图标
         */
        @TableField("icon_type")
        private Integer iconType;

        /**
         * 图标描述
         */
        @TableField("icon_desc")
        private String iconDesc;

        /**
         * 图标地址
         */
        @TableField("icon_url")
        private String iconUrl;

        /**
         * 类型：0、通用1、链接导航2、服务部门3、服务分类4、菜单
         */
        @TableField("icon_item")
        private Integer iconItem;

        /**
         * 排序序号
         */
        @TableField("order_number")
        private Integer orderNumber;

        /**
         * 是否已经删除 0:否 1:是
         */
        @TableField("is_deleted")
        private Integer isDeleted;


}
