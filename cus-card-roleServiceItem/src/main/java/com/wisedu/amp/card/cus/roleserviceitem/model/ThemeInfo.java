package com.wisedu.amp.card.cus.roleserviceitem.model;

import java.util.List;

public class ThemeInfo {

    private String subjectWid;
    private String subjectName;
    List<ThemeInfo> children;

    public String getSubjectWid() {
        return subjectWid;
    }

    public void setSubjectWid(String subjectWid) {
        this.subjectWid = subjectWid;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<ThemeInfo> getChildren() {
        return children;
    }

    public void setChildren(List<ThemeInfo> children) {
        this.children = children;
    }
}
