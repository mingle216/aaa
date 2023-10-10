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
 * 页面卡片配置表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_page_card_config")
public class PageCardConfigEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 卡片运行时ID
         */
        @TableField("card_wid")
        private String cardWid;

        /**
         * 页面ID
         */
        @TableField("page_wid")
        private String pageWid;

        /**
         * 卡片ID
         */
        @TableField("card_id")
        private String cardId;

        /**
         * 卡片配置
         */
        @TableField("card_config")
        private String cardConfig;

        /**
         * 是否启用（0：否；1：是）
         */
        @TableField("enabled_flag")
        private Integer enabledFlag;

        /**
         * 是否已经删除 0:否 1:是
         */
        @TableField("is_deleted")
        private Integer isDeleted;

        /**
         * 终端类型,0为PC端(默认)，1为移动端
         */
        @TableField("platform_type")
        private Integer platformType;


}
