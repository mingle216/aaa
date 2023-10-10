package com.wisedu.minos.casp.portal.vo;

import com.wisedu.minos.casp.portal.common.page.PageBase;

import java.util.List;

/**
 * 功能描述：用户信息Vo
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title UserInfoVo
 * @Author: jcx
 * @Date: 2020/8/19
 */
public class UserInfoVo extends PageBase {

    private String userAccount;//用户工号
    private List<String> userAccountList;//用户工号List
    private List<String> userWidList;//人员主键List
    private List<String> userWids;//人员主键List(not in 用)

    public List<String> getUserWids () {
        return userWids;
    }

    public void setUserWids (List<String> userWids) {
        this.userWids = userWids;
    }

    public List<String> getUserWidList () {
        return userWidList;
    }

    public void setUserWidList (List<String> userWidList) {
        this.userWidList = userWidList;
    }

    public String getUserAccount () {
        return userAccount;
    }

    public void setUserAccount (String userAccount) {
        this.userAccount = userAccount;
    }

    public List<String> getUserAccountList () {
        return userAccountList;
    }

    public void setUserAccountList (List<String> userAccountList) {
        this.userAccountList = userAccountList;
    }
}
