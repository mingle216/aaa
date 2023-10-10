package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统弹窗
 * </p>
 *
 * @author zkpu
 * @since 2021-05-20
 */
@TableName("t_amp_view_popup_window")
public class PopupWindowEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("pop_title")
    private String popTitle;

    @TableField("title_lang_key")
    private String titleLangKey;

    @TableField("contents_lang_key")
    private String contentsLangKey;

    @TableField("page_wid")
    private String pageWid;

    @TableField("pop_contents")
    private String popContents;

    @TableField("beg_time")
    private LocalDateTime begTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 0不允许 1允许忽略
     */
    @TableField("pop_type")
    private int popType;

    @TableField("is_enabled")
    private int isEnabled;

    @TableField("is_deleted")
    private int isDeleted;

    public String getPopTitle() {
        return popTitle;
    }

    public void setPopTitle(String popTitle) {
        this.popTitle = popTitle;
    }

    public String getTitleLangKey() {
        return titleLangKey;
    }

    public void setTitleLangKey(String titleLangKey) {
        this.titleLangKey = titleLangKey;
    }

    public String getPageWid() {
        return pageWid;
    }

    public void setPageWid(String pageWid) {
        this.pageWid = pageWid;
    }

    public String getPopContents() {
        return popContents;
    }

    public void setPopContents(String popContents) {
        this.popContents = popContents;
    }

    public LocalDateTime getBegTime() {
        return begTime;
    }

    public void setBegTime(LocalDateTime begTime) {
        this.begTime = begTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getPopType() {
        return popType;
    }

    public void setPopType(int popType) {
        this.popType = popType;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getContentsLangKey() {
        return contentsLangKey;
    }

    public void setContentsLangKey(String contentsLangKey) {
        this.contentsLangKey = contentsLangKey;
    }
}
