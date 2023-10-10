package com.wisedu.minos.casp.portal.common.result;

import java.util.List;

/**
 * 功能描述：AMP分页返回数据类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title PageData
 * @Author: jcx
 * @Date: 2020/8/18
 */
public class PageData<T> {
    private int total;
    private int filterTotal;
    private boolean empty;
    private List<T> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFilterTotal() {
        return filterTotal;
    }

    public void setFilterTotal(int filterTotal) {
        this.filterTotal = filterTotal;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
