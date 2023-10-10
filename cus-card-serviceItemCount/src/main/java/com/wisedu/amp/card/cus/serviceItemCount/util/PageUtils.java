package com.wisedu.amp.card.cus.serviceItemCount.util;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName PageUtils
 * @Description
 * @Date 2021/1/26 0026 16:48
 * @Author zkpu
 * @Version 1.0
 **/
public class PageUtils<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 第几页
     */
    private int pageNum;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 当前页的数量
     */
    private int size;
    /**
     * 结果集
     */
    private List<T> data;

    public PageUtils(List<T> list, int pageNum, int pageSize){
        List<T> pageList = list.stream().skip(pageSize * (pageNum - 1)).limit(pageSize).collect(Collectors.toList());
        this.setTotal(list.size());
        this.setPages((list.size() - 1) / pageSize + 1);
        this.setPageNum(pageNum);
        this.setPageSize(pageSize);
        this.setData(pageList);
        this.setSize(pageList.size());
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageUtils{" +
            "total=" + total +
            ", pageNum=" + pageNum +
            ", pageSize=" + pageSize +
            ", pages=" + pages +
            ", size=" + size +
            ", data=" + data +
            '}';
    }

}