package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户邮箱关系表
 * </p>
 *
 * @author wisedu
 * @since 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_user_email")
public class UserEmailEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 主键
         */
        @TableId(value = "wid",type= IdType.UUID)
        private String wid;

        /**
         * 邮箱账号
         */
        @TableField("email")
        private String email;

        /**
         * 用户id
         */
        @TableField("user_wid")
        private String userWid;

        /**
         * 是否已删除 1是0否
         */
        @TableField("is_delete")
        private Integer isDelete;

        /**
         * 字段排序
         */
        @TableField("order_number")
        private Integer orderNumber;

        /**
         * 邮箱来源
         */
        @TableField("source_type")
        private String sourceType;


}
