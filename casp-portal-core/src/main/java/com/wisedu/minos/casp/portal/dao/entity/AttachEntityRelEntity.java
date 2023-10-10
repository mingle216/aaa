package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 附件与其他表关系
 * </p>
 *
 * @author jcx
 * @since 2020-09-16
 */
@TableName("t_amp_attach_entity_rel")
public class AttachEntityRelEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 附件wid
     */
    @TableField("attach_wid")
    private String attachWid;

    /**
     * 其他表wid
     */
    @TableField("entity_wid")
    private String entityWid;

    public String getAttachWid() {
        return attachWid;
    }

    public void setAttachWid(String attachWid) {
        this.attachWid = attachWid;
    }
    public String getEntityWid() {
        return entityWid;
    }
    public void setEntityWid (String entityWid) {
        this.entityWid = entityWid;
    }
}
