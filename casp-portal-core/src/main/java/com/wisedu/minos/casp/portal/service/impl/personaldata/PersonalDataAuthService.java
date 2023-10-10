package com.wisedu.minos.casp.portal.service.impl.personaldata;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wisedu.minos.api.data.OrgTreeService;
import com.wisedu.minos.api.model.DubboSearchObject;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataAuthEntity;
import com.wisedu.minos.casp.portal.dao.mapper.PersonalDataAuthMapper;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.personal.PersonalDataGrantDto;
import com.wisedu.minos.casp.portal.model.personal.PersonalDataGrantVo;
import com.wisedu.minos.casp.portal.service.MinosApiAdapter;
import com.wisedu.minos.casp.portal.service.impl.MinosApiAdapterImpl;
import com.wisedu.minos.casp.portal.service.impl.MyServiceImpl;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 个人数据授权
 *
 * @author jszhang
 * @date 2021/7/20 10:08
 * @version 1.0
 */
@Service
public class PersonalDataAuthService extends MyServiceImpl<PersonalDataAuthMapper, PersonalDataAuthEntity> {

    @Autowired
    UserUtil userUtil;

    @Autowired
    MinosApiAdapter minosApiAdapter;

    @DubboReference
    private OrgTreeService orgTreeService;

    @Autowired
    private MinosApiAdapterImpl minosApi;

    public void saveAuth(PersonalDataGrantDto dto) {
        this.update(new UpdateWrapper<PersonalDataAuthEntity>().lambda()
                .eq(PersonalDataAuthEntity::getDataId, dto.getWid())
                .set(PersonalDataAuthEntity::getIsDeleted, 1));
        List<PersonalDataAuthEntity> personalDataAuthEntities = dto.getAuthDtos().stream()
                .map(e -> new PersonalDataAuthEntity().setAuthRelWid(e.getAuthRelWid())
                        .setAuthType(e.getAuthType()).setDataId(dto.getWid()).setIsDeleted(0)).collect(Collectors.toList());
        this.saveBatch(personalDataAuthEntities);
    }

    /**
     * getAuthedMenus
     * <p/>
     * 获取用户授权的菜单
     *
     * @param userWid
     * @return java.util.List<com.wisedu.minos.casp.portal.dao.entity.MenuAuthEntity>
     * @throws
     * @date 2020/10/10 10:41
     */
    public List<PersonalDataAuthEntity> getAuthPersonalData(String userWid) {
        UserInfo userInfo = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        //获取用户对应用户组
        List<String> groupIds = userInfo.getGroups().stream().map(DubboGroupBeanInfo::getWid).filter(Objects::nonNull).collect(Collectors.toList());
        Set<String> orgIds = getUserOrgIds(userInfo);
        LambdaQueryWrapper<PersonalDataAuthEntity> queryWrapper = Wrappers.<PersonalDataAuthEntity>lambdaQuery()
                .eq(PersonalDataAuthEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
        List<PersonalDataAuthEntity> list = this.list(queryWrapper);
        list = list.stream()
                .filter(item -> (item.getAuthType().equals(String.valueOf(Global.MenuAuthType.USER.getId())) && item.getAuthRelWid().equals(userWid)) ||
                        (item.getAuthType().equals(String.valueOf(Global.MenuAuthType.DOMAIN_AND_GROUP.getId())) && groupIds.contains(item.getAuthRelWid())) ||
                        (item.getAuthType().equals(String.valueOf(Global.MenuAuthType.ORG.getId())) && orgIds.contains(item.getAuthRelWid())))
                .collect(Collectors.toList());
        return list;
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
    private Set<String> getUserOrgIds(UserInfo userInfo) {
        //获取所选组织id的所有父组织
        if(null != userInfo && CollectionUtils.isNotEmpty(userInfo.getOrgs())){
            Set<String> wids = userInfo.getOrgs().stream().map(DubboOrgBeanInfo::getWid).collect(Collectors.toSet());
            return orgTreeService.getAllParentOrgWids(wids);
        }

        return new HashSet<>();
    }

    public CommonResponseResult<List<PersonalDataGrantVo>> grantQuery(String wid) {
        LambdaQueryWrapper<PersonalDataAuthEntity> eq = Wrappers.<PersonalDataAuthEntity>lambdaQuery()
                .eq(PersonalDataAuthEntity::getDataId, wid)
                .eq(PersonalDataAuthEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
        //查询机构信息
        OrgInfoTreeReq orgInfoTreeReq = new OrgInfoTreeReq();
        orgInfoTreeReq.setIsTree(Global.CONSTANT_NO);
        InlineResponse20029 minosOrg = minosApi.getMinosOrg(orgInfoTreeReq);

        //机构list转map
        Map<String, String> orgMap = getOrgMap(minosOrg.getData());
        //查询域及用户组信息
        FieldGroupsReq fieldGroupsReq = new FieldGroupsReq();
        fieldGroupsReq.setIsTree(Global.CONSTANT_NO);
        InlineResponse20031 fieldGroups = minosApi.getFieldGroups(fieldGroupsReq);
        //域及用户组list转Map
        Map<String, String> fieldGroupMap = getFieldGroupMap(fieldGroups.getData());
        List<PersonalDataGrantVo> collect = this.list(eq).stream().map(e ->
        {
            PersonalDataGrantVo personalDataGrantVo = new PersonalDataGrantVo().setAuthRelWid(e.getAuthRelWid()).setAuthType(e.getAuthType());
            if (null != orgMap && e.getAuthType().equals(String.valueOf(Global.MenuAuthType.ORG.getId()))) {
                //组织机构类型 赋值组织机构名称
                personalDataGrantVo.setAuthRelName(null != orgMap.get(e.getAuthRelWid()) ? orgMap.get(e.getAuthRelWid()) : "");
            } else if (null != fieldGroupMap && e.getAuthType().equals(String.valueOf(Global.MenuAuthType.DOMAIN_AND_GROUP.getId()))) {
                //赋值域及用户组名称
                personalDataGrantVo.setAuthRelName(null != fieldGroupMap.get(e.getAuthRelWid()) ? fieldGroupMap.get(e.getAuthRelWid()) : "");
            } else if (e.getAuthType().equals(String.valueOf(Global.MenuAuthType.USER.getId()))) {
                //赋值用户名
                InlineResponse20028 userDetails = minosApi.getUserDetails(e.getAuthRelWid());
                if (null != userDetails && userDetails.getData() != null) {
                    personalDataGrantVo.setAuthRelName(userDetails.getData().getUserName());
                    personalDataGrantVo.setUserId(userDetails.getData().getUserAccount());

                }
            }
            return personalDataGrantVo;
        }).filter(e-> StringUtils.isNotBlank(e.getAuthRelName())).collect(Collectors.toList());

        return new CommonResponseResult<List<PersonalDataGrantVo>>().setData(collect);
    }

    //组织机构list转Map
    private Map<String, String> getOrgMap(List<OrgInfosBiz> orgInfosBizs) {
        Map<String, String> orgMap = null;
        if (CollectionUtils.isNotEmpty(orgInfosBizs)) {
            orgMap = orgInfosBizs.stream().collect(
                    Collectors.toMap(OrgInfosBiz::getWid,
                            OrgInfosBiz::getName, (oldVal, currVal) -> currVal));
        }
        return orgMap;
    }

    //域及用户组List转map
    private Map<String, String> getFieldGroupMap(List<FieldGroupBiz> fieldGroupBizs) {
        Map<String, String> fieldGroupMap = null;
        if (CollectionUtils.isNotEmpty(fieldGroupBizs)) {
            fieldGroupMap = fieldGroupBizs.stream().collect(
                    Collectors.toMap(FieldGroupBiz::getWid,
                            FieldGroupBiz::getName, (oldVal, currVal) -> currVal));
        }
        return fieldGroupMap;
    }

}
