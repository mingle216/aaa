package com.wisedu.amp.card.cus.serviceItemCount.model;


import java.util.List;

/**
 * @ClassName RoleModel
 * @Description
 * @Date 2021/1/27 0027 9:44
 * @Author zkpu
 * @Version 1.0
 **/
public class RoleModel {

    private String roleWid;

    private List<MultiLangData> roleName;

    public String getRoleWid() {
        return roleWid;
    }

    public void setRoleWid(String roleWid) {
        this.roleWid = roleWid;
    }

    public List<MultiLangData> getRoleName() {
        return roleName;
    }

    public void setRoleName(
        List<MultiLangData> roleName) {
        this.roleName = roleName;
    }
}
