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
 * 个人数据权限卡片
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_personal_data_auth")
public class PersonalDataAuthEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 个人数据卡片id
         */
        @TableField("data_id")
        private String dataId;

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
         * 是否已经删除 0:否 1:是
         */
        @TableField("is_deleted")
        private Integer isDeleted;


}
