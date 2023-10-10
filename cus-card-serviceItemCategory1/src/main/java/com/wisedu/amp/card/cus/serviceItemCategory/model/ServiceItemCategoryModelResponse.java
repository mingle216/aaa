package com.wisedu.amp.card.cus.serviceItemCategory1.model;

import java.io.Serializable;
import java.util.List;

public class ServiceItemCategoryModelResponse implements Serializable {

    private String roleWid;

    private List<ServiceItemCategoryModel> data;

    public String getRoleWid() {
        return roleWid;
    }

    public void setRoleWid(String roleWid) {
        this.roleWid = roleWid;
    }

    public List<ServiceItemCategoryModel> getData() {
        return data;
    }

    public void setData(List<ServiceItemCategoryModel> data) {
        this.data = data;
    }
}
