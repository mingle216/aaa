package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 国际化公用表
 * </p>
 *
 * @author jdwan
 * @since 2020-09-15
 */
@TableName("t_amp_view_international")
public class InternationalizationEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 某个词的唯一id
     */
    @TableField("lang_key")
    private String langKey;

    /**
     * 语言类型（例zh_CN  、en_US等）
     */
    @TableField("lang_country")
    private String langCountry;

    /**
     * 展示内容
     */
    @TableField("lang_value")
    private String langValue;
    /**
     * 数据来源,0 系统 1 手工  默认值 0
     */
    @TableField("source_type")
    private Integer sourceType;


    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getLangCountry() {
        return langCountry;
    }

    public void setLangCountry(String langCountry) {
        this.langCountry = langCountry;
    }

    public String getLangValue() {
        return langValue;
    }

    public void setLangValue(String langValue) {
        this.langValue = langValue;
    }


    public Integer getSourceType () {
        return sourceType;
    }

    public void setSourceType (Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
