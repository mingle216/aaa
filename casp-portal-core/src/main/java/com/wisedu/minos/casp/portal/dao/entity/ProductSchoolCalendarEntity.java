package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 系统弹窗
 * </p>
 *
 * @author zkpu
 * @since 2021-05-20
 */
@TableName("t_amp_view_school_calendar")
public class ProductSchoolCalendarEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("calendar_type")
    private int calendarType;

    @TableField("school_year")
    private int schoolYear;

    @TableField("term_code")
    private int termCode;

    @TableField("start_date")
    private LocalDate startDate;

    @TableField("end_date")
    private LocalDate endDate;

    @TableField("is_deleted")
    private int isDeleted;

    public int getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(int calendarType) {
        this.calendarType = calendarType;
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(int schoolYear) {
        this.schoolYear = schoolYear;
    }

    public int getTermCode() {
        return termCode;
    }

    public void setTermCode(int termCode) {
        this.termCode = termCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

}
