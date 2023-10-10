package com.wisedu.amp.card.cus.roleserviceitem.model;

import java.util.List;

public class LocalDept {
    private String deptWid;

    private String deptName;

    private String parentWid;

    List<DeptInfo> children;

    public String getDeptWid() {
        return deptWid;
    }

    public void setDeptWid(String deptWid) {
        this.deptWid = deptWid;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getParentWid() {
        return parentWid;
    }

    public void setParentWid(String parentWid) {
        this.parentWid = parentWid;
    }

    public List<DeptInfo> getChildren() {
        return children;
    }

    public void setChildren(List<DeptInfo> children) {
        this.children = children;
    }
}
