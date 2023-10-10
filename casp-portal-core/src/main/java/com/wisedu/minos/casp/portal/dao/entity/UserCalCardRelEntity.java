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
 * 用户-日程卡片关联表
 * </p>
 *
 * @author wisedu
 * @since 2022-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_user_cal_card_rel")
public class UserCalCardRelEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 用户id
         */
        @TableField("user_id")
        private String userId;

        /**
         * 卡片wid
         */
        @TableField("card_wid")
        private String cardWid;

        /**
         * 提醒时间
         */
        @TableField("remind_time")
        private String remindTime;

        /**
         * 提醒方式
         */
        @TableField("remind_way")
        private String remindWay;

        /**
         * 周提醒状态 0 停用 1启用
         */
        @TableField("is_enabled")
        private Integer isEnabled;

        /**
         * 是否已经删除 0:非删除 1:删除
         */
        @TableField("is_deleted")
        private Integer isDeleted;

        /**
         * 提醒时间（小时）
         */
        @TableField("remind_time_hour")
        private String remindTimeHour;


}
