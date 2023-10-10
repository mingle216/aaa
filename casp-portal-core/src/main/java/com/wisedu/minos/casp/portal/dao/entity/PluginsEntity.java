package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 插件发布表
 * </p>
 *
 * @author jcx
 * @since 2021-06-08
 */
@TableName("t_amp_view_plugins")
public class PluginsEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类型0：模板 1：卡片 2:邮箱插件
     */
    @TableField("plugin_type")
    private Integer pluginType;

    /**
     * 模板、卡片、邮箱插件的ID
     */
    @TableField("plugin_id")
    private String pluginId;

    /**
     * 安装包下载附件表wid
     */
    @TableField("download_att_wid")
    private String downloadAttWid;

    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    public Integer getPluginType() {
        return pluginType;
    }

    public void setPluginType(Integer pluginType) {
        this.pluginType = pluginType;
    }
    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }
    public String getDownloadAttWid() {
        return downloadAttWid;
    }

    public void setDownloadAttWid(String downloadAttWid) {
        this.downloadAttWid = downloadAttWid;
    }
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "PluginsEntity{" +
        "wid=" + getWid() +
        ", pluginType=" + pluginType +
        ", pluginId=" + pluginId +
        ", downloadAttWid=" + downloadAttWid +
        ", isDeleted=" + isDeleted +
        ", updateTime=" + getUpdateTime() +
        ", createTime=" + getCreateTime() +
        "}";
    }
}
