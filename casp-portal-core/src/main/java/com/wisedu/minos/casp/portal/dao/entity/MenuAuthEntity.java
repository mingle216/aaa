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
 * 菜单权限表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_menu_auth")
public class MenuAuthEntity extends BasicEntity implements java.io.Serializable {

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


}
