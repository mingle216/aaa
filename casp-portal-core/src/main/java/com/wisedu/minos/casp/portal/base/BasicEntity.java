package com.wisedu.minos.casp.portal.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 * 功能描述：基础实体类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title BasicEntity
 * @Author: jcx
 * @Date: 2020/9/10
 */
public class BasicEntity {

    /**
     * 主键
     */
    @TableId(value = "wid",type= IdType.UUID)
    private String wid;

    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    public String getWid () {
        return wid;
    }

    public void setWid (String wid) {
        this.wid = wid;
    }

    public Date getUpdateTime () {
        return updateTime;
    }

    public void setUpdateTime (Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime () {
        return createTime;
    }

    public void setCreateTime (Date createTime) {
        this.createTime = createTime;
    }
}
