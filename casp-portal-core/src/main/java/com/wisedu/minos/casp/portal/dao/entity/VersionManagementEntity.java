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
 * 版本管理表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_version_management")
public class VersionManagementEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 版本号
         */
        @TableField("version_number")
        private String versionNumber;

        /**
         * 类型0：模板1：卡片
         */
        @TableField("version_type")
        private Integer versionType;

        /**
         * 名称
         */
        @TableField("version_name")
        private String versionName;

        /**
         * 状态0：停用1：在用
         */
        @TableField("version_status")
        private Integer versionStatus;

        /**
         * 模板/卡片id
         */
        @TableField("foreign_key")
        private String foreignKey;

        /**
         * 是否已经删除 0:否 1:是
         */
        @TableField("is_deleted")
        private Integer isDeleted;


}
