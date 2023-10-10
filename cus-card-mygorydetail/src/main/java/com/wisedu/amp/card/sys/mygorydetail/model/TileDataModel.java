package com.wisedu.amp.card.sys.mygorydetail.model;

import java.util.List;

public class TileDataModel {

    private String roleWid;

    private String roleName;

    private List<ServiceItemModel> itemModelList;

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

    public List<ServiceItemModel> getItemModelList() {
        return itemModelList;
    }

    public void setItemModelList(List<ServiceItemModel> itemModelList) {
        this.itemModelList = itemModelList;
    }
}
