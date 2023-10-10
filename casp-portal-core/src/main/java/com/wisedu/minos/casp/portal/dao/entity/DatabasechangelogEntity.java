package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jcx
 * @since 2021-06-01
 */
@TableName("databasechangelog")
public class DatabasechangelogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;

    @TableField("author")
    private String author;

    @TableField("filename")
    private String filename;

    @TableField("dateexecuted")
    private LocalDateTime dateexecuted;

    @TableField("orderexecuted")
    private Double orderexecuted;

    @TableField("exectype")
    private String exectype;

    @TableField("md5sum")
    private String md5sum;

    @TableField("description")
    private String description;

    @TableField("comments")
    private String comments;

    @TableField("tag")
    private String tag;

    @TableField("liquibase")
    private String liquibase;

    @TableField("contexts")
    private String contexts;

    @TableField("labels")
    private String labels;

    @TableField("deployment_id")
    private String deploymentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    public LocalDateTime getDateexecuted() {
        return dateexecuted;
    }

    public void setDateexecuted(LocalDateTime dateexecuted) {
        this.dateexecuted = dateexecuted;
    }
    public Double getOrderexecuted() {
        return orderexecuted;
    }

    public void setOrderexecuted(Double orderexecuted) {
        this.orderexecuted = orderexecuted;
    }
    public String getExectype() {
        return exectype;
    }

    public void setExectype(String exectype) {
        this.exectype = exectype;
    }
    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getLiquibase() {
        return liquibase;
    }

    public void setLiquibase(String liquibase) {
        this.liquibase = liquibase;
    }
    public String getContexts() {
        return contexts;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }
    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }
    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    @Override
    public String toString() {
        return "DatabasechangelogEntity{" +
        "id=" + id +
        ", author=" + author +
        ", filename=" + filename +
        ", dateexecuted=" + dateexecuted +
        ", orderexecuted=" + orderexecuted +
        ", exectype=" + exectype +
        ", md5sum=" + md5sum +
        ", description=" + description +
        ", comments=" + comments +
        ", tag=" + tag +
        ", liquibase=" + liquibase +
        ", contexts=" + contexts +
        ", labels=" + labels +
        ", deploymentId=" + deploymentId +
        "}";
    }
}
