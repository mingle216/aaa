package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisedu.minos.api.data.OrgTreeService;
import com.wisedu.minos.api.model.DubboSearchObject;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.dao.entity.MenuAuthEntity;
import com.wisedu.minos.casp.portal.dao.mapper.MenuAuthMapper;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.MinosApiAdapter;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title MenuAuthImpl
 * @Author: jcx
 * @Date: 2020/9/23
 */
@Service
public class MenuAuthService extends ServiceImpl<MenuAuthMapper, MenuAuthEntity> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MenuAuthService.class);

    @Autowired
    UserUtil userUtil;

    @Autowired
    MinosApiAdapter minosApiAdapter;

    @DubboReference
    private OrgTreeService orgTreeService;

    /**
     * getAuthedMenus
     * <p/>
     * 获取用户授权的菜单
     *
     * @param userWid
     * @param menuMenuAuthType
     * @return java.util.List<com.wisedu.minos.casp.portal.dao.entity.MenuAuthEntity>
     * @throws
     * @date 2020/10/10 10:41
     */
    public List<MenuAuthEntity> getAuthedMenus (String userWid, Global.MenuMenuAuthType menuMenuAuthType) {
        UserInfo userInfo;
        try {
            InlineResponse20028 userDetails = minosApiAdapter.getUserDetails(userWid);
            userInfo = userDetails.getData();
        } catch ( Exception e ) {
            LOGGER.info("获取用户失败：", e);
            return Collections.emptyList();
        }

        //获取用户对应用户组
        List<String> groupIds = userInfo.getGroups().stream().map(DubboGroupBeanInfo::getWid).filter(Objects::nonNull).collect(Collectors.toList());
        Set<String> orgIds = getUserOrgIds(userInfo);
        LOGGER.debug("查询菜单权限：userWid:{},groupIds:{},orgIds:{}", userWid, groupIds, orgIds);
        LambdaQueryWrapper<MenuAuthEntity> queryWrapper = Wrappers.<MenuAuthEntity>lambdaQuery()
                .eq(MenuAuthEntity::getAuthRelWid, userWid)
                .eq(MenuAuthEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(MenuAuthEntity::getMenuAuthType, menuMenuAuthType.getId())
                .eq(MenuAuthEntity::getAuthType, Global.MenuAuthType.USER.getId()+"");
        if ( CollectionUtils.isNotEmpty(groupIds) ) {
            queryWrapper.or()
                    .eq(MenuAuthEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                    .eq(MenuAuthEntity::getMenuAuthType, menuMenuAuthType.getId())
                    .eq(MenuAuthEntity::getAuthType, Global.MenuAuthType.DOMAIN_AND_GROUP.getId()+"")
                    .in(MenuAuthEntity::getAuthRelWid, groupIds);
        }
        if ( !orgIds.isEmpty() ) {
            queryWrapper.or()
                    .eq(MenuAuthEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                    .eq(MenuAuthEntity::getMenuAuthType, menuMenuAuthType.getId())
                    .eq(MenuAuthEntity::getAuthType, Global.MenuAuthType.ORG.getId()+"")
                    .in(MenuAuthEntity::getAuthRelWid, orgIds);
        }
        return this.list(queryWrapper);
    }

    /**
     * getUserOrgIds
     * <p/>
     * 获取用户对应组织机构列表,包含子组织并去重
     *
     * @param userInfo
     * @return java.util.Set<java.lang.String>
     * @throws
     * @date 2020/10/10 11:04
     */
    private Set<String> getUserOrgIds (UserInfo userInfo) {
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

}
