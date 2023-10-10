package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 个人数据配置表
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_amp_personal_data")
public class PersonalDataEntity extends BasicEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

        /**
         * 个人数据标题
         */
        @TableField("title")
        private String title;

        /**
         * 图标地址
         */
        @TableField("icon_url")
        private String iconUrl;

        /**
         * 主要信息
         */
        @TableField("main_info")
        private String mainInfo;

        /**
         * 次要信息
         */
        @TableField(value = "sub_info",el="subInfo,jdbcType=VARCHAR",strategy= FieldStrategy.IGNORED)
        private String subInfo;

        /**
         * 是否为个人数据（0：非个人数据，1：个人数据）
         */
        @TableField("personal_data")
        private Integer personalData;

        /**
         * 是否为个人数据（0：非个人数据，1：个人数据）
         */
        @TableField("personal_title")
        private String personalTitle;

        /**
         * 数据来源方式（0：静态文字，1：JSON接口，2:MINOS数据源，3：邮箱）
         */
        @TableField("source_type")
        private Integer sourceType;

        /**
         * 数据来源信息（sourceType=1：接口地址，source_type=2：数据源ID）
         */
        @TableField("source_key")
        private String sourceKey;

        /**
         * 获取数据的sql，sourceType=2时有效
         */
        @TableField("data_sql")
        private String dataSql;

        /**
         * HTTP请求方式，sourceType=1时有效（0：GET，1：POST）
         */
        @TableField("http_method")
        private Integer httpMethod;

        /**
         * 用户参数key值，sourceType=1时有效,当不配置时，不传输用户信息。当前的用户信息为“用户工号”
         */
        @TableField("user_key")
        private String userKey;

        /**
         * 跳转地址，sourceType为0,1,2时有效
         */
        @TableField("link_url")
        private String linkUrl;

        /**
         * 数据缓存时长，单位：秒
         */
        @TableField("cache_timeout")
        private Long cacheTimeout;

        /**
         * 排序
         */
        @TableField("order_number")
        private Integer orderNumber;

        /**
         * 0：停用，1：启用
         */
        @TableField("enabled")
        private Integer enabled;

        /**
         * 个人数据标题国际化id
         */
        @TableField("title_lang_key")
        private String titleLangKey;

        /**
         * 主要信息国际化id
         */
        @TableField("main_info_lang_key")
        private String mainInfoLangKey;

        /**
         * 次要信息国际化id
         */
        @TableField(value = "sub_info_lang_key",el="subInfoLangKey,jdbcType=VARCHAR",strategy= FieldStrategy.IGNORED)
        private String subInfoLangKey;

        /**
         * 认证相关配置信息，根据具体认证插件配置，json格式
         */
        @TableField("auth_config")
        private String authConfig;

        /**
         * 是否删除0:不删除，1：删除
         */
        @TableField("is_deleted")
        private Integer isDeleted;

        /**
         * 是否隐藏隐私数据 0不隐藏 1：隐藏
         */
        @TableField("is_hidden_privacy")
        private Integer isHiddenPrivacy;

}
