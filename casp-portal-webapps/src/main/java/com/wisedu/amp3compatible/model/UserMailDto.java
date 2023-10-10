package com.wisedu.amp3compatible.model;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/8/8
 */

public class UserMailDto {

    private String userId;

    private String mailAccount;

    private String mailPwd;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMailAccount() {
        return mailAccount;
    }

    public void setMailAccount(String mailAccount) {
        this.mailAccount = mailAccount;
    }

    public String getMailPwd() {
        return mailPwd;
    }

    public void setMailPwd(String mailPwd) {
        this.mailPwd = mailPwd;
    }
}
