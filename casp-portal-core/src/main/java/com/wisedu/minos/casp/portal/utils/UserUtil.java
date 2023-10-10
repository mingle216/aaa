package com.wisedu.minos.casp.portal.utils;

import com.wisedu.minos.casp.portal.model.UserInfo;

import java.util.Set;

public interface UserUtil {

    /**
     * getCurrentUser
     * <p/>
     * 获取当前用户 
     * @param 
     * @throws 
     * @return com.wisedu.minos.casp.portal.model.UserInfo
     * @date 2020-10-1 19:51
     */  
    UserInfo getCurrentUser();


    /***
     * @Author jcx
     * @Description 根据用户帐号获取用户信息
     * @Date 10:34 2020/8/19
     * @Param userAccount:
     * @return UserBean
     **/
    UserInfo getUserByAccount(String userAccount);

    /**
     * @return String
     * @Author jcx
     * @Description 得到当前用户帐号
     * @Date 14:21 2020/8/19
     **/
    String getUserAccount();

    /***
     * 获取用户的组织机构
     * @param userInfo
     * @return java.util.Set<java.lang.String>
     * @author jszhang
     * @date 2021/11/29 18:38
     */
    Set<String> getUserOrgIds (UserInfo userInfo);
    /***
     * 获取用户组
     * @param userInfo
     * @return java.util.List<java.lang.String>
     * @author jszhang
     * @date 2021/11/29 18:41
     */
    Set<String> getUserGroupIds(UserInfo userInfo);
}
