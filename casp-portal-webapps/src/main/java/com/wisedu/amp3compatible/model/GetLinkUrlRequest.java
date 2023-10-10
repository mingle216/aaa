package com.wisedu.amp3compatible.model;

public class GetLinkUrlRequest {
    private String userId;
    private String mailAccount;

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
}
