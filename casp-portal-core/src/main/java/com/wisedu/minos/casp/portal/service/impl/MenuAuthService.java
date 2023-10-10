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
import org.springframework.util.StringUtils;

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
public class MenuAuthService extends MyServiceImpl<MenuAuthMapper, MenuAuthEntity> {

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
     * @param menuMenuAuthType
     * @return java.util.List<com.wisedu.minos.casp.portal.dao.entity.MenuAuthEntity>
     * @throws
     * @date 2020/10/10 10:41
     */
    public List<MenuAuthEntity> getAuthedMenus (Global.MenuMenuAuthType menuMenuAuthType) {
        UserInfo userInfo = userUtil.getCurrentUser();
        if(null==userInfo){
            return Collections.emptyList();
        }
        //获取用户对应用户组
        List<String> groupIds = userInfo.getGroups().stream().map(DubboGroupBeanInfo::getWid).filter(Objects::nonNull).collect(Collectors.toList());
        Set<String> orgIds = getUserOrgIds(userInfo);
        LOGGER.debug("查询菜单权限：userWid:{},groupIds:{},orgIds:{}", userInfo.getWid(), groupIds, orgIds);
        LambdaQueryWrapper<MenuAuthEntity> queryWrapper = Wrappers.<MenuAuthEntity>lambdaQuery()
                .eq(MenuAuthEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(MenuAuthEntity::getMenuAuthType, menuMenuAuthType.getId());
        List<MenuAuthEntity> list = this.list(queryWrapper);
        // 将筛选放在内存中，减少sql的枚举值，有效利用mybatis plus的二级缓存
        list = list.stream().filter(item -> {
            boolean flag;
            if ( String.valueOf(Global.MenuAuthType.USER.getId()).equals(item.getAuthType())
                    && userInfo.getWid().equals(item.getAuthRelWid()) ) {
                flag = true;
            }else if ( CollectionUtils.isNotEmpty(groupIds) && String.valueOf(Global.MenuAuthType.DOMAIN_AND_GROUP.getId()).equals(item.getAuthType())
                    && groupIds.contains(item.getAuthRelWid()) ) {
                flag = true;
            }else if ( (!orgIds.isEmpty()) && String.valueOf(Global.MenuAuthType.ORG.getId()).equals(item.getAuthType())
                    && orgIds.contains(item.getAuthRelWid()) ) {
                flag = true;
            }else {
                flag = false;
            }
            return flag;
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * getUserOrgIds
     * <p/>
     * 获取用户对应组织机构列表,包含父组织并去重
     *
     * @param userInfo
     * @return java.util.Set<java.lang.String>
     * @throws
     * @date 2020/10/10 11:04
     */
    private Set<String> getUserOrgIds (UserInfo userInfo) {
        //获取所选组织id的所有父组织
        if(null != userInfo && CollectionUtils.isNotEmpty(userInfo.getOrgs())){
            Set<String> wids = userInfo.getOrgs().stream().map(DubboOrgBeanInfo::getWid).collect(Collectors.toSet());
            Set<String> parentOrgWids = orgTreeService.getAllParentOrgWids(wids);
            if( !org.springframework.util.CollectionUtils.isEmpty(parentOrgWids) ){
                wids.addAll(parentOrgWids);
            }
            return wids;
        }

        return new HashSet<>();
    }
}
