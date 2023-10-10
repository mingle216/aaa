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
 * 站点管理员
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_site_mag")
public class SiteMagEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 站点wid
         */
        @TableField("site_wid")
        private String siteWid;

        /**
         * 管理员对象wid
         */
        @TableField("mag_rel_wid")
        private String magRelWid;

        /**
         * 授权类型 0 角色 1 个人
         */
        @TableField("mag_type")
        private Integer magType;

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
