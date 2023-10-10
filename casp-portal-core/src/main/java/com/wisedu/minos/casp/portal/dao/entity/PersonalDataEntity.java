package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 个人数据配置表
 * </p>
 *
 * @author jcx
 * @since 2021-05-21
 */
@TableName("t_amp_personal_data")
@Getter
@Setter
@Accessors(chain = true)
public class PersonalDataEntity extends BasicEntity implements Serializable {

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
    @TableField(value = "sub_info",strategy = FieldStrategy.IGNORED)
    private String subInfo;

    /**
     * 数据来源方式（0：静态文字，1：JSON接口，2:MINOS数据源，3：邮箱,4:插件）
     */
    @TableField("source_type")
    private Integer sourceType;

    /**
     * 数据来源信息（sourceType=1：接口地址，source_type=2：数据源ID）
     */
    @TableField(value = "source_key",strategy = FieldStrategy.IGNORED)
    private String sourceKey;

    /**
     * 获取数据的sql，sourceType=2时有效
     */
    @TableField(value = "data_sql",strategy = FieldStrategy.IGNORED)
    private String dataSql;

    /**
     * HTTP请求方式，sourceType=1时有效（0：GET，1：POST）
     */
    @TableField(value = "http_method",strategy = FieldStrategy.IGNORED)
    private Integer httpMethod;

    /**
     * 用户参数key值，sourceType=1时有效,当不配置时，不传输用户信息。当前的用户信息为“用户工号”
     */
    @TableField(value = "user_key",strategy = FieldStrategy.IGNORED)
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
    @TableField(value = "sub_info_lang_key",strategy = FieldStrategy.IGNORED)
    private String subInfoLangKey;
    /**
     * 插件配置
     */
    @TableField(value = "auth_config",strategy = FieldStrategy.IGNORED)
    private String authConfig;
    @TableField("is_deleted")
    private Integer isDeleted;



    @Override
    public String toString() {
        return "PersonalDataEntity{" +

        ", title=" + title +
        ", iconUrl=" + iconUrl +
        ", mainInfo=" + mainInfo +
        ", subInfo=" + subInfo +
        ", sourceType=" + sourceType +
        ", sourceKey=" + sourceKey +
        ", dataSql=" + dataSql +
        ", httpMethod=" + httpMethod +
        ", userKey=" + userKey +
        ", linkUrl=" + linkUrl +
        ", cacheTimeout=" + cacheTimeout +
        ", orderNumber=" + orderNumber +
        ", enabled=" + enabled +
        ", titleLangKey=" + titleLangKey +
        ", mainInfoLangKey=" + mainInfoLangKey +
        ", subInfoLangKey=" + subInfoLangKey +
        ", authConfig=" + authConfig +
        "}";
    }
}
