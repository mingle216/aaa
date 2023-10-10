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
 * 系统弹窗
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_popup_window")
public class PopupWindowEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 弹窗主题
         */
        @TableField("pop_title")
        private String popTitle;

        /**
         * 主题多语言id
         */
        @TableField("title_lang_key")
        private String titleLangKey;

        /**
         * 展示方案id
         */
        @TableField("page_wid")
        private String pageWid;

        /**
         * 弹窗内容
         */
        @TableField("pop_contents")
        private String popContents;

        /**
         * 开始时间
         */
        @TableField("beg_time")
        private Date begTime;

        /**
         * 结束时间
         */
        @TableField("end_time")
        private Date endTime;

        /**
         * 展示方式 0:每次登录时提醒 1:仅提醒一次
         */
        @TableField("pop_type")
        private Integer popType;

        /**
         * 是否启用 0:否 1:是
         */
        @TableField("is_enabled")
        private Integer isEnabled;

        /**
         * 是否已经删除 0:否 1:是
         */
        @TableField("is_deleted")
        private Integer isDeleted;

        /**
         * 内容多语言key
         */
        @TableField("contents_lang_key")
        private String contentsLangKey;


}
