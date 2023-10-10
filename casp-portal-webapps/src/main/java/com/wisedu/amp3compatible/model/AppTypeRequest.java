package com.wisedu.amp3compatible.model;

import java.util.List;

/**
 * Created by glhan on 2016/6/22.
 */
public class AppTypeRequest {
    /**
     * 用户id，为空表示游客
     */
    private String userId;
    /**
     * 服务角色Id：学生，教师，游客
     */
    private String roleId;
    private List<RunPlatform> runPlatformList;
    private int limitNum;

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

    public List<RunPlatform> getRunPlatformList() {
        return runPlatformList;
    }

    public void setRunPlatformList(List<RunPlatform> runPlatformList) {
        this.runPlatformList = runPlatformList;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }
}
