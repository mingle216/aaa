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
 * 站点表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_site")
public class SiteEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 是否主站点
         */
        @TableField("is_master")
        private Integer isMaster;

        /**
         * 站点名称
         */
        @TableField("site_name")
        private String siteName;

        /**
         * 站点名称多语言
         */
        @TableField("site_name_lang_key")
        private String siteNameLangKey;

        /**
         * 站点路由
         */
        @TableField("site_route")
        private String siteRoute;

        /**
         * 授权类型 0游客可见 1登录后可见 2游客及登录后可见 3授权后可见
         */
        @TableField("auth_type")
        private Integer authType;

        /**
         * 排序
         */
        @TableField("order_index")
        private Integer orderIndex;

        /**
         * 站点状态 0 停用 1启用
         */
        @TableField("is_enabled")
        private Integer isEnabled;

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
