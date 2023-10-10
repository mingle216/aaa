package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author jcx
 * @since 2020-09-23
 */
@TableName("t_amp_view_menu_auth")
public class MenuAuthEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableField("menu_wid")
    private String menuWid;

    /**
     * 授权的其他类型id
     */
    @TableField("auth_rel_wid")
    private String authRelWid;

    /**
     * 授权类型  0:组织机构 1:用户 2:域及用户组
     */
    @TableField("auth_type")
    private String authType;
    /**
     * 授权菜单种类  0:顶部菜单 1:侧边栏
     */
    @TableField("menu_auth_type")
    private String menuAuthType;

    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;


    public String getMenuWid() {
        return menuWid;
    }

    public void setMenuWid(String menuWid) {
        this.menuWid = menuWid;
    }
    public String getAuthRelWid() {
        return authRelWid;
    }

    public void setAuthRelWid(String authRelWid) {
        this.authRelWid = authRelWid;
    }
    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getMenuAuthType () {
        return menuAuthType;
    }

    public void setMenuAuthType (String menuAuthType) {
        this.menuAuthType = menuAuthType;
    }
}
