package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 站点切换记录表
 * </p>
 *
 * @author wisedu
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_site_switching")
public class SiteSwitchingEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "wid", type = IdType.UUID)
    private String wid;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 用户账号
     */
    @TableField("user_account")
    private String userAccount;

    /**
     * 站点id
     */
    @TableField("site_wid")
    private String siteWid;


}
