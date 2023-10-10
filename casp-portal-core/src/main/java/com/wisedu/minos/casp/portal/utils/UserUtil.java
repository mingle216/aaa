package com.wisedu.minos.casp.portal.utils;

import com.wisedu.minos.casp.portal.model.UserInfo;

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
}
