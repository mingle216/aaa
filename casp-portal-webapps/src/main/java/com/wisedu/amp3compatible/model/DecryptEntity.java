package com.wisedu.amp3compatible.model;

/**
 * Created by 01111178 on 2017/11/2.
 */
public class DecryptEntity {
    private String userId;

    private String groupId;

    public DecryptEntity(){

    }

    public DecryptEntity(String userId,String groupId){
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
