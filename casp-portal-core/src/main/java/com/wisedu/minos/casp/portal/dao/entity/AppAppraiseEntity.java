package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 应用服务评价
 * </p>
 * @author yujiaxin
 * @date 2021/12/13 13:39
 */
@TableName("t_amp_app_appraise")
public class AppAppraiseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务主键
     */
    @TableId("wid")
    private String wid;

    /**
     * 应用id
     */
    @TableField("app_id")
    private String appId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 评分
     */
    @TableField("score")
    private Float score;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 是否删除 1：正常 0：删除
     */
    @TableField("is_deleted")
    private String isDeleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 图片
     */
    @TableField("appraise_pics")
    private String appraisePics;

    /**
     * 管理员回复
     */
    @TableField("admin_comment")
    private String adminComment;

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppraisePics() {
        return appraisePics;
    }

    public void setAppraisePics(String appraisePics) {
        this.appraisePics = appraisePics;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

    @Override
    public String toString() {
        return "AppAppraiseEntity{" +
                "wid=" + wid +
                ", appId=" + appId +
                ", userId=" + userId +
                ", score=" + score +
                ", content=" + content +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}

