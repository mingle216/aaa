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
 * 附件信息表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_attach_info")
public class AttachInfoEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 文件名称
         */
        @TableField("file_name")
        private String fileName;

        /**
         * 文件地址全路径
         */
        @TableField("file_url")
        private String fileUrl;

        /**
         * 文件地址路径
         */
        @TableField("file_location")
        private String fileLocation;

        /**
         * 文件后缀名
         */
        @TableField("file_suffix")
        private String fileSuffix;

        /**
         * 是否删除 1：正常 0：删除
         */
        @TableField("is_deleted")
        private String isDeleted;

        /**
         * 操作人的wid
         */
        @TableField("operate_wid")
        private String operateWid;

        /**
         * 操作人的名称
         */
        @TableField("operate_name")
        private String operateName;


}
