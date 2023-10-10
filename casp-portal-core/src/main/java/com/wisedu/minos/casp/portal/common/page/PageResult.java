package com.wisedu.minos.casp.portal.common.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wisedu.minos.casp.portal.model.PaginationApiResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：分页结果类
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
public class PageResult<T>  implements Serializable  {
	private static final long serialVersionUID = -4071521319254024213L;

	private List<T> data;

	private Long pageNumber = null;

	private Long pageSize = null;

	private Long totalSize = null;

	private String errcode = "0";

	private String errmsg = "请求成功";

	private String traceId = null;

	public List<T> getData () {
		return data;
	}

	public void setData (List<T> data) {
		this.data = data;
	}

	public Long getPageNumber () {
		return pageNumber;
	}

	public void setPageNumber (Long pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Long getPageSize () {
		return pageSize;
	}

	public void setPageSize (Long pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotalSize () {
		return totalSize;
	}

	public void setTotalSize (Long totalSize) {
		this.totalSize = totalSize;
	}

	public String getErrcode () {
		return errcode;
	}

	public void setErrcode (String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg () {
		return errmsg;
	}

	public void setErrmsg (String errmsg) {
		this.errmsg = errmsg;
	}

	public String getTraceId () {
		return traceId;
	}

	public void setTraceId (String traceId) {
		this.traceId = traceId;
	}

	public PageResult() {
		this.pageNumber = 1L;
		this.pageSize = 10L;
	}
	public PageResult(Long currentPage,Long pageSize,Long totalPage,Long totalRows,
					  boolean hasNextPage,boolean hasPreviousPage,List<T> dataList) {
		this.pageNumber = currentPage;
		this.pageSize = pageSize;
		this.totalSize = totalRows;
		this.data = dataList;
	}

	public PageResult(IPage<T> page) {
		this.setData(page.getRecords());
		this.setTotalSize(page.getTotal());
		this.setPageNumber(page.getCurrent());
		this.setPageSize(page.getSize());
	}
	public static long getSerialVersionUID () {
		return serialVersionUID;
	}


}
