package com.wisedu.amp.card.cus.hotApp.model;

import java.util.List;

public class HotAppRequestOld {
    private String userId;
    private String roleId;
    private List<String> runPlatformList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getRunPlatformList() {
        return runPlatformList;
    }

    public void setRunPlatformList(List<String> runPlatformList) {
        this.runPlatformList = runPlatformList;
    }

}
