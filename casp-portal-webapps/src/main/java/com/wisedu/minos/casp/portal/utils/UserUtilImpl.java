package com.wisedu.minos.casp.portal.utils;

import com.alibaba.fastjson.JSON;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.UserInfoReq;
import com.wisedu.minos.casp.portal.service.impl.MinosApiAdapterImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserUtilImpl implements UserUtil {

    private static final Logger logger = LogManager.getLogger(UserUtilImpl.class);

    @Autowired
    private HttpSession session;

    @Autowired
    private MinosApiAdapterImpl minosApi;

    @Override
    public UserInfo getCurrentUser() {
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        if (userInfo == null) {
            return null;
        }
        UserInfo minosUser = (UserInfo) session.getAttribute("minosUserInfo");
        if(minosUser==null){
            minosUser = getUserByAccount(userInfo.getUserAccount());
            if(null!=minosUser){
                session.setAttribute("minosUserInfo",minosUser);
            }
        }
        logger.debug("获取用户：{} ", JSON.toJSONString(minosUser));
        return minosUser;
    }


    /***
     * @Author jcx
     * @Description 根据用户帐号获取用户信息
     * @Date 10:34 2020/8/19
     * @Param userAccount:
     * @return UserBean
     **/
    @Override
    public UserInfo getUserByAccount(String userAccount) {
        UserInfoReq vo = new UserInfoReq();
        vo.setUserAccount(userAccount);
        List<UserInfo> dataList = minosApi.searchUser(vo).getData();
        if (CollectionUtils.isNotEmpty(dataList)) {
            return dataList.get(0);
        }
        return null;
    }

    /**
     * @return String
     * @Author jcx
     * @Description 得到当前用户帐号
     * @Date 14:21 2020/8/19
     **/
    @Override
    public String getUserAccount() {
        UserInfo currentUser = this.getCurrentUser();
        return currentUser == null ? null : currentUser.getUserAccount();
    }
}
