package com.wisedu.minos.casp.portal.common.page;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * 功能描述：分页类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title PageResult
 * @Author: jcx
 * @Date: 2020/8/14
 */
public class PageBase implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    private Integer pageSize;
    @TableField(exist = false)
    private Integer pageNumber;
    @TableField(exist = false)
    private String orderByType;
    @TableField(exist = false)
    private String orderByField;

    public PageBase() {
    }

    public PageBase(final Integer pageSize, final Integer pageNum, final String orderByType, final String orderByField) {
        this.pageSize = pageSize;
        this.pageNumber = pageNum;
        this.orderByType = orderByType;
        this.orderByField = orderByField;
    }

    public static long getSerialVersionUID () {
        return serialVersionUID;
    }

    public Integer getPageSize () {
        return pageSize;
    }

    public void setPageSize (Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber () {
        return pageNumber;
    }

    public void setPageNumber (Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getOrderByType () {
        return orderByType;
    }

    public void setOrderByType (String orderByType) {
        this.orderByType = orderByType;
    }

    public String getOrderByField () {
        return orderByField;
    }

    public void setOrderByField (String orderByField) {
        this.orderByField = orderByField;
    }
}
