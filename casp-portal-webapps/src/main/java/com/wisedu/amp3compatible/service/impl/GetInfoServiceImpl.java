package com.wisedu.amp3compatible.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisedu.amp3compatible.model.*;
import com.wisedu.amp3compatible.service.GetInfoService;
import com.wisedu.amp3compatible.util.Assert;
import com.wisedu.amp3compatible.util.ParameterCheckUtil;
import com.wisedu.minos.api.data.OrgTreeService;
import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.api.model.DubboOrgBeanInfo;
import com.wisedu.minos.api.model.DubboUserInfo;
import com.wisedu.minos.api.model.DubboUserOrUserGroupApiResp;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.*;
import com.wisedu.minos.casp.portal.dao.mapper.*;
import com.wisedu.minos.casp.portal.i18n.DubboI18nService;
import com.wisedu.minos.casp.portal.i18n.Lang;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.MailUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wisedu.amp3compatible.util.PersonalInfoUtil.*;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/8/1
 */
@Service
public class GetInfoServiceImpl implements GetInfoService {

    private static final Logger logger = LoggerFactory.getLogger(GetInfoServiceImpl.class);

    private final static String SEPARATOR_JING = "#";
    public static final int OK_CODE = 200;
    public static final String OK = "success";
    public static final int SYSTEM_ERROR_CODE = 500;
    public static final String SYSTEM_ERROR = "系统错误~~~";

    public static final String DUTY_DEPT_IDS = "dutyDeptIds";
    public static final String ENABLED = "enabled";

    //个人数据授权类型
    public static final int USER = 1;
    public static final int ORGANIZATION = 0;
    public static final int USER_GROUP = 2;

    //1: JSON接口方式
    private static final int JSON = 1;
    //2: MINOS数据源方式
    private static final int MINOS_DB = 2;
    //3: 邮箱
    private static final int MAIL = 3;
    //4: 定制插件
    private static final int CUSTOM_PLUGIN = 4;
    //5: XML
    private static final int XML = 5;

    @Autowired
    UserUtil userUtil;

    @Autowired
    private UserEmailMapper userEmailMapper;

    @Autowired
    private MailPluginMapper mailPluginMapper;

    @Autowired
    private PersonalDataMapper personalDataMapper;

    @Autowired
    private PersonalDataAuthMapper personalDataAuthMapper;

    @Autowired
    private InternationalizationMapper internationalizationMapper;

    @Autowired
    private MailUtil mailUtil;

    @DubboReference
    private UserService userService;
    @DubboReference
    private OrgTreeService orgTreeService;

    @Override
    public Object getPersonalRemindDetail(PersonalRemindDetailRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String userId = request.getUserId();
            String wid = request.getWid();
            String mailAccount = request.getMailAccount();
            ParameterCheckUtil.verifyInput(userId);
            ParameterCheckUtil.verifyInput(wid);
            ParameterCheckUtil.verifyInput(true, true, mailAccount);
            UserInfo user = getUserWithGroupInfoById(userId);
            if ( null == user ) {
                throw new RuntimeException("获取用户为空");
            }
            List<GrsjInfoExtra> datas = getInfoDataDetail(wid, user,"", mailAccount);
            result.put("datas", datas);
            result.put("status", OK_CODE);
            result.put("message", OK);
        } catch ( Exception e ) {
            result.put("status", SYSTEM_ERROR_CODE);
            result.put("message", SYSTEM_ERROR);
            logger.error("获取用户个人数据详情出错！，异常信息：", e);
        }
        return result;
    }

    /**
     * 方法名：获得用户(包含用户组信息)信息
     * <p>功能说明：</p>
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public UserInfo getUserWithGroupInfoById(String userId) {
        UserInfo userInfo = userUtil.getUserByAccount(userId);
        return userInfo;
    }

    @Override
    public PersonalRemindResponse getPersonalRemindList(UserIdEntity userIdEntity) {
        PersonalRemindResponse response = new PersonalRemindResponse();
        try {
            String userId = userIdEntity.getUserId();
            ParameterCheckUtil.verifyInput(userId);
            UserInfo user = getUserWithGroupInfoById(userId);
            if ( null == user ) {
                throw new RuntimeException("获取用户为空");
            }
            logger.info("获取用户[{}]的信息：{}", userId, user);

            List<PersonalDataEntity> personalDataEntityList = personalDataMapper.selectList(null);
            if(CollectionUtils.isEmpty(personalDataEntityList)){
                return response;
            }
            List<String> mainInfolangs = personalDataEntityList.stream().map(e->e.getMainInfoLangKey()).collect(Collectors.toList());
            List<String> subInfolangs = personalDataEntityList.stream().map(e->e.getSubInfoLangKey()).collect(Collectors.toList());
            subInfolangs.addAll(mainInfolangs);
            List<InternationalizationEntity> internationalizationEntities = internationalizationMapper.selectList(new QueryWrapper<InternationalizationEntity>().lambda()
                    .eq(InternationalizationEntity::getIsDeleted, 0).in(!CollectionUtils.isEmpty(subInfolangs),InternationalizationEntity::getLangKey,subInfolangs));
            List<GrsjInfoExtra> grsjInfoExtraList = new ArrayList<>();
            //获取该用户所属的机构等
            List<String> personWids = queryRoleByUserId(user.getWid());
            if(!CollectionUtils.isEmpty(personWids)){
                Map<String,PersonalDataEntity> personalDataEntityMap = personalDataEntityList.stream().collect(Collectors.toMap(PersonalDataEntity::getWid,entity -> entity));
                for(String personalWid:personWids){
                    PersonalDataEntity personalDataEntity = personalDataEntityMap.get(personalWid);
                    if(null == personalDataEntity){
                        continue;
                    }
                    //个人数据类型是邮箱
                    if(personalDataEntity.getSourceType() == 3){
                        List<UserEmailEntity> userEmailEntities = userEmailMapper.selectList(new QueryWrapper<UserEmailEntity>().lambda()
                                .eq(UserEmailEntity::getIsDelete, 0).eq(UserEmailEntity::getUserWid,user.getWid()));
                        if(CollectionUtils.isEmpty(userEmailEntities)){
                            GrsjInfoExtra info = new GrsjInfoExtra();
                            formPersonalData(internationalizationEntities, personalDataEntity, info);
                            info.setIsViewDetail(( short ) 2);
                            grsjInfoExtraList.add(info);
                        }else{
                            for(UserEmailEntity userEmailEntity:userEmailEntities){
                                GrsjInfoExtra info = new GrsjInfoExtra();
                                formPersonalData(internationalizationEntities, personalDataEntity, info);

                                info.setSubTitle(userEmailEntity.getEmail());
                                info.setExtraInfo(userEmailEntity.getEmail());
                                info.setIsViewDetail(( short ) 2);
                                grsjInfoExtraList.add(info);
                            }
                        }
                    }
                    else{
                        GrsjInfoExtra info = new GrsjInfoExtra();
                        formPersonalData(internationalizationEntities, personalDataEntity, info);
                        grsjInfoExtraList.add(info);
                    }
                }

                grsjInfoExtraList = grsjInfoExtraList.stream().sorted(Comparator.comparing(GrsjInfoExtra::getOrderNum)).collect(Collectors.toList());
            }

            response.setDatas(grsjInfoExtraList);
            response.setStatus(OK_CODE);
            response.setMessage(OK);
        } catch ( Exception e ) {
            response.setStatus(SYSTEM_ERROR_CODE);
            response.setMessage(SYSTEM_ERROR);
            logger.error("调用接口getFavoriteApps失败：" + e);
        }
        return response;
    }

    private void formPersonalData(List<InternationalizationEntity> internationalizationEntities, PersonalDataEntity personalDataEntity, GrsjInfoExtra info) {
        info.setWid(personalDataEntity.getWid());
        info.setTitle(personalDataEntity.getTitle());
        info.setSubTitle(personalDataEntity.getMainInfo());
        info.setImptInfo(personalDataEntity.getSubInfo());
        int modeConf = 0;
        if(personalDataEntity.getSourceType() == JSON){
            modeConf = 3;
        }
        if(personalDataEntity.getSourceType() == MINOS_DB){
            modeConf = 1;
        }
        if(personalDataEntity.getSourceType() == MAIL){
            modeConf = 4;
        }
        info.setModeConf((short) modeConf);
        info.setUrlSql(personalDataEntity.getSourceKey());
        info.setIsViewDetail((short) 1);
        info.setLinkUrl(personalDataEntity.getLinkUrl());
        info.setIconUrl(personalDataEntity.getIconUrl());
        info.setOrderNum(Short.valueOf(personalDataEntity.getOrderNumber().toString()));
        info.setRequestType(Short.valueOf(personalDataEntity.getHttpMethod().toString()));
        if(null != personalDataEntity.getSubInfoLangKey()){
            Optional<InternationalizationEntity> first = internationalizationEntities.stream().filter(e->personalDataEntity.getSubInfoLangKey().equals(e.getLangKey()) && "en_US".equals(e.getLangCountry())).findFirst();
            if(first.isPresent()){
                InternationalizationEntity internationalizationEntity= first.get();
                info.setImptInfoEnus(internationalizationEntity.getLangValue());
            }
        }
        if(null != personalDataEntity.getMainInfoLangKey()){
            Optional<InternationalizationEntity> intFirst = internationalizationEntities.stream().filter(e->personalDataEntity.getMainInfoLangKey().equals(e.getLangKey()) && "en_US".equals(e.getLangCountry())).findFirst();
            if(intFirst.isPresent()){
                InternationalizationEntity internationalizationEntity= intFirst.get();
                info.setSubTitleEnus(internationalizationEntity.getLangValue());
            }
        }
    }

    @Override
    public Object getMailAccountInfo(UserIdEntity userIdEntity) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String userId = userIdEntity.getUserId();
            ParameterCheckUtil.verifyInput(userId);
            UserInfo user = getUserWithGroupInfoById(userId);
            if ( null == user ) {
                throw new RuntimeException("获取用户为空");
            }
            Map<String, Object> resultData = new HashMap<>();
            LambdaQueryWrapper<UserEmailEntity> queryWrapper = new LambdaQueryWrapper<UserEmailEntity>()
                    .eq(UserEmailEntity::getUserWid,user.getWid())
                    .eq(UserEmailEntity::getIsDelete,0);
            List<UserEmailEntity> userEmailEntities = userEmailMapper.selectList(queryWrapper);
            List<UserMailDto> mailAccountsList = new ArrayList<>();
            if ( null != userEmailEntities ) {
                for ( UserEmailEntity email : userEmailEntities ) {
                    UserMailDto userMailDto = new UserMailDto();
                    userMailDto.setUserId(userId);
                    userMailDto.setMailAccount(email.getEmail());
                    mailAccountsList.add(userMailDto);
                }
            }

            List<String> mailSuffixes = new ArrayList<>();
            List<MailPluginEntity> mailPlugins = mailPluginMapper.selectList(null);
            if(!CollectionUtils.isEmpty(mailPlugins)){
                mailSuffixes = mailPlugins.stream().map(e->e.getMailSuffix()).collect(Collectors.toList());
            }
            resultData.put("mailAccountsList", mailAccountsList);
            resultData.put("mailDomainList", mailSuffixes);

            result.put("datas", resultData);
            result.put("status", OK_CODE);
            result.put("message", OK);
        } catch ( Exception e ) {
            logger.error("获取邮箱账号信息出错，异常信息：", e);
            result.put("status", SYSTEM_ERROR_CODE);
            result.put("message", SYSTEM_ERROR);
        }
        return result;
    }


    @Override
    public Object getLinkUrl (GetLinkUrlRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String userId = request.getUserId();
            ParameterCheckUtil.verifyInput(userId);
            UserInfo user = getUserWithGroupInfoById(userId);
            if ( null == user ) {
                throw new RuntimeException("获取用户为空");
            }
            String mailAccount = request.getMailAccount();
            ParameterCheckUtil.verifyInput(false, true, mailAccount);
            mailAccount = mailAccount.trim();
            if ( StringUtil.isNotBlank(mailAccount) && checkMailAccountIsCurrentUser(user, mailAccount) ) {
                String linkUrl = mailUtil.getLinkUrl(mailAccount);
                if ( StringUtil.isNotEmpty(linkUrl) ) {
                    result.put("datas", linkUrl);
                    result.put("status", Status.OK_CODE);
                    result.put("message", Status.OK);
                } else {
                    result.put("status", Status.SYSTEM_ERROR_CODE);
                    result.put("message", Status.SYSTEM_ERROR);
                }
            } else {
                result.put("status", Status.SYSTEM_ERROR_CODE);
                result.put("result", "mailAccount is empty or mailAccount isn't currentUser");
            }
        } catch ( Exception e ) {
            logger.error("获取用户单点登录地址出错，异常信息：", e);
            result.put("status", Status.SYSTEM_ERROR_CODE);
            result.put("message", Status.SYSTEM_ERROR);
        }
        return result;
    }


    @Override
    public Object getServiceItemInfo(YanBianServiceReq yanBianServiceReq) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            YanBianServiceRes serviceList = getServiceItemInfoList(yanBianServiceReq);
            result.put("status", OK_CODE);
            result.put("message", OK);
            result.put("datas", serviceList.getYanBianObjList());
        } catch ( Exception e ) {
            logger.error("yanBian 调用接口失败getServiceItemInfo", e);
            result.put("status", SYSTEM_ERROR_CODE);
            result.put("message", SYSTEM_ERROR);
        }
        return result;
    }


    List<GrsjInfoExtra> getInfoDataDetail(String wid, UserInfo user,String extraInfo, String mailAccount){
        List<GrsjInfoExtra> grsjInfoExtraList = new ArrayList<>();
        PersonalDataEntity information = ApplicationContextUtil.get(PersonalDataMapper.class).selectById(wid);
        if ( null == information ){
            return null;
        }
        //判断接口是否为邮箱接口
        if ( null != information.getSourceType() && (MAIL == information.getSourceType()) && StringUtils.isNotEmpty(mailAccount) ) {
            //防止用户恶意获取别人邮箱数据
            Assert.notNull(ApplicationContextUtil.get(MailUtil.class).getMailPluginByAccount(extraInfo), "邮箱账号不合法");
            Assert.isTrue(checkMailAccountIsCurrentUser(user, extraInfo), "非本人邮箱，请勿非法操作！");
            extraInfo = mailAccount;
        }
        //查询多语言数据
        List<InternationalizationEntity> internationalizationEntities = internationalizationMapper.selectList(new QueryWrapper<InternationalizationEntity>().lambda()
                .eq(InternationalizationEntity::getIsDeleted, 0).eq(InternationalizationEntity::getLangKey,information.getMainInfoLangKey())
                .or().eq(InternationalizationEntity::getLangKey,information.getSubInfoLangKey()));

        Map<String, PersonalDataEntity> dataInfoMap = buildLangInfo(information);
        Map<String, PersonalDataInfo> resultMap = getPersonalData(dataInfoMap, user, extraInfo);
        grsjInfoExtraList = new ArrayList<GrsjInfoExtra>();
        GrsjInfoExtra grsjInfoExtra = new GrsjInfoExtra();
        formPersonalData(internationalizationEntities,information,grsjInfoExtra);
        BeanUtils.copyProperties(resultMap.get(Global.DEFAULT_LANGUAGE), grsjInfoExtra);
        grsjInfoExtra.setExtraInfo(mailAccount);
        if ( StringUtils.isNotEmpty(information.getDataSql()) && (! information.getDataSql().equals("userMailACLInformationManager")) )
            grsjInfoExtra.setUrlSql(null);
        grsjInfoExtraList.add(grsjInfoExtra);
        getPersonalInformation(grsjInfoExtraList);
        return grsjInfoExtraList;
    }

    /**
     * 个人数据拼装多语言
     */
    private Map<String, PersonalDataEntity> buildLangInfo (PersonalDataEntity information) {
        //获取目前所支持的语言类型
        DubboI18nService dubboI18nService = ApplicationContextUtil.get(DubboI18nService.class);
        List<Lang> supportLanguages = dubboI18nService.getSupportLanguages();
        Map<String, Map<String, String>> langDataMap = getLangDataMap(supportLanguages.stream().map(Lang::getLangCode).collect(Collectors.toList()));
        Map<String, PersonalDataEntity> dataInfoMap = new HashMap<>();
        for ( Lang item : supportLanguages ) {
            Map<String, String> langMap = langDataMap.get(item.getLangCode());
            if ( null != langMap ) {
                PersonalDataEntity tempInfo = new PersonalDataEntity();
                BeanUtils.copyProperties(information, tempInfo);
                tempInfo.setTitle(null != langMap.get(information.getTitleLangKey()) ? langMap.get(information.getTitleLangKey()) : information.getTitle());
                tempInfo.setMainInfo(null != langMap.get(information.getMainInfoLangKey()) ? langMap.get(information.getMainInfoLangKey()) : information.getMainInfo());
                tempInfo.setSubInfo(null != langMap.get(information.getSubInfoLangKey()) ? langMap.get(information.getSubInfoLangKey()) : information.getSubInfo());
                dataInfoMap.put(item.getLangCode(), tempInfo);
            }
        }
        return dataInfoMap;
    }


    /**
     * Map<lang, Map<lang_key, lang_value>>
     *
     * @param supportLanguages
     * @return
     */
    private Map<String, Map<String, String>> getLangDataMap (List<String> supportLanguages) {
        // 获取国际化map
        InternationalizationMapper internationalizationMapper = ApplicationContextUtil.get(InternationalizationMapper.class);
        List<InternationalizationEntity> list = internationalizationMapper.selectList(Wrappers.<InternationalizationEntity>lambdaQuery()
                .in(InternationalizationEntity::getLangCountry, supportLanguages)
                .eq(InternationalizationEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
        Map<String, Map<String, String>> langDataMap = new HashMap<>();
        if ( org.apache.commons.collections.CollectionUtils.isNotEmpty(list) ) {
            langDataMap = list.stream().collect(Collectors.groupingBy(InternationalizationEntity::getLangCountry,
                    Collectors.toMap(InternationalizationEntity::getLangKey,
                            internationalizationEntity -> internationalizationEntity.getLangValue() == null ? "" : internationalizationEntity.getLangValue(), (oldVal, currVal) -> currVal)
            ));
        }
        return langDataMap;

    }

    public Map<String, PersonalDataInfo> getPersonalData (Map<String, PersonalDataEntity> dataInfoMap, UserInfo user, String extraInfo) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        PersonalDataEntity information = values.get(0);
        Map<String, PersonalDataInfo> resultMap;
        try {
            switch ( information.getSourceType() ) {
                case JSON:
                    //1：json格式
                    getJsonInformation(dataInfoMap, user);
                    break;
                case MINOS_DB:
                    //2：DB模式
                    getDbInformation(dataInfoMap, user);
                    break;
                case MAIL:
                    //3：邮箱格式
                    getMailInformation(dataInfoMap, extraInfo);
                    break;
                case CUSTOM_PLUGIN:
                    getCustomInformation(dataInfoMap, user);
                    break;
                case XML:
                    getXmlInformation(dataInfoMap, user);
                    break;
                default:
            }
        } catch ( Exception e ) {
            logger.warn("获取个人数据失败", e);
        }
        resultMap = buildData(dataInfoMap, extraInfo);
        return resultMap;
    }

    private Map<String, PersonalDataInfo> buildData (Map<String, PersonalDataEntity> dataInfoMap, String extraInfo) {
        return dataInfoMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, item -> {
            PersonalDataInfo dataInfo = new PersonalDataInfo();
            BeanUtils.copyProperties(item.getValue(), dataInfo);
            dataInfo.setAccessTime(new Date());
            dataInfo.setNeedRetrieve(0);
            dataInfo.setIsEmail(3 == item.getValue().getSourceType() ? 1 : 0);
            dataInfo.setExtraInfo(extraInfo);
            return dataInfo;
        }));
    }

    /**
     * 判断是否是本人的邮箱账号
     */
    private boolean checkMailAccountIsCurrentUser (UserInfo user, String mailAccount) {

        int count = ApplicationContextUtil.get(UserEmailMapper.class).selectCount(
                Wrappers.<UserEmailEntity>lambdaQuery()
                        .eq(UserEmailEntity::getUserWid, user.getWid())
                        .eq(UserEmailEntity::getEmail, mailAccount)
                        .eq(UserEmailEntity::getIsDelete, Global.DeleteStatus.NO.getId())
                        .isNull(UserEmailEntity::getSourceType));
        // 判断邮箱账号是否被本人绑定
        return count > 0;
    }

    private void getPersonalInformation (List<GrsjInfoExtra> grsjInfoList) {
        if ( CollectionUtil.isNotEmpty(grsjInfoList) ) {
            for ( int i = grsjInfoList.size() - 1 ; i >= 0 ; i-- ) {
                GrsjInfo grsjInfo = grsjInfoList.get(i);
                if ( StringUtils.isNotEmpty(grsjInfo.getHideCondition()) && grsjInfo.getHideCondition().equals("true") ) {
                    grsjInfoList.remove(i);
                    continue;
                }
                if( StringUtils.isNotEmpty(grsjInfo.getSubTitle())) {
                    String[] subTitleArr = grsjInfo.getSubTitle().split(SEPARATOR_JING);
                    if (subTitleArr.length > 1) {
                        String[] imptInfoArr = grsjInfo.getImptInfo().split(SEPARATOR_JING);
                        for (int j = 0; j < subTitleArr.length; j++) {
                            if (StringUtils.isEmpty(subTitleArr[j]))
                                continue;
                            GrsjInfoExtra info = new GrsjInfoExtra();
                            info.setWid(info.getWid());
                            info.setTitle(grsjInfo.getTitle());
                            info.setSubTitle(subTitleArr[j]);
                            info.setImptInfo(imptInfoArr.length > 1 ? imptInfoArr[j] : "");
                            info.setUrlSql(grsjInfo.getUrlSql());
                            info.setLinkUrl(grsjInfo.getLinkUrl());
                            info.setIconUrl(grsjInfo.getIconUrl());
                            info.setOrderNum(grsjInfo.getOrderNum());
                            info.setIsViewDetail(grsjInfo.getIsViewDetail());
                            info.setModeConf(grsjInfo.getModeConf());
                            info.setHideCondition(grsjInfo.getHideCondition());
                            grsjInfoList.add(info);
                        }
                        grsjInfoList.remove(i);
                    }
                }
            }
        }
    }

    public YanBianServiceRes getServiceItemInfoList(YanBianServiceReq req) {
        YanBianServiceRes yanBianServiceRes = new YanBianServiceRes();
        try {
            JSONObject param = new JSONObject();
            if (CollectionUtil.isNotEmpty(req.getDutyDeptIds())) {
                param.put(DUTY_DEPT_IDS, req.getDutyDeptIds());
            }
            if(null!=req.getEnabled()){
                param.put(ENABLED, req.getEnabled());
            }
            HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);
            yanBianServiceRes = RestTemplateUtils.post("/coosk/serviceItem/queryItemInfos",httpEntity, YanBianServiceRes.class).getBody();
        } catch ( Exception e ) {
            logger.error("yanBian 调用接口失败getServiceItemInfo", e);
        }
        return yanBianServiceRes;

    }

    /**
     * 根据userId获取个人数据授权的数据
     * @param userId
     * @return
     */
    public List<String> queryRoleByUserId(String userId) {
        List<String> result = Lists.newArrayList();
        List<PersonalDataEntity> personalDataEntityList = personalDataMapper.selectList(new QueryWrapper<PersonalDataEntity >().lambda()
                        .eq(PersonalDataEntity::getIsDeleted,Global.DeleteStatus.NO.getId()).eq(PersonalDataEntity::getEnabled,Global.EnableStatus.ENABLE.getId()).eq(PersonalDataEntity::getPersonalData,0));
        if(CollectionUtils.isEmpty(personalDataEntityList)){
            return result;
        }
        List<String> personalWids = personalDataEntityList.stream().map(e->e.getWid()).collect(Collectors.toList());
        List<PersonalDataAuthEntity> personalDataAuthEntities = personalDataAuthMapper.selectList(new QueryWrapper<PersonalDataAuthEntity >().lambda()
                .eq(PersonalDataAuthEntity::getIsDeleted,0).in(PersonalDataAuthEntity::getDataId,personalWids));
        List<String> tempList;

        tempList = personalDataAuthEntities.stream().filter( item ->
                Integer.valueOf(item.getAuthType()) == Integer.valueOf(USER).intValue() &&
                        userId.equals(item.getAuthRelWid()))
                .map(PersonalDataAuthEntity::getDataId).collect(Collectors.toList());
        result.addAll(tempList);

        DubboUserInfo dubboUserIds = new DubboUserInfo();
        dubboUserIds.setWid(userId);
        DubboUserOrUserGroupApiResp dubboUserOrUserGroupApiResp = userService
                .getUserOrUserGroup(dubboUserIds);
        if (null != dubboUserOrUserGroupApiResp && !CollectionUtils
                .isEmpty(dubboUserOrUserGroupApiResp.getGroupIds())) {
            tempList = personalDataAuthEntities.stream().filter( item ->
                    Integer.valueOf(item.getAuthType()) == Integer.valueOf(USER_GROUP).intValue() &&
                            dubboUserOrUserGroupApiResp.getGroupIds().contains(item.getAuthRelWid()))
                    .map(PersonalDataAuthEntity::getDataId).collect(Collectors.toList());
            result.addAll(tempList);
        }
        if (null != dubboUserOrUserGroupApiResp && !CollectionUtils
                .isEmpty(dubboUserOrUserGroupApiResp.getOrgIds())) {
            //用户所属机构ID
            List<String> orgIds = dubboUserOrUserGroupApiResp.getOrgIds();
            //授权给组织机构的数据
            List<PersonalDataAuthEntity> roleRelatives = personalDataAuthEntities.stream().filter( item  ->
                    Integer.valueOf(item.getAuthType()) == Integer.valueOf(ORGANIZATION).intValue()).collect(Collectors.toList());
            if( org.apache.dubbo.common.utils.CollectionUtils.isNotEmpty(roleRelatives)){
                Map<String,List<String>> orgRoleMap= Maps.newHashMap();
                roleRelatives.forEach(o -> {
                    if(MapUtils.isNotEmpty(orgRoleMap)&&orgRoleMap.containsKey(o.getAuthType())){
                        List<String> strings = orgRoleMap.get(o.getAuthRelWid());
                        strings.add(o.getDataId());
                        orgRoleMap.put(o.getAuthRelWid(),strings);
                    }else {
                        List<String> data=Lists.newArrayList();
                        data.add(o.getDataId());
                        orgRoleMap.put(o.getAuthRelWid(),data);
                    }
                });
                //查询用户所属组织机构的所有父级机构
                List<String> userFathOrgWids = getUserFathOrgWids(orgIds);
                if(!CollectionUtils.isEmpty(userFathOrgWids)){
                    userFathOrgWids.forEach(k -> {
                        if(orgRoleMap.containsKey(k)){
                            result.addAll(orgRoleMap.get(k));
                        }
                    });
                }
            }
        }
        return CollectionUtils.isEmpty(result)?result:result.stream().distinct().collect(Collectors.toList());
    }

    /***
     * @Description 查询用户所属组织机构的所有父级机构
     * @Date 19:08 2022/7/13
     * @Param userOrgWids:
     * @return List<String>
     **/
    private List<String> getUserFathOrgWids(List<String> userOrgWids){
        List<String> data=Lists.newArrayList();
        if(!CollectionUtils.isEmpty(userOrgWids)){
            List<DubboOrgBeanInfo> minosOrgList = orgTreeService.searchOrgTrees(new ArrayList<>());
            if(!CollectionUtils.isEmpty(minosOrgList)){
                Map<String, DubboOrgBeanInfo> orgMap = minosOrgList.stream().collect(Collectors.toMap(DubboOrgBeanInfo::getWid, Function.identity(), (key1, key2) -> key2));
                userOrgWids.forEach(k->{
                    List<String> orgWids=Lists.newArrayList();
                    orgWids.add(k);
                    returnOrgFath(orgWids, minosOrgList,orgMap,k);
                    data.addAll(orgWids);
                });
            }
        }
        return CollectionUtils.isEmpty(data)?data:data.stream().distinct().collect(Collectors.toList());
    }

    private void returnOrgFath(List<String> orgWids, List<DubboOrgBeanInfo> orgs,Map<String, DubboOrgBeanInfo> orgMap,String wid) {
        for (int i = 0; i < orgs.size(); i++) {
            DubboOrgBeanInfo dubboOrgBeanInfo = orgs.get(i);
            if(MapUtils.isNotEmpty(orgMap)&&null!=orgMap.get(wid)){
                if (orgMap.get(wid).getpWid().equals(dubboOrgBeanInfo.getWid())) {
                    orgWids.add(dubboOrgBeanInfo.getWid());
                    returnOrgFath(orgWids,orgs, orgMap, dubboOrgBeanInfo.getWid());
                }
            }

        }
    }
}
