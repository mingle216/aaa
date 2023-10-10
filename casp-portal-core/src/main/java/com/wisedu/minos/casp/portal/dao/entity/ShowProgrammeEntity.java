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
 * 展示方案表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_view_show_programme")
public class ShowProgrammeEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 展示方案名称
         */
        @TableField("programme_name")
        private String programmeName;

        /**
         * 适配终端0：PC1：移动端
         */
        @TableField("platform_type")
        private Integer platformType;

        /**
         * 模板id
         */
        @TableField("template_id")
        private String templateId;

        /**
         * 页面数量
         */
        @TableField("page_amount")
        private Long pageAmount;

        /**
         * 状态0：启用1：停用
         */
        @TableField("page_status")
        private Integer pageStatus;

        /**
         * 校徽/LOGO地址
         */
        @TableField("logo_url")
        private String logoUrl;

        /**
         * 模板配置
         */
        @TableField("template_config")
        private String templateConfig;

        /**
         * 是否已经删除 0:否 1:是
         */
        @TableField("is_deleted")
        private Integer isDeleted;

        /**
         * 是否展示左/右侧栏0都不展示 1只展示左侧栏2只展示右侧栏3、都展示
         */
        @TableField("side_flag")
        private Integer sideFlag;

        /**
         * 是否初始化方案（初始化展示方案隐藏）：0否  1是
         */
        @TableField("initialize_flag")
        private Integer initializeFlag;

        /**
         * 是否显示PC端服务  1:显示  0:隐藏
         */
        @TableField("is_show_pc_service")
        private Integer isShowPcService;

        /**
         * 站点wid
         */
        @TableField("site_wid")
        private String siteWid;

        /**
         * 是否支持pc服务在移动打开  1:支持  0:不支持
         */
        @TableField("is_pc_service_supported")
        private Integer isPcServiceSupported;

}
