package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/**
 * <p>
 * 附件信息表
 * </p>
 *
 * @author jcx
 * @since 2020-09-16
 */
@TableName("t_amp_attach_info")
public class AttachInfoEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件地址全路径
     */
    @TableField("file_url")
    private String fileUrl;

    /**
     * 文件地址路径
     */
    @TableField("file_location")
    private String fileLocation;

    /**
     * 文件后缀名
     */
    @TableField("file_suffix")
    private String fileSuffix;

    /**
     * 是否删除 1：正常 0：删除
     */
    @TableField("is_deleted")
    private String isDeleted;

    /**
     * 操作人的wid
     */
    @TableField("operate_wid")
    private String operateWid;

    /**
     * 操作人的名称
     */
    @TableField("operate_name")
    private String operateName;

    @TableField(exist = false)
    private String reUrl;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getOperateWid() {
        return operateWid;
    }

    public void setOperateWid(String operateWid) {
        this.operateWid = operateWid;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getReUrl() {
        return reUrl;
    }

    public void setReUrl(String reUrl) {
        this.reUrl = reUrl;
    }
}
