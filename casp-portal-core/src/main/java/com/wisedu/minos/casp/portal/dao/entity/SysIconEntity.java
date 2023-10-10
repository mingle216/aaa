package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 系统图标库
 * </p>
 *
 * @author jdwan
 * @since 2020-09-15
 */
@TableName("t_amp_sys_icon")
public class SysIconEntity extends BasicEntity implements Serializable {

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
     * 排序序号
     */
    @TableField("order_number")
    private Integer orderNumber;

    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;
    /**
     * 类型：0、通用1、链接导航2、服务部门3、服务分类4、菜单
     */
    @TableField("icon_item")
    private Integer iconItem;

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    public Integer getIconType() {
        return iconType;
    }

    public void setIconType(Integer iconType) {
        this.iconType = iconType;
    }
    public String getIconDesc() {
        return iconDesc;
    }

    public void setIconDesc(String iconDesc) {
        this.iconDesc = iconDesc;
    }
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getIconItem() {
        return iconItem;
    }

    public void setIconItem(Integer iconItem) {
        this.iconItem = iconItem;
    }
}
