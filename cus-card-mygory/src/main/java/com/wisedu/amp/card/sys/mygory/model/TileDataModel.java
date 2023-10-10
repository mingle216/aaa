package com.wisedu.amp.card.sys.mygory.model;

import java.util.List;

public class TileDataModel {

    private String roleWid;

    private String roleName;

    private List<ServiceItemCategoryModel> itemModelList;

    public String getRoleWid() {
        return roleWid;
    }

    public void setRoleWid(String roleWid) {
        this.roleWid = roleWid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<ServiceItemCategoryModel> getItemModelList() {
        return itemModelList;
    }

    public void setItemModelList(List<ServiceItemCategoryModel> itemModelList) {
        this.itemModelList = itemModelList;
    }
}
