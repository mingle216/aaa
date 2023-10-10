package com.wisedu.minos.casp.portal.model;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title QueryAllAppResponse
 * @Author: hlchen02
 * @Date: 2020/9/8
 */
public class QueryAllServiceItemResponse extends AmpBaseResponse implements Serializable {
    private List<RecommendServiceItems> data;

    private Integer pageSize;

    private Integer totalSize;

    private Integer total;

    public List<RecommendServiceItems> getData() {
        return data;
    }

    public void setData(List<RecommendServiceItems> data) {
        this.data = data;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalSize () {
        return totalSize;
    }

    public void setTotalSize (Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
