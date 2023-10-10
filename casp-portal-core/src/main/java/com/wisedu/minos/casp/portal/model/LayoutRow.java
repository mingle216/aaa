package com.wisedu.minos.casp.portal.model;

import java.io.Serializable;
import java.util.List;

/**
 * 行元素
 * 行元素中只能放列元素
 *
 */
public class LayoutRow implements Serializable  {
    /**
     * 列元素列表
     */
    private List<LayoutColumn> columns;

    public List<LayoutColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<LayoutColumn> columns) {
        this.columns = columns;
    }
}
