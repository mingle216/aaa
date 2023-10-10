package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 门户用户登录记录表
 * </p>
 *
 * @author wisedu
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_user_login")
public class UserLoginEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 用户账号
         */
        @TableField("user_account")
        private String userAccount;

        /**
         * 模块的平台类型 0:pc 1:移动
         */
        @TableField("model_platform")
        private Integer modelPlatform;

        /**
         * 用户登录门户的ip
         */
        @TableField("login_ip")
        private String loginIp;


}
