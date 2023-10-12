package com.wisedu.minos.casp.portal.utils;

import com.alibaba.fastjson.JSON;
import com.wisedu.minos.api.data.OrgTreeService;
import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.api.model.DubboSearchObject;
import com.wisedu.minos.api.model.DubboUserInfo;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.model.DubboGroupBeanInfo;
import com.wisedu.minos.casp.portal.model.DubboOrgBeanInfo;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.UserInfoReq;
import com.wisedu.minos.casp.portal.service.impl.MinosApiAdapterImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserUtilImpl implements UserUtil {

    private static final Logger logger = LogManager.getLogger(UserUtilImpl.class);

    @Autowired
    private HttpSession session;

    @Autowired
    private MinosApiAdapterImpl minosApi;

    @DubboReference
    private OrgTreeService orgTreeService;

    @DubboReference
    private UserService userService;

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
            DubboUserInfo userDetail = userService.getUserDetail(dataList.get(0).getWid());
            UserInfo userInfo = ObjectUtil.copyProperties(userDetail, new UserInfo());
            //dubbo对象 com.wisedu.minos.api.model.DubboGroupBeanInfo  直接复制为com.wisedu.minos.casp.portal.model.DubboGroupBeanInfo时 取值时有类型转化问题，这里单独设置
            List<DubboGroupBeanInfo> groupBeanInfoList = userDetail.getGroups().stream()
                    .map(dubboGroupBeanInfo -> ObjectUtil.copyProperties(dubboGroupBeanInfo, new DubboGroupBeanInfo())).collect(Collectors.toList());

            List<com.wisedu.minos.casp.portal.model.DubboOrgBeanInfo> orgBeanInfoList = userDetail.getOrgs().stream()
                    .map(bean -> ObjectUtil.copyProperties(bean, new com.wisedu.minos.casp.portal.model.DubboOrgBeanInfo())).collect(Collectors.toList());
            userInfo.setOrgs(orgBeanInfoList);
            userInfo.setGroups(groupBeanInfoList);
            return userInfo;
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
    /***
     * 获取用户组织结构
     * @param userInfo
     * @return java.util.Set<java.lang.String>
     * @author jszhang
     * @date 2021/11/29 18:40
     */
    @Override
    public Set<String> getUserOrgIds(UserInfo userInfo) {
        //获取所选组织id的所有父组织
        Set<com.wisedu.minos.api.model.DubboOrgBeanInfo> orgIdsSet = new HashSet<>();
        userInfo.getOrgs().stream().map(DubboOrgBeanInfo::getWid).forEach(orgWid -> {
            List<DubboSearchObject> dubboSearchObjects = new ArrayList<>();
            DubboSearchObject dubboSearchObject = new DubboSearchObject();
            dubboSearchObject.setField("wid");
            dubboSearchObject.setComparator(DubboSearchObject.ComparatorEnum.EQ);
            dubboSearchObject.setValue(orgWid);
            dubboSearchObjects.add(dubboSearchObject);
            //返回当前组织及所有父组织
            List<com.wisedu.minos.api.model.DubboOrgBeanInfo> orgBeanInfoList = orgTreeService.searchOrgTrees(dubboSearchObjects);
            orgIdsSet.addAll(orgBeanInfoList);
        });
        return orgIdsSet.stream().map(com.wisedu.minos.api.model.DubboOrgBeanInfo::getWid).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getUserGroupIds(UserInfo userInfo) {
        return userInfo.getGroups().stream().map(DubboGroupBeanInfo::getWid).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
