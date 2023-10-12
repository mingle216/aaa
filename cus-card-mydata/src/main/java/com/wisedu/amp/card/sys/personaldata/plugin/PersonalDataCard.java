package com.wisedu.amp.card.sys.personaldata.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.wisedu.amp.card.sys.personaldata.model.CheckBox;
import com.wisedu.amp.card.sys.personaldata.model.ConfigModel;
import com.wisedu.amp.card.sys.personaldata.model.PersonalDataInfo;
import com.wisedu.amp.card.sys.personaldata.model.PersonalDataResponse;
import com.wisedu.amp.card.sys.personaldata.service.*;
import com.wisedu.amp.card.sys.personaldata.util.SourceTypeUtil;
import com.wisedu.minos.api.config.SystemToolsService;
import com.wisedu.minos.api.model.MessageSynResponse;
import com.wisedu.minos.api.model.Receiver;
import com.wisedu.minos.api.model.SynMessageInfo;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.DateUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.*;
import com.wisedu.minos.casp.portal.dao.mapper.*;
import com.wisedu.minos.casp.portal.i18n.DubboI18nService;
import com.wisedu.minos.casp.portal.i18n.Lang;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.service.impl.personaldata.PersonalDataAuthService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.*;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.gateway.client.license.MinosLicenseManager;
import com.wisedu.minos.util.MinosException;
import com.wisedu.minos.util.jdbc.JDBCTools;
import com.wisedu.minos.util.jdbc.bean.JDBCConnectParamBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@MinosSPI
public class PersonalDataCard extends AbstractCard {
    private static final Logger LOGGER = LogManager.getLogger(PersonalDataCard.class);

    // 勾选 上次登录时间
    public static final int LAST_LOGIN_TIME = 1;

    // 勾选 上次登录ip
    public static final int LAST_LOGIN_IP = 2;

    public static final String INTERNAL_PERSONAL_WID = "3b032f77193e4b65b78cf89936dba5f9";

    @Override
    public String getCardId () {
        return "SYS_CARD_PERSONALDATA";
    }

    @Override
    public GroupTemplate getGroupTemplate () {
        GroupTemplate groupTemplate = super.getGroupTemplate();
        if ( null == groupTemplate ) {
            ClasspathResourceLoader classResourceLoader = new ClasspathResourceLoader();
            try {
                Configuration cfg = Configuration.defaultConfiguration();
                groupTemplate = new GroupTemplate(classResourceLoader, cfg);
                groupTemplate.registerFormat("xss", new XSSDefenseFormat());
                groupTemplate.registerFormat("scriptFilter", new ScriptDefenseFormat());
            } catch ( IOException e ) {
                LOGGER.error("模板引擎初始化失败");
                throw new RuntimeException("模板引擎初始化失败", e);
            }
        }
        return groupTemplate;
    }

    @Override
    public void destroy () {
        LOGGER.info("destroy..");
    }

    @Override
    public Object execDispatcher (CardAjaxRequest cardAjaxRequest) {
        Object result = null;
        if(!checkLogin()){
            return result;
        }
        switch ( cardAjaxRequest.getMethod() ) {
            case "getMarkNumber":
                result = 0;
                break;
            case "getPersonalDataList":
                result = this.getDataList(cardAjaxRequest);
                break;
            case "getPersonalDataDetail":
                result = this.getDataDetail(cardAjaxRequest);
                break;
            case "existsAccount":
                result = this.existsAccount();
                break;
            case "bindMail":
                result = this.bindMail(cardAjaxRequest);
                break;
            case "unBindMail":
                result = this.unBindMail(cardAjaxRequest);
                break;
            case "sendVerificationMessage":
                result = this.sendVerificationMessage(cardAjaxRequest);
                break;
            case "getMailSuffix":
                result = this.getMailSuffix();
                break;
            case "getLinkUrl":
                result = this.getLinkUrl(cardAjaxRequest);
                break;
            case "renderData":
                result = this.renderData(cardAjaxRequest);
                break;
            case "hasAccountBound":
                result = this.hasAccountBound(cardAjaxRequest);
                break;
            case "configuredData":
                result = this.getConfiguredData(cardAjaxRequest);
            default:
        }

        return result;
    }

    @Override
    public ComponentContainer getConfig (CardConfigReq cardConfigReq) {
        ComponentContainer componentContainer = cardConfigReq.getComponentContainer();
        PersonalDataMapper personalDataMapper = ApplicationContextUtil.get(PersonalDataMapper.class);
        List<PersonalDataEntity> informationList =
                personalDataMapper.selectList(Wrappers.<PersonalDataEntity>lambdaQuery()
                        .eq(PersonalDataEntity::getEnabled, 1)
                        .eq(PersonalDataEntity::getIsDeleted, 0)
                        .orderByDesc(PersonalDataEntity::getOrderNumber));
        List<CheckBox> checkBoxes = informationList.stream().filter(e -> 0==e.getPersonalData()).map(e -> new CheckBox().setLabel(e.getTitle()).setValue(e.getWid())).collect(Collectors.toList());
        componentContainer.setData("personalDatas", Global.ComponentParam.DATAS, checkBoxes);

        List<PersonalDataEntity> personalDataEntityList = informationList.stream().filter(e -> 1==e.getPersonalData()).collect(Collectors.toList());
        componentContainer.setData("personalInfo",Global.ComponentParam.DATAS,personalDataEntityList);
        return super.getConfig(cardConfigReq);
    }

    /**
     * 获取配置信息
     */
    private Object renderData (CardAjaxRequest request) {
        //获取配置信息
        String configInfoStr = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        if ( StringUtil.isNotEmpty(configInfoStr) ) {
            configInfoStr = JSON.parse(configInfoStr).toString();
        }
        return JSONObject.parseObject(configInfoStr);
    }

    /**
     * 获取个人信息数据
     * @param request
     * @return
     */
    private PersonalDataResponse getConfiguredData(CardAjaxRequest request){
        PersonalDataResponse personalDataResponse = new PersonalDataResponse();
        try {

            //获取配置的个人卡片
            String configInfoStr = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
            if (StringUtils.isNotEmpty(configInfoStr)) {
                configInfoStr = JSON.parse(configInfoStr).toString();
            }
            ConfigModel configModel = JSON.parseObject(configInfoStr, ConfigModel.class);
            String lang = request.getParam().get("lang");
            if (configModel.getPersonalInfo().getIsDisplay() == 0) {
                return null;
            }
            //展示个人数据，根据数据源查询个人信息
            LambdaQueryWrapper<PersonalDataEntity> wrapper = new LambdaQueryWrapper<PersonalDataEntity>()
                    .eq(PersonalDataEntity::getIsDeleted, 0)
                    .eq(PersonalDataEntity::getEnabled, 1)
                    .eq(PersonalDataEntity::getWid, configModel.getPersonalInfo().getDataSource());
            List<PersonalDataEntity> personalDataEntityList = ApplicationContextUtil.get(PersonalDataMapper.class).selectList(wrapper);
            if (CollectionUtils.isEmpty(personalDataEntityList)) {
                return null;
            }
            PersonalDataEntity personalDataEntity = personalDataEntityList.get(0);
            //内置个人数据，读取当前登录用户信息
            if (INTERNAL_PERSONAL_WID.equals(personalDataEntity.getWid())) {
                UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
                personalDataResponse.setWid(currentUser.getWid());
                personalDataResponse.setPicUrl(currentUser.getUserIcon());
                personalDataResponse.setName(currentUser.getUserName());
                personalDataResponse.setStuNumber(currentUser.getUserAccount());
                String orgnizationName = currentUser.getDeptName() == null? "":currentUser.getDeptName();
                orgnizationName=orgnizationName.replace(';','；');
                personalDataResponse.setOrganizationName(orgnizationName);
            } else {
                int sourceType = personalDataEntity.getSourceType();
                switch (sourceType) {
                    //JSON接口获取个人数据
                    case 1:
                        setResponseFromJSON(personalDataResponse, personalDataEntity, lang);
                        break;
                    //外部数据源获取数据
                    case 2:
                        setResponseFromDB(personalDataResponse, personalDataEntity, lang);
                        break;
                }
            }
            //根据配置查询上次登录时间和ip
            if (configModel.getPersonalInfo().getInfoList().contains(LAST_LOGIN_TIME) || configModel.getPersonalInfo().getInfoList().contains(LAST_LOGIN_IP)) {
                //从redis中读取当前用户的登录信息
                setLastLogTimeAndLogIp(personalDataResponse, request);
                return personalDataResponse;
            }
        } catch ( Exception e ) {
            LOGGER.error("获取个人数据列表失败,返回错误信息", e);
        }
        return personalDataResponse;
    }

    private void setLastLogTimeAndLogIp(PersonalDataResponse personalDataResponse, CardAjaxRequest request){
        String logTime = "";
        String logIp = "";
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if(null!=currentUser){
            String userAccount=currentUser.getUserAccount() ;
            String redisKey = Global.USER_LOGIN_INFO_PREFIX + userAccount;
            String redisLoginData = (String) ApplicationContextUtil.get(RedisUtil.class).get(redisKey);
            List<UserLoginEntity> userLoginEntityList = JSON.parseObject(redisLoginData, new TypeReference<List<UserLoginEntity>>() {
            });
            List<UserLoginEntity> userLoginEntities = userLoginEntityList.stream().sorted(Comparator.comparing(UserLoginEntity::getCreateTime).reversed()).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(userLoginEntities)){
                List<UserLoginEntity> userLoginData = ApplicationContextUtil.get(UserLoginMapper.class)
                        .selectList(new LambdaQueryWrapper<UserLoginEntity>().eq(UserLoginEntity::getUserAccount,userAccount));
                if(userLoginEntityList.size()>1){
                    userLoginEntities = userLoginData.stream().sorted(Comparator.comparing(UserLoginEntity::getCreateTime).reversed()).collect(Collectors.toList()).subList(0,2);
                }else{
                    userLoginEntities = userLoginData;
                }
            }
            if (CollectionUtils.isNotEmpty(userLoginEntities) && userLoginEntities.size() > 1) {
                logTime = DateUtil.getStrFromDate(userLoginEntities.get(1).getCreateTime(), DateUtil.DATE_FORMATE_STRING_A);
                logIp = userLoginEntities.get(1).getLoginIp();
                if (StringUtils.isNotBlank(logIp)) {
                    String[] strIp = logIp.split(",");
                    if (strIp.length > 0) {
                        personalDataResponse.setLastLogIp(strIp[0]);
                    }
                }
                personalDataResponse.setLastLogTime(logTime);
            }
        }

    }

    private void setResponseFromJSON(PersonalDataResponse personalDataResponse,PersonalDataEntity personalDataEntity,String lang ){
        StringBuilder url = new StringBuilder(personalDataEntity.getSourceKey());
        String resultStr;
        if ("1".equals(personalDataEntity.getHttpMethod())) {
            // post请求
            JSONObject entityJson = new JSONObject();
            entityJson.put("langKey", lang);

            resultStr = RestTemplateUtils.post(url.toString(), entityJson, String.class).getBody();
        } else {
            try {
                // get请求
                if (url.toString().contains("?")) {
                    url.append("&");
                } else {
                    url.append("?");
                }
                url.append("langKey").append("=").append(lang);
                resultStr = RestTemplateUtils.get(url.toString(), String.class).getBody();
            } catch (Exception e) {
                throw new MinosException("调用JSON接口出错", e);
            }
        }
        PersonalDataDto personalDataDto = JSON.parseObject(resultStr, PersonalDataDto.class);
        personalDataResponse.setPicUrl(personalDataDto.getPicUrl());
        personalDataResponse.setName(personalDataDto.getName());
        personalDataResponse.setStuNumber(personalDataDto.getStuNumber());
        personalDataResponse.setOrganizationName(personalDataDto.getOrgName());
    }

    private void setResponseFromDB(PersonalDataResponse personalDataResponse,PersonalDataEntity personalDataEntity,String lang ){
        try {
            JDBCConnectParamBean jdbcConnectParamBean = ApplicationContextUtil.get(DataSourceUtil.class).getJDBCConnectParamBeanByName(personalDataEntity.getSourceKey());
            List<Map<String, String>> matchInformationList;
            matchInformationList = JDBCTools.query(jdbcConnectParamBean, personalDataEntity.getDataSql(), null);
            Map<String, String> matchInformationMap = CollectionUtils.isNotEmpty(matchInformationList) ? matchInformationList.get(0) : Collections.emptyMap();
            String orgName = "orgname" + lang;
            if (StringUtils.isBlank(matchInformationMap.get(orgName))) {
                personalDataResponse.setOrganizationName(matchInformationMap.get("orgname_zh_cn"));
            } else {
                personalDataResponse.setOrganizationName(matchInformationMap.get(orgName));
            }
            personalDataResponse.setPicUrl(matchInformationMap.get("pic_url"));
            personalDataResponse.setStuNumber(matchInformationMap.get("stu_number"));
            personalDataResponse.setName(matchInformationMap.get("name"));
        } catch (Exception e) {
            throw new MinosException("查询外部数据源出错", e);
        }
    }

    /**
     * 获取个人数据列表
     *
     * @param request
     * @return
     */
    private List<PersonalDataInfo> getDataList (CardAjaxRequest request) {
        List<PersonalDataInfo> personalDataInfoList = new ArrayList<>();
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

            //获取配置的个人卡片
            String configInfoStr = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
            if ( StringUtils.isNotEmpty(configInfoStr) ) {
                configInfoStr = JSON.parse(configInfoStr).toString();
            }
            ConfigModel configModel = JSON.parseObject(configInfoStr, ConfigModel.class);
            if ( CollectionUtils.isEmpty(configModel.getPersonalDatas()) ) {
                return personalDataInfoList;
            }

            // 从数据库获取用户个人数据
            PersonalDataAuthService personalDataAuthService = ApplicationContextUtil.get(PersonalDataAuthService.class);
            List<String> wids = personalDataAuthService.getAuthPersonalData(user.getWid()).stream().map(PersonalDataAuthEntity::getDataId).collect(Collectors.toList());
            wids.retainAll(configModel.getPersonalDatas());
            if ( wids.isEmpty() ) {
                return personalDataInfoList;
            }
            PersonalDataMapper personalDataMapper = ApplicationContextUtil.get(PersonalDataMapper.class);
            List<PersonalDataEntity> informationList =
                    personalDataMapper.selectList(Wrappers.<PersonalDataEntity>lambdaQuery()
                            .eq(PersonalDataEntity::getEnabled, 1)
                            .eq(PersonalDataEntity::getIsDeleted, 0)
                            .orderByDesc(PersonalDataEntity::getOrderNumber));
            if ( CollectionUtils.isEmpty(informationList) ) {
                return personalDataInfoList;
            }
            informationList = informationList.stream().filter(item -> wids.contains(item.getWid())).peek(e->e.setIsHiddenPrivacy(e.getIsHiddenPrivacy()==null?0:e.getIsHiddenPrivacy())).collect(Collectors.toList());

            // 获取国际化数据
            String localeStr = LocaleContextHolder.getLocale().toString();
            Map<String, String> langDataMap = getLangDataMap(Collections.singletonList(localeStr)).get(localeStr);
            for ( PersonalDataEntity information : informationList ) {
                // 转换为当前语言
                castLanguage(langDataMap, information);

                if ( SourceTypeUtil.MAIL == information.getSourceType() ) {
                    getMailInformation(information, user, personalDataInfoList);
                    continue;
                }

                PersonalDataInfo dataInfo = getCacheData(information, user, "");
                if ( StringUtil.isNotEmpty(dataInfo.getIconUrl()) ) {
                    dataInfo.setIconUrl(ApplicationContextUtil.get(CardUtil.class).filterHttpOrHttps(dataInfo.getIconUrl()));
                }
                personalDataInfoList.add(dataInfo);
            }
        } catch ( Exception e ) {
            LOGGER.error("获取个人数据列表失败,返回错误信息", e);
        }
        return personalDataInfoList;
    }

    /**
     * 获取邮箱数据
     *
     * @param information
     * @param user
     * @param personalDataInfoList
     */
    private void getMailInformation (PersonalDataEntity information, UserInfo user, List<PersonalDataInfo> personalDataInfoList) {
        List<String> accountList = getUserAccountList(user);
        if ( CollectionUtils.isEmpty(accountList) ) {
            //若发现用户未绑定邮箱或绑定的邮箱不合法，则直接将主要信息、次要信息置为空，且needRetrieve为0返回。
            PersonalDataInfo info = new PersonalDataInfo();
            BeanUtils.copyProperties(information, info);
            info.setIsHiddenPrivacy(info.getIsHiddenPrivacy()==null?0:info.getIsHiddenPrivacy());
            info.setNeedRetrieve(0);
            info.setMainInfo("");
            info.setSubInfo("");
            info.setIsEmail(1);
            info.setOriginalMainInfo(information.getMainInfo());
            if ( StringUtil.isNotEmpty(information.getIconUrl()) ) {
                info.setIconUrl(ApplicationContextUtil.get(CardUtil.class).filterHttpOrHttps(information.getIconUrl()));
            }
            personalDataInfoList.add(info);
        } else {
            // 先获取缓存数据
            accountList.forEach(item -> {
                personalDataInfoList.add(getCacheData(information, user, item));
            });
        }
    }


    private void castLanguage (Map<String, String> langDataMap, PersonalDataEntity information) {
        if ( null != langDataMap ) {
            // 添加国际化信息
            information.setMainInfo(null == langDataMap.get(information.getMainInfoLangKey()) ? information.getMainInfo() : langDataMap.get(information.getMainInfoLangKey()));
            information.setTitle(null == langDataMap.get(information.getTitleLangKey()) ? information.getTitle() : langDataMap.get(information.getTitleLangKey()));
            information.setSubInfo(null == langDataMap.get(information.getSubInfoLangKey()) ? information.getSubInfo() : langDataMap.get(information.getSubInfoLangKey()));
        }

    }


    /**
     * 获取用户的可用合法邮箱账号列表
     *
     * @param user
     * @return
     */
    private List<String> getUserAccountList (UserInfo user) {
        List<String> accountList = Lists.newArrayList();
        UserEmailMapper userEmailMapper = ApplicationContextUtil.get(UserEmailMapper.class);
        List<UserEmailEntity> userEmailEntities = userEmailMapper.selectList(Wrappers.<UserEmailEntity>lambdaQuery()
                .eq(UserEmailEntity::getUserWid, user.getWid())
                .eq(UserEmailEntity::getIsDelete, Global.DeleteStatus.NO.getId())
                .isNull(UserEmailEntity::getSourceType)
                .orderByAsc(UserEmailEntity::getOrderNumber));
        if ( CollectionUtils.isEmpty(userEmailEntities) ) {
            return accountList;
        }
        MailUtil mailUtil = ApplicationContextUtil.get(MailUtil.class);
        return userEmailEntities.stream()
                .filter(item -> null != mailUtil.getMailPluginByAccount(item.getEmail()))
                .map(UserEmailEntity::getEmail)
                .collect(Collectors.toList());
    }


    /**
     * 获取缓存的key值
     */
    private String buildCacheKey (PersonalDataEntity information, UserInfo user, String extraInfo) {
        StringBuilder cacheKey = new StringBuilder(information.getWid()).append(getCardId());
        // 数据获取方式为JSON时且"user_key"为空 || 数据获取方式为MINOS数据源方式且sql中不含?，缓存key直接为个人数据wid；
        // 其他情况，缓存key为个人数据wid_userId
        boolean addUserId = (SourceTypeUtil.JSON != information.getSourceType() || StringUtil.isNotEmpty(information.getUserKey()))
                && (SourceTypeUtil.MINOS_DB != information.getSourceType() || information.getDataSql().contains(SourceTypeUtil.URL_PARAM_HOLDER));
        if ( addUserId ) {
            cacheKey.append("_").append(user.getWid());
        }
        if ( SourceTypeUtil.MAIL == information.getSourceType() ) {
            cacheKey.append("_").append(extraInfo);
        }
        return cacheKey.toString();
    }

    /**
     * 获取缓存中的数据，如果缓存失效，则设置needRetrieve=1,需要重新获取数据
     *
     * @param information
     * @param user
     * @param extraInfo
     * @return
     */
    private PersonalDataInfo getCacheData (PersonalDataEntity information, UserInfo user, String extraInfo) {
        String cacheKey = buildCacheKey(information, user, extraInfo);
        Map<String, PersonalDataInfo> dataInfoMap = ( Map<String, PersonalDataInfo> ) ApplicationContextUtil.get(RedisUtil.class).get(cacheKey);

        PersonalDataInfo dataInfo = null != dataInfoMap ? dataInfoMap.get(LocaleContextHolder.getLocale().toString()) : new PersonalDataInfo();
        // 是否需要重新获取数据，如果是静态文字 或 "数据获取时间"+数据库中的"cache_timeout" < 当前时间 则不需要获取
        if ( null != dataInfo.getAccessTime() && dataInfo.getAccessTime().getTime() + information.getCacheTimeout() * 1000 > System.currentTimeMillis() ) {
            dataInfo.setNeedRetrieve(0);
        } else {
            BeanUtils.copyProperties(information, dataInfo);
            dataInfo.setIsHiddenPrivacy(dataInfo.getIsHiddenPrivacy()==null?0:dataInfo.getIsHiddenPrivacy());
            dataInfo.setNeedRetrieve(SourceTypeUtil.STATIC_TEXT == information.getSourceType() ? 0 : 1);
            dataInfo.setIsEmail(SourceTypeUtil.MAIL == information.getSourceType() ? 1 : 0);
            dataInfo.setExtraInfo(extraInfo);
            dataInfo.setOriginalMainInfo(dataInfo.getMainInfo());
        }
        if ( StringUtil.isNotEmpty(dataInfo.getIconUrl()) ) {
            dataInfo.setIconUrl(ApplicationContextUtil.get(CardUtil.class).filterHttpOrHttps(dataInfo.getIconUrl()));
        }
        return dataInfo;
    }

    /**
     * 根据wid获取单条个人数据信息
     */
    private PersonalDataInfo getDataDetail (CardAjaxRequest request) {
        String wid = request.getParam().get("wid");
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        PersonalDataEntity information = ApplicationContextUtil.get(PersonalDataMapper.class).selectById(wid);
        if ( StringUtil.isNotEmpty(information.getIconUrl()) ) {
            information.setIconUrl(ApplicationContextUtil.get(CardUtil.class).filterHttpOrHttps(information.getIconUrl()));
        }
        Assert.notNull(information, "个人数据不存在....");
        information.setIsHiddenPrivacy(information.getIsHiddenPrivacy()==null?0:information.getIsHiddenPrivacy());
        Map<String, PersonalDataEntity> dataInfoMap = buildLangInfo(information);

        String extraInfo = request.getParam().get("extraInfo");
        // 判断是否为邮箱
        if ( SourceTypeUtil.MAIL == information.getSourceType() ) {
            Assert.hasLength(extraInfo, "extraInfo不能为空");
            // 判断传入的邮箱账号是否是本人绑定的
            Assert.notNull(ApplicationContextUtil.get(MailUtil.class).getMailPluginByAccount(extraInfo), "邮箱账号不合法");
            Assert.isTrue(checkMailAccountIsCurrentUser(user, extraInfo), "非本人邮箱，请勿非法操作！");
        }

        // 通过第三方系统获取数据
        Map<String, PersonalDataInfo> resultMap = getPersonalData(dataInfoMap, user, extraInfo);

        String localeStr = LocaleContextHolder.getLocale().toString();
        return resultMap.get(localeStr);
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
        if ( CollectionUtils.isNotEmpty(list) ) {
            langDataMap = list.stream().collect(Collectors.groupingBy(InternationalizationEntity::getLangCountry,
                    Collectors.toMap(InternationalizationEntity::getLangKey,
                            internationalizationEntity -> internationalizationEntity.getLangValue() == null ? "" : internationalizationEntity.getLangValue(), (oldVal, currVal) -> currVal)
            ));
        }
        return langDataMap;

    }

    private Map<String, PersonalDataInfo> getPersonalData (Map<String, PersonalDataEntity> dataInfoMap, UserInfo user, String extraInfo) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        Map<String,String> langMainInfo =new HashMap<>();
        dataInfoMap.entrySet().forEach(e->langMainInfo.put(e.getKey(),e.getValue().getMainInfo()));
        PersonalDataEntity information = values.get(0);
        Map<String, PersonalDataInfo> resultMap;
        boolean hasException = Boolean.FALSE;
        try {
            switch ( information.getSourceType() ) {
                case SourceTypeUtil.JSON:
                    //1：json格式
                    UserJsonACLInformationService.getInformation(dataInfoMap, user);
                    break;
                case SourceTypeUtil.MINOS_DB:
                    //2：DB模式
                    UserDbACLInformationService.getInformation(dataInfoMap, user);
                    break;
                case SourceTypeUtil.MAIL:
                    //3：邮箱格式
                    UserMailACLInformationService.getInformation(dataInfoMap, extraInfo);
                    break;
                case SourceTypeUtil.CUSTOM_PLUGIN:
                    UserCustomACLInformationService.getInformation(dataInfoMap, user);
                    break;
                case SourceTypeUtil.XML:
                    UserXmlACLInformationService.getInformation(dataInfoMap, user);
                    break;
                default:
            }
        } catch ( Exception e ) {
            LOGGER.warn("获取个人数据失败", e);
            hasException = Boolean.TRUE;
        }
        resultMap = buildData(dataInfoMap, extraInfo,langMainInfo);
        if ( information.getCacheTimeout() > 0 && ! hasException ) {
            // 存缓存
            String cacheKey = buildCacheKey(information, user, extraInfo);
            ApplicationContextUtil.get(RedisUtil.class).set(cacheKey, resultMap, information.getCacheTimeout(), TimeUnit.SECONDS);
        }
        return resultMap;
    }

    private Map<String, PersonalDataInfo> buildData (Map<String, PersonalDataEntity> dataInfoMap, String extraInfo,Map<String, String> langMainInfo) {
        return dataInfoMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, item -> {
            PersonalDataInfo dataInfo = new PersonalDataInfo();
            BeanUtils.copyProperties(item.getValue(), dataInfo);
            dataInfo.setAccessTime(new Date());
            dataInfo.setNeedRetrieve(0);
            dataInfo.setIsEmail(SourceTypeUtil.MAIL == item.getValue().getSourceType() ? 1 : 0);
            dataInfo.setExtraInfo(extraInfo);
            dataInfo.setOriginalMainInfo(langMainInfo.get(item.getKey()));
            return dataInfo;
        }));
    }

    /**
     * 解绑邮箱
     *
     * @param
     */
    private Object unBindMail (CardAjaxRequest request) {
        ResultData retModel = new ResultData();
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

            String personalDataId = request.getParam().get("wid");
            String mailAccount = request.getParam().get("mailAccount");

            // 逻辑删除
            ApplicationContextUtil.get(UserEmailMapper.class).update(null, Wrappers.<UserEmailEntity>lambdaUpdate()
                    .eq(UserEmailEntity::getUserWid, user.getWid())
                    .eq(UserEmailEntity::getEmail, mailAccount)
                    .isNull(UserEmailEntity::getSourceType)
                    .set(UserEmailEntity::getIsDelete, Global.DeleteStatus.YES.getId()));

            RedisUtil redisUtil = ApplicationContextUtil.get(RedisUtil.class);
            // 清除缓存(缓存key为个人数据id_userId)
            redisUtil.del(personalDataId + "_" + user.getWid() + "_" + mailAccount);
            retModel.setErrcode(ResultData.DEFAULT_SUCCESS_CODE);
            retModel.setErrmsg("解绑邮箱成功");
        } catch ( Exception e ) {
            retModel.setErrcode(ResultData.DEFAULT_ERROR_CODE);
            retModel.setErrmsg("解绑邮箱出错:" + e.getMessage());
            LOGGER.error("解绑邮箱出错，异常信息:", e);
        }
        return retModel;
    }


    /**
     * 绑定邮箱
     */
    private Object bindMail (CardAjaxRequest request) {
        ResultData retModel = new ResultData();
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

            String mailAccount = request.getParam().get("mailAccount");
            String directBind = request.getParam().get("directBind");
            Assert.hasLength(mailAccount, "邮箱账号为空");
            if ( Global.CONSTANT_NO.equals(directBind) ) {
                // 如果不是直接绑定则需要校验验证码
                String userCode = request.getParam().get("code");
                String cacheKey = user.getWid() + "_" + mailAccount;
                // 验证用户填写的验证码是否正确
                String code = ( String ) ApplicationContextUtil.get(RedisUtil.class).get(cacheKey);
                Assert.notNull(userCode, "验证码为空");
                if ( ! userCode.equals(code) ) {
                    retModel.setErrmsg("邮箱绑定失败：验证码填写不正确");
                    retModel.setErrcode(ResultData.VERIFY_ERROR_CODE);
                    return retModel;
                }
            } else {// 直接绑定需要校验邮箱号是否一致
                // 获取user详细信息
                Assert.isTrue(mailAccount.equals(user.getEmail()), "邮箱账号与minos账户中账号不一致");
            }

            // 绑定邮箱
            bindUserMail(user, mailAccount);

            retModel.setErrmsg("邮箱绑定成功");
            retModel.setErrcode(ResultData.DEFAULT_SUCCESS_CODE);
        } catch ( Exception e ) {
            retModel.setErrmsg("邮箱绑定失败：" + e.getMessage());
            retModel.setErrcode(ResultData.DEFAULT_ERROR_CODE);
            LOGGER.error("邮箱绑定失败，异常信息:", e);
        }
        return retModel;
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindUserMail (UserInfo user, String mailAccount) {
        UserEmailMapper userEmailMapper = ApplicationContextUtil.get(UserEmailMapper.class);

        // 获取最大order_number值
        List<UserEmailEntity> entities = userEmailMapper.selectList(Wrappers.<UserEmailEntity>lambdaQuery()
                .eq(UserEmailEntity::getUserWid, user.getWid())
                .eq(UserEmailEntity::getIsDelete, Global.DeleteStatus.NO.getId())
                .isNull(UserEmailEntity::getSourceType)
        );
        int orderNumber = 0;
        if ( CollectionUtils.isNotEmpty(entities) ) {
            Optional<UserEmailEntity> max = entities.stream().max(Comparator.comparingInt(UserEmailEntity::getOrderNumber));
            if ( max.isPresent() ) {
                orderNumber = max.get().getOrderNumber() + 1;
            }
        }
        List<UserEmailEntity> userEmailEntities = userEmailMapper.selectList(Wrappers.<UserEmailEntity>lambdaQuery()
                .eq(UserEmailEntity::getUserWid, user.getWid()).eq(UserEmailEntity::getEmail, mailAccount).isNull(UserEmailEntity::getSourceType));
        if ( CollectionUtils.isNotEmpty(userEmailEntities) ) {
            userEmailMapper.update(null, Wrappers.<UserEmailEntity>lambdaUpdate()
                    .eq(UserEmailEntity::getUserWid, user.getWid()).eq(UserEmailEntity::getEmail, mailAccount).isNull(UserEmailEntity::getSourceType)
                    .set(UserEmailEntity::getIsDelete, Global.DeleteStatus.NO.getId())
                    .set(UserEmailEntity::getOrderNumber, orderNumber));
        } else {
            // 插入
            UserEmailEntity userEmailEntity = new UserEmailEntity();
            userEmailEntity.setEmail(mailAccount);
            userEmailEntity.setUserWid(user.getWid());
            userEmailEntity.setWid(StringUtil.uuid());
            userEmailEntity.setIsDelete(Global.DeleteStatus.NO.getId());
            userEmailEntity.setOrderNumber(orderNumber);
            userEmailMapper.insert(userEmailEntity);
        }
    }

    /**
     * 判断minos用户管理该账号下是否存在邮箱(且后缀为系统已支持的邮箱后缀)
     *
     * @return
     */
    private Map<String, Object> existsAccount () {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", Boolean.FALSE);
        try {
            // 判断该邮箱后缀是否为已支持的邮箱后缀
            if ( null != user
                    && StringUtil.isNotEmpty(user.getEmail())
                    && ApplicationContextUtil.get(MailUtil.class).verifyMail(user.getEmail(), user.getWid(), null)
            ) {
                resultMap.put("status", Boolean.TRUE);
                resultMap.put("account", user.getEmail());
            }
        } catch ( Exception e ) {
            LOGGER.error("判断账号下是否存在邮箱失败：", e);
        }
        return resultMap;
    }

    /**
     * 发送验证邮件
     */
    private ResultData sendVerificationMessage (CardAjaxRequest request) {
        ResultData resultData = new ResultData();
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            if ( ObjectUtils.isNotEmpty(ApplicationContextUtil.get(RedisUtil.class).get(Global.USER_SEND_MAIL_SUCCESS + user.getWid())) ) {
                throw new MinosException("请勿重复发送！");
            }

            // 获取输入的邮箱地址
            String mailAccount = request.getParam().get("mailAccount");
            // 校验邮箱是否合法，是否已被绑定过
            Assert.isTrue(ApplicationContextUtil.get(MailUtil.class).verifyMail(mailAccount, user.getWid(), null), "邮箱账号不支持或已被绑定");
            // 生成6位数验证码
            String verificationCode = getVerificationCode();
            String schoolName = MinosLicenseManager.getLicenseInfo().getName();
            Template t = this.getGroupTemplate().getTemplate("/" + this.getCardId() + "/content.html");
            t.binding("verificationCode", verificationCode);
            t.binding("schoolName", schoolName);
            t.binding("userName", user.getUserName());
            String content = t.render();
            String channelId = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("mailSendChannelId");
            String instanceId = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("mailSendInstanceWid");
            // 发送邮件
            SystemToolsService systemToolsService = ApplicationContextUtil.get(DubboServiceComponent.class).getSystemToolsService();
            SynMessageInfo message = new SynMessageInfo();
            Receiver receiver = new Receiver();
            receiver.setEmail(mailAccount);
            message.setContent(content);
            message.setReceiver(receiver);
            message.setChannelId(channelId);
            message.setInstanceWid(instanceId);
            message.setSubject("绑定邮箱验证");
            MessageSynResponse response = systemToolsService.synSendMessage(message);
            if ( ! Global.CONSTANT_NO.equals(response.getCode()) ) { // 消息中心发送消息出错,直接返回错误信息
                resultData.setErrmsg(response.getMsg());
                resultData.setErrcode(ResultData.DEFAULT_ERROR_CODE);
                return resultData;
            }

            // 将数据存入redis中，缓存时间为5分钟
            String cacheKey = user.getWid() + "_" + mailAccount;
            ApplicationContextUtil.get(RedisUtil.class).set(cacheKey, verificationCode, 5, TimeUnit.MINUTES);
            // 将当前用户信息存入缓存，缓存时间为60，防止短信轰炸
            ApplicationContextUtil.get(RedisUtil.class).set(Global.USER_SEND_MAIL_SUCCESS + user.getWid(), LocalDateTime.now().toString(), 1, TimeUnit.MINUTES);
            resultData.setErrmsg("验证邮件发送成功...");
            resultData.setErrcode(ResultData.DEFAULT_SUCCESS_CODE);
        } catch ( Exception e ) {
            resultData.setErrmsg("邮件发送失败：" + e.getMessage());
            resultData.setErrcode(ResultData.DEFAULT_ERROR_CODE);
            LOGGER.error("邮件发送失败:", e);
        }
        return resultData;
    }

    /**
     * 获取邮箱后缀名
     */
    private List<String> getMailSuffix () {
        List<MailPluginEntity> mailPluginList = ApplicationContextUtil.get(MailPluginMapper.class).selectList(Wrappers.lambdaQuery());
        // 获取后缀
        return mailPluginList.stream().map(item -> SourceTypeUtil.SEPARATOR_ATTR + item.getMailSuffix()).distinct().collect(Collectors.toList());
    }

    /**
     * 获取邮箱跳转地址
     */
    private String getLinkUrl (CardAjaxRequest request) {
        // 获取用户绑定的邮箱账号
        String mailAccount = request.getParam().get("mailAccount");
        Assert.hasLength(mailAccount, "邮箱账号不能为空....");
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        // 判断邮箱账号是否被本人绑定
        if ( checkMailAccountIsCurrentUser(user, mailAccount) ) {
            return ApplicationContextUtil.get(MailUtil.class).getLinkUrl(mailAccount);
        }
        LOGGER.error("非法的邮箱账号");
        return "";
    }

    /**
     * 判断邮箱账号是否已经被绑定
     */
    private boolean hasAccountBound (CardAjaxRequest request) {
        // 获取用户绑定的邮箱账号
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        String mailAccount = request.getParam().get("mailAccount");
        Assert.hasLength(mailAccount, "邮箱账号不能为空....");

        int count = ApplicationContextUtil.get(UserEmailMapper.class).selectCount(
                Wrappers.<UserEmailEntity>lambdaQuery()
                        .eq(UserEmailEntity::getUserWid, user.getWid())
                        .eq(UserEmailEntity::getEmail, mailAccount)
                        .eq(UserEmailEntity::getIsDelete, Global.DeleteStatus.NO.getId())
                        .isNull(UserEmailEntity::getSourceType));
        return count > 0;
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

    /**
     * 生成六位验证码
     *
     * @return
     */
    private String getVerificationCode () {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        IntStream.range(0, 6).forEach(i -> code.append(random.nextInt(10)));
        return code.toString();
    }

}
