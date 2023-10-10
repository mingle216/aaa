package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 服务事项搜索历史表
 * </p>
 *
 * @author gulong
 * @since 2020-09-11
 */
@TableName("t_amp_service_latest_use")
public class ServiceLatestUseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务id
     */
    @TableField("service_wid ")
    private String serviceWid;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    @TableField(value = "use_time")
    private Date useTime;

    public String getServiceWid() {
        return serviceWid;
    }

    public void setServiceWid(String serviceWid) {
        this.serviceWid = serviceWid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

}
