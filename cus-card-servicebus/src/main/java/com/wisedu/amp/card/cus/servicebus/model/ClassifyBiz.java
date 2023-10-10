package com.wisedu.amp.card.cus.servicebus.model;

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
 * @title ClassifyBiz
 * @Author: u
 * @Date: 2020/10/10
 */
public class ClassifyBiz implements Serializable {
    private String classifyWid;
    private String parentId;
    private Boolean show;
    private String classifyName;
    private String classifyIconUrl;
    private Integer count;

    private List<ClassifyBiz> classifyInfoList;

    public String getClassifyWid () {
        return classifyWid;
    }

    public void setClassifyWid (String classifyWid) {
        this.classifyWid = classifyWid;
    }

    public String getParentId () {
        return parentId;
    }

    public void setParentId (String parentId) {
        this.parentId = parentId;
    }

    public String getClassifyName () {
        return classifyName;
    }

    public void setClassifyName (String classifyName) {
        this.classifyName = classifyName;
    }

    public String getClassifyIconUrl () {
        return classifyIconUrl;
    }

    public void setClassifyIconUrl (String classifyIconUrl) {
        this.classifyIconUrl = classifyIconUrl;
    }

    public List<ClassifyBiz> getClassifyInfoList () {
        return classifyInfoList;
    }

    public void setClassifyInfoList (List<ClassifyBiz> classifyInfoList) {
        this.classifyInfoList = classifyInfoList;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }
}
