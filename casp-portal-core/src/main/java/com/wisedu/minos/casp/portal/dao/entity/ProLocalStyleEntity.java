package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * localStyle
 * </p>
 *
 * @author jcx
 * @since 2021-04-28
 */
@TableName("t_amp_view_pro_local_style")
public class ProLocalStyleEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 展示方案WID
     */
    @TableField("programme_wid")
    private String programmeWid;

    /**
     * 展示方案本地化样式代码
     */
    @TableField("programme_local_style")
    private String programmeLocalStyle;

    public String getProgrammeWid() {
        return programmeWid;
    }

    public void setProgrammeWid(String programmeWid) {
        this.programmeWid = programmeWid;
    }
    public String getProgrammeLocalStyle() {
        return programmeLocalStyle;
    }

    public void setProgrammeLocalStyle(String programmeLocalStyle) {
        this.programmeLocalStyle = programmeLocalStyle;
    }

    @Override
    public String toString() {
        return "ProLocalStyleEntity{" +
        "programmeWid=" + programmeWid +
        ", programmeLocalStyle=" + programmeLocalStyle +
        "}";
    }
}
