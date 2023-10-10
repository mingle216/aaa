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
 * 旧OSS域名记录替换表
 * </p>
 *
 * @author wisedu
 * @since 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_old_oss")
public class OldOssEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 主键
         */
        @TableId(value = "wid",type= IdType.UUID)
        private String wid;

        /**
         * 需要替换的oss旧域名
         */
        @TableField("url")
        private String url;


}
