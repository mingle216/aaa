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
 * 站点授权表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_site_auth")
public class SiteAuthEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 站点wid
         */
        @TableField("site_wid")
        private String siteWid;

        /**
         * 授权关联对象wid
         */
        @TableField("auth_rel_wid")
        private String authRelWid;

        /**
         * 授权类型 0 组织机构 1 个人 2 域及用户组
         */
        @TableField("auth_type")
        private Integer authType;

        /**
         * 是否已经删除 0:非删除 1:删除
         */
        @TableField("is_deleted")
        private Integer isDeleted;

        /**
         * 操作人
         */
        @TableField("operator")
        private String operator;


}
