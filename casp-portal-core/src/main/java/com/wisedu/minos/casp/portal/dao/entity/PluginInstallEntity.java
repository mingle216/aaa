package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 插件安装表
 * </p>
 *
 * @author jcx
 * @since 2021-06-07
 */
@TableName("t_amp_view_plugin_install")
public class PluginInstallEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 插件发布表wid
     */
    @TableId("plugin_wid")
    private String pluginWid;

    /**
     * ip地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    public String getPluginWid() {
        return pluginWid;
    }

    public void setPluginWid(String pluginWid) {
        this.pluginWid = pluginWid;
    }
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PluginInstallEntity{" +
        "pluginWid=" + pluginWid +
        ", ipAddress=" + ipAddress +
        ", updateTime=" + updateTime +
        ", createTime=" + createTime +
        "}";
    }
}
