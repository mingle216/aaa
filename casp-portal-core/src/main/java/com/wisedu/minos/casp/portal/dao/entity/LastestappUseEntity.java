package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 最新服务使用记录表
 * </p>
 *
 * @author wisedu
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_lastestapp_use")
public class LastestappUseEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 用户wid
         */
        @TableField("user_wid")
        private String userWid;

        /**
         * 服务wid
         */
        @TableField("service_wid")
        private String serviceWid;

        /**
         * pc是否使用(0 否)
         */
        @TableField("pc_read")
        private Integer pcRead;

        /**
         * mobile是否使用(0 否)
         */
        @TableField("mobile_read")
        private Integer mobileRead;


}
