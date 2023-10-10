package com.wisedu.minos.casp.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisedu.minos.api.data.OrgTreeService;
import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.api.model.DubboOrgBeanInfo;
import com.wisedu.minos.api.model.*;
import com.wisedu.minos.api.util.MinosDubboException;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.page.PageFactory;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.Assert;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.common.utils.StructureTransitionUtil;
import com.wisedu.minos.casp.portal.model.DubboGroupBeanInfo;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.MinosApiAdapter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import springfox.documentation.schema.Entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述：MinosApi接口
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title MinosApiImpl
 * @Author: jcx
 * @Date: 2020/9/24
 */
@Service
public class MinosApiAdapterImpl implements MinosApiAdapter {

    private static final Logger logger = LogManager.getLogger(MinosApiAdapterImpl.class);

    @DubboReference
    private UserService userService;
    @DubboReference
    private OrgTreeService orgTreeService;


    @Override
    public InlineResponse20031 getFieldGroups(FieldGroupsReq body) {
        Entry[] entries = {};
        HttpEntity entity = new HttpEntity(entries);
        InlineResponse20031 inlineResponse20031 = RestTemplateUtils.post("group/search", entity, InlineResponse20031.class).getBody();
        List<FieldGroupBiz> data = inlineResponse20031.getData();
        if (CollectionUtils.isNotEmpty(data)) {
            if (StringUtil.isEmpty(body.getIsTree()))
                body.setIsTree(Global.CONSTANT_YES);
            List<FieldGroupBiz> result = new ArrayList<>();
            String pWid = "";
            //传单个域及用户组wid   查出该域及用户组和该域及用户组以下子数据
            if (StringUtil.isNotEmpty(body.getWid())) {
                List<FieldGroupBiz> fieldPar = data.stream().filter(s -> s.getWid().equals(body.getWid())).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(fieldPar)) {
                    result.add(fieldPar.get(0));
                    pWid = fieldPar.get(0).getPwid();
                    returnFieldGroupsChild(result, data, body.getWid());
                }
            } else {
                result = data;
            }
            if (Global.CONSTANT_NO.equals(body.getIsTree()))
                return new InlineResponse20031().data(result);
            return new InlineResponse20031().data(fieldGroupToTree(result, pWid, body));
        }
        return new InlineResponse20031();
    }

    /**
     * @Author jcx
     * @Descriptiond 递归查询子级域及用户组
     * @Date 08:30 2020/9/30
     **/
    private List<FieldGroupBiz> returnFieldGroupsChild(List<FieldGroupBiz> list, List<FieldGroupBiz> fieldGroups, String wid) {
        if (CollectionUtils.isNotEmpty(fieldGroups)) {
            for (int i = 0; i < fieldGroups.size(); i++) {
                FieldGroupBiz fieldGroupBiz = fieldGroups.get(i);
                if (wid.equals(fieldGroupBiz.getPwid())) {
                    list.add(fieldGroupBiz);
                    returnFieldGroupsChild(list, fieldGroups, fieldGroupBiz.getWid());
                }
            }
        }
        return list;
    }

    @Override
    public InlineResponse20029 getMinosOrg(OrgInfoTreeReq body) {
        //组装Where条件
        List<DubboSearchObject> paramsList = makeParams(body);
        //调用minos服务获取机构数据
        List<DubboOrgBeanInfo> minosOrgList =new ArrayList<>();
        try{
            minosOrgList = orgTreeService.searchOrgTrees(paramsList);
        }catch (Exception e){
            logger.error("获取minos组织机构异常:",e);
        }

        //组装数据
        List<OrgInfosBiz> orgInfosBizs = null;
        if (CollectionUtils.isNotEmpty(minosOrgList)) {
            String parentId = "";
            List<DubboOrgBeanInfo> dubboOrgBeanInfos = new ArrayList<>();
            if (StringUtil.isNotEmpty(body.getWid())) {
                //传单个机构wid   查出该机构及该机构以下数据
                List<DubboOrgBeanInfo> minosPars = minosOrgList.stream().filter(minosOrg -> minosOrg.getWid().equals(body.getWid())).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(minosPars)) {
                    dubboOrgBeanInfos.addAll(minosPars);
                    returnOrgChild(dubboOrgBeanInfos, minosOrgList, body.getWid());
                    parentId = minosPars.get(0).getpWid();
                }
            } else {
                dubboOrgBeanInfos = minosOrgList;
            }
            orgInfosBizs = filterData(dubboOrgBeanInfos,body, parentId);
        }
        return new InlineResponse20029().data(orgInfosBizs);
    }

    /**
     * @Author jcx
     * @Descriptiond 递归查询子级机构
     * @Date 08:30 2020/9/30
     **/
    private List<DubboOrgBeanInfo> returnOrgChild(List<DubboOrgBeanInfo> list, List<DubboOrgBeanInfo> minosOrgList, String orgWid) {
        if (CollectionUtils.isNotEmpty(minosOrgList)) {
            for (int i = 0; i < minosOrgList.size(); i++) {
                DubboOrgBeanInfo dubboOrgBeanInfo = minosOrgList.get(i);
                if (orgWid.equals(dubboOrgBeanInfo.getpWid())) {
                    list.add(dubboOrgBeanInfo);
                    returnOrgChild(list, minosOrgList, dubboOrgBeanInfo.getWid());
                }
            }
        }
        return list;
    }

    /**
     * @Author jcx
     * @Description 组装Where条件
     * @Date 18:55 2020/4/29
     **/
    private List<DubboSearchObject> makeParams(OrgInfoTreeReq body) {
        List<DubboSearchObject> paramsList = new ArrayList<>();
        return paramsList;
    }

    /**
     * @return List<OrgInfosBiz>
     * @Author jcx
     * @Description 过滤机构数据
     * @Date 14:55 2020/9/24
     * @Param minosOrgList:
     * @Param orgInfosBizs:
     **/
    private List<OrgInfosBiz> filterData(List<DubboOrgBeanInfo> minosOrgList,OrgInfoTreeReq body, String parentId) {
        List<OrgInfosBiz> orgInfosBizs = ObjectUtil.copy(minosOrgList, OrgInfosBiz.class);
        if (StringUtil.isEmpty(body.getIsTree()))
            body.setIsTree(Global.CONSTANT_YES);
        //不返回tree
        if (StringUtils.isNotBlank(body.getIsTree()) && Global.CONSTANT_NO.equals(body.getIsTree()))
            return orgInfosBizs;
        //组装成树
        List<OrgInfosBiz> result = orgListToTree(orgInfosBizs, body, parentId);
        return result;
    }

    /**
     * @Author jcx
     * @Description 将数据组装成树形结构, 带学校一级
     * @Date 16:32 2020/3/24
     **/
    private List<OrgInfosBiz> orgListToTree(List<OrgInfosBiz> list, OrgInfoTreeReq body, String parentId) {
        if (CollectionUtils.isNotEmpty(list)) {
            StructureTransitionUtil.listToTree(list, new StructureTransitionUtil
                    .TransformMatcher<OrgInfosBiz>() {
                @Override
                public void transform(OrgInfosBiz parent, OrgInfosBiz child) {
                    if (null == parent.getChildList()) {
                        parent.setChildList(new ArrayList<OrgInfosBiz>());
                    }
                    if (parent.getWid().equals(child.getPWid())) {
                        parent.getChildList().add(child);
                    }
                }

                @Override
                public boolean remove(OrgInfosBiz object) {
                    if (StringUtil.isNotEmpty(body.getWid())) {
                        return !parentId.equals(object.getPWid());
                    }
                    return !Global.ORG_ROOT_PWID.equals(object.getPWid());
                }
            });
            return list;
        }
        return Collections.emptyList();
    }

    //域及用户组转成树List
    private List<FieldGroupBiz> fieldGroupToTree(List<FieldGroupBiz> list, String pWid, FieldGroupsReq body) {
        if (CollectionUtils.isNotEmpty(list)) {
            StructureTransitionUtil.listToTree(list, new StructureTransitionUtil
                    .TransformMatcher<FieldGroupBiz>() {
                @Override
                public void transform(FieldGroupBiz parent, FieldGroupBiz child) {
                    if (null == parent.getChildList()) {
                        parent.setChildList(new ArrayList<FieldGroupBiz>());
                    }
                    if (parent.getWid().equals(child.getPwid())) {
                        parent.getChildList().add(child);
                    }
                }

                @Override
                public boolean remove(FieldGroupBiz object) {
                    if (StringUtil.isNotEmpty(body.getWid()) && StringUtil.isNotEmpty(pWid)) {
                        return !pWid.equals(object.getPwid());
                    }
                    return !Global.ORG_ROOT_PWID.equals(object.getPwid());
                }
            });
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public InlineResponse20028 getUserDetails(String wid) {
        Assert.notNull(wid, GlobalEnum.PARAM_FAIL.getCode(), GlobalEnum.PARAM_FAIL.getMsg());
        UserInfo userInfo = null;
        try {
            DubboUserInfo userDetail = userService.getUserDetail(wid);
            userInfo = ObjectUtil.copyProperties(userDetail, new UserInfo());
            //dubbo对象 com.wisedu.minos.api.model.DubboGroupBeanInfo  直接复制为com.wisedu.minos.casp.portal.model.DubboGroupBeanInfo时 取值时有类型转化问题，这里单独设置
            List<DubboGroupBeanInfo> groupBeanInfoList = userDetail.getGroups().stream()
                    .map(dubboGroupBeanInfo -> ObjectUtil.copyProperties(dubboGroupBeanInfo, new DubboGroupBeanInfo())).collect(Collectors.toList());

            List<com.wisedu.minos.casp.portal.model.DubboOrgBeanInfo> orgBeanInfoList = userDetail.getOrgs().stream()
                    .map(bean -> ObjectUtil.copyProperties(bean, new com.wisedu.minos.casp.portal.model.DubboOrgBeanInfo())).collect(Collectors.toList());
            userInfo.setOrgs(orgBeanInfoList);
            userInfo.setGroups(groupBeanInfoList);
        } catch (Exception e) {
            logger.info("调用getUserDetails获取用户详情失败，原因为：",e);
        }
        return new InlineResponse20028().data(userInfo);
    }

    @Override
    public InlineResponse20027 searchUser(UserInfoReq body) {
        //开启分页
        Page<UserInfo> page = PageFactory.createPageBeginPage(body);
        PageResponse<DubboUserInfo> dubboUserInfoList = getMinosUser(page, body);
        //组装成用户List
        if (null != dubboUserInfoList) {
            List<DubboUserInfo> datas = dubboUserInfoList.getDatas();
            List<UserInfo> userList = ObjectUtil.copy(datas, UserInfo.class);
            page.setRecords(userList);
            page.setTotal(dubboUserInfoList.getTotal());
            page.setPages(dubboUserInfoList.getPageNumber());

            InlineResponse20027 inlineResponse20027 = new InlineResponse20027().data(userList);
            inlineResponse20027.setPageNumber(page.getCurrent());
            inlineResponse20027.setPageSize(page.getSize());
            inlineResponse20027.setTotalSize(dubboUserInfoList.getTotal());
            return inlineResponse20027;
        }
        return new InlineResponse20027();
    }

    /**
     * 修改用户信息
     * @param dubboUserInfo
     * @return
     */
    @Override
    public InlineResponse20030 updateUserInfo (DubboUserInfo dubboUserInfo) {
        InlineResponse20030 inlineResponse20030=new InlineResponse20030();
        inlineResponse20030.setData(userService.userInfoUpdate(dubboUserInfo));
        return inlineResponse20030;
    }
    /**
     * @return PageResponse<DubboUserInfo>
     * @Author jcx
     * @Description 获取minos用户信息
     * @Date 13:44 2020/8/19
     * @Param page: 分页对象
     * @Param vo:  传值vo
     **/
    private PageResponse<DubboUserInfo> getMinosUser(Page page, UserInfoReq body) {
        PageResponse<DubboUserInfo> dubboUserInfoList = null;
        //组装查询条件
        DubboUserSearchRequest userRequest = getSearchCriteria(page, body);
        try {
            dubboUserInfoList = userService.searchUser(userRequest);
        } catch (Exception re) {// MinosDubboException
            logger.debug("获取minos用户信息发生了异常" + re);

            if (re instanceof MinosDubboException)
                logger.debug(re.getMessage());
            throw new BusinessException("500", re.getMessage());
        }
        return dubboUserInfoList;
    }

    //组装查询条件
    private DubboUserSearchRequest getSearchCriteria(Page page, UserInfoReq body) {
        DubboUserSearchRequest userRequest = new DubboUserSearchRequest();
        List<DubboSearchObject> searchCriteria = new ArrayList<>();
        //默认查状态为正常的用户
        DubboSearchObject dubboSearchState = getSearchObject();
        dubboSearchState.setComparator(DubboSearchObject.ComparatorEnum.EQ);
        dubboSearchState.setField(DbFieldConstant.MINOS_ACCOUNT_STATUS);
        dubboSearchState.setValue(Global.MINOS_ACCOUNT_STATUS);
        searchCriteria.add(dubboSearchState);
        //默认查未删除的用户
        DubboSearchObject dubboSearchStateFir = getSearchObject();
        dubboSearchStateFir.setComparator(DubboSearchObject.ComparatorEnum.EQ);
        dubboSearchStateFir.setField(DbFieldConstant.MINOS_IS_DELETED);
        dubboSearchStateFir.setValue(Global.CONSTANT_NO);
        searchCriteria.add(dubboSearchStateFir);
        //默认查未归档的用户
        DubboSearchObject dubboSearchStateSed = getSearchObject();
        dubboSearchStateSed.setComparator(DubboSearchObject.ComparatorEnum.EQ);
        dubboSearchStateSed.setField(DbFieldConstant.MINOS_IS_ARCHIVED);
        dubboSearchStateSed.setValue(Global.CONSTANT_NO);
        searchCriteria.add(dubboSearchStateSed);
        //关键字条件搜索
        getKeyWordSearch(body.getKeyword(), searchCriteria);

        //根据单个用户 userAccount 筛选用户
        getUserAccountSearch(body.getUserAccount(), searchCriteria);
        //分页
        userRequest.setPageSize(Integer.valueOf(String.valueOf(page.getSize())));
        userRequest.setPageNumber(Integer.valueOf(String.valueOf(page.getCurrent())));

        if (CollectionUtils.isNotEmpty(searchCriteria)) {
            userRequest.setSearchCriteria(searchCriteria);
        }
        logger.debug("查询所有用户信息参数:" + JSON.toJSONString(userRequest));
        return userRequest;
    }

    //组装And查询
    private DubboSearchObject getSearchObject() {
        DubboSearchObject dubboSearchObject = new DubboSearchObject();
        dubboSearchObject.setCondition(DubboSearchObject.ConditionEnum.AND);
        return dubboSearchObject;
    }

    //根据单个用户 userAccount 筛选用户
    private void getUserAccountSearch(String userAccount, List<DubboSearchObject> searchCriteria) {
        if (StringUtils.isNotBlank(userAccount)) {
            DubboSearchObject dubboSearchObject = getSearchObject();
            dubboSearchObject.setComparator(DubboSearchObject.ComparatorEnum.EQ);
            dubboSearchObject.setField(DbFieldConstant.MINOS_USER_ACCOUNT);
            dubboSearchObject.setValue(userAccount);
            searchCriteria.add(dubboSearchObject);
        }
    }

    //关键字条件搜索
    private void getKeyWordSearch(String keyword, List<DubboSearchObject> searchCriteria) {
        if (StringUtils.isNotBlank(keyword)) {
            //关键字条件搜索
            DubboSearchObject dubboSearchObject = getSearchObject();
            dubboSearchObject.setComparator(DubboSearchObject.ComparatorEnum.LIKE);
            dubboSearchObject.setField(DbFieldConstant.MINOS_KEY_WORD);
            dubboSearchObject.setValue(keyword);
            searchCriteria.add(dubboSearchObject);
        }
    }
}
