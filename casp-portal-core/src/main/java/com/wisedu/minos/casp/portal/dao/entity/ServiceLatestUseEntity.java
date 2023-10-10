package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户最近使用服务表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_service_latest_use")
public class ServiceLatestUseEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 用户最近使用服务表
         */
        @TableField("service_wid")
        private String serviceWid;

        /**
         * 访问用户Id
         */
        @TableField("user_id")
        private String userId;

        /**
         * 最新使用时间
         */
        @TableField("use_time")
        private Date useTime;


}
