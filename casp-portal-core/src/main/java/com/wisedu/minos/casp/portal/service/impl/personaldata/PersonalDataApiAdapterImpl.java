package com.wisedu.minos.casp.portal.service.impl.personaldata;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.InternationalizationEntity;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataEntity;
import com.wisedu.minos.casp.portal.dao.mapper.InternationalizationMapper;
import com.wisedu.minos.casp.portal.dao.mapper.PersonalDataMapper;
import com.wisedu.minos.casp.portal.model.CommonResponseResult;
import com.wisedu.minos.casp.portal.model.MultiLangData;
import com.wisedu.minos.casp.portal.model.personal.*;
import com.wisedu.minos.casp.portal.service.PersonalDataApiAdapter;
import com.wisedu.minos.casp.portal.service.impl.InternationalizationService;
import com.wisedu.minos.casp.portal.spi.MinosExtensionLoader;
import com.wisedu.minos.casp.portal.spi.itf.IAuth;
import com.wisedu.minos.casp.portal.spi.itf.IMail;
import com.wisedu.minos.casp.portal.spi.model.MailPluginInfo;
import com.wisedu.minos.casp.portal.spi.model.PluginInfo;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.CommunalUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * //todo 添加描述
 * @date 2021/7/14 14:49
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Service
public class PersonalDataApiAdapterImpl implements PersonalDataApiAdapter {
    public static final String PERSONAL_KEY = "redisKey:personalData";
    public static final String INTERNATIONAL_KEY = "redisKey:perTitleInternation";

    @Autowired
    private InternationalizationService internationalizationService;
    @Autowired
    private PersonalDataMapper personalDataMapper;
    @Autowired
    private InternationalizationMapper internationalizationMapper;
    @Autowired
    private CardUtil cardUtil;
@Autowired
private StringRedisTemplate stringRedisTemplate;
    /**
     * 添加个人数据
     * @param dto
     * @return com.wisedu.minos.casp.portal.model.CommonResponseResult
     * @author jszhang
     * @date: 2021/7/14 14:50
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponseResult addPersonDataConfig(PersonalDataDto dto) {
        //是个人信息:不保存图标和标题等数据
        if(dto.getIsPersonalData() == 1){
            PersonalDataEntity personalDataEntity = new PersonalDataEntity();
            personalDataEntity.setEnabled(dto.getEnabled());
            personalDataEntity.setSourceType(dto.getSourceType());
            personalDataEntity.setCacheTimeout(dto.getCacheTimeout());
            personalDataEntity.setIsDeleted(0);
            personalDataEntity.setPersonalData(1);
            personalDataEntity.setPersonalTitle(dto.getPersonalTitle());
            personalDataEntity.setIsHiddenPrivacy(dto.getIsHiddenPrivacy());
            processPersonalEntity(personalDataEntity, dto);

            personalDataEntity.setOrderNumber(getMaxSortNum());
            personalDataMapper.insert(personalDataEntity);
        }else{
            PersonalDataEntity personalDataEntity = getPersonalDataEntity(dto);
            personalDataEntity.setOrderNumber(getMaxSortNum());
            personalDataMapper.insert(personalDataEntity);
        }
        loadPersonalDataToRedis();
        return new CommonResponseResult<>();
    }



    @Override
    public CommonResponseResult editPersonDataConfig(PersonalDataDto dto) {
        String wid = dto.getWid();
        if(wid == null){
            throw new BusinessException("wid 不能为null");
        }
        //是个人信息：不更新标题等数据
        if(dto.getIsPersonalData() == 1){
            PersonalDataEntity personalDataEntity = new PersonalDataEntity();
            personalDataEntity.setEnabled(dto.getEnabled());
            personalDataEntity.setIconUrl(dto.getIconUrl());
            personalDataEntity.setPersonalData(dto.getIsPersonalData());
            personalDataEntity.setSourceType(dto.getSourceType());
            personalDataEntity.setCacheTimeout(dto.getCacheTimeout());
            personalDataEntity.setPersonalData(dto.getIsPersonalData());
            personalDataEntity.setIsDeleted(0);
            processPersonalEntity(personalDataEntity, dto);
            personalDataEntity.setOrderNumber(dto.getOrderNumber());
            personalDataEntity.setPersonalData(1);
            personalDataEntity.setPersonalTitle(dto.getPersonalTitle());
            personalDataEntity.setWid(wid);
            personalDataEntity.setIsHiddenPrivacy(dto.getIsHiddenPrivacy());
            personalDataMapper.updateById(personalDataEntity);
        }else{
            PersonalDataEntity personalDataEntity = getPersonalDataEntity(dto);
            personalDataEntity.setOrderNumber(dto.getOrderNumber());
            personalDataEntity.setPersonalData(0);
            personalDataEntity.setWid(wid);
            personalDataMapper.updateById(personalDataEntity);
        }
        //todo 个人数据key

        loadPersonalDataToRedis();
        return new CommonResponseResult();
    }
//    public void removeByKeyPrefix(String keyPrefix){
//        ScanOptions scanOptions = new ScanOptions();
//
//        stringRedisTemplate.scan()
//    }

    @Override
    public CommonResponseResult<List<PersonalDataVo>> getPersonalDataConfig(PersonalDataQueryConfigDto dto) {
        QueryWrapper<PersonalDataEntity> queryWrapper = new QueryWrapper<>();
        String encodeTitle = CommonUtil.encodeLikeKeyWordEncode(dto.getTitle());
        queryWrapper
                .eq(dto.getEnabled() != null, "enabled", dto.getEnabled())
                .eq(dto.getSourceType() != null, "source_type", dto.getSourceType())
                .eq("is_deleted", 0)
                .apply(StringUtil.isNotEmpty(encodeTitle), "lower(title) like {0} escape '/'",
                        "%"+encodeLikeKeyWordEncode(StringUtils.trim(dto.getTitle()).toLowerCase())+"%")
                .orderByDesc("order_number");

        List<PersonalDataEntity> dataEntities = personalDataMapper.selectList(queryWrapper);
        List<PersonalDataVo> collect = dataEntities.stream().map(this::processEntityToVo).collect(Collectors.toList());
        collect.forEach(coll->{
            if(StringUtil.isNotEmpty(coll.getIconUrl())){
                coll.setIconUrl(cardUtil.filterHttpOrHttps(coll.getIconUrl()));
            }
        });
        return new CommonResponseResult<List<PersonalDataVo>>().setData(collect);
    }

    @Override
    public CommonResponseResult<List<MailPluginInfo>> getMailPlugins() {
        Map<String, IMail> supportedExtensionInstances = MinosExtensionLoader.getExtensionLoader(IMail.class).getSupportedExtensionInstances();
        Iterator<IMail> iterator = supportedExtensionInstances.values().iterator();
        List<MailPluginInfo> list =new ArrayList<>();
        while (iterator.hasNext()){
            list.add(iterator.next().getPluginInfo());
        }
        return new CommonResponseResult<List<MailPluginInfo>>().setData(list);
    }
    public static String encodeLikeKeyWordEncode(String keyworld) {
        if (org.apache.commons.lang3.StringUtils.isBlank(keyworld)) {
            return null;
        } else {
            return keyworld.replaceAll("/", "//")
                    .replaceAll("%", "/%")
                    .replaceAll("_", "/_")
                    ;
        }
    }
    @Override
    public CommonResponseResult updateEnableState(UpdateStateDto dto) {
        UpdateWrapper<PersonalDataEntity> updateWrapper =new UpdateWrapper<>();
        updateWrapper.lambda().set(PersonalDataEntity::getEnabled,dto.getEnabled()).eq(PersonalDataEntity::getWid,dto.getWid());
        personalDataMapper.update(null,updateWrapper);
        loadPersonalDataToRedis();
        return new CommonResponseResult();
    }

    @Override
    public CommonResponseResult delete(String wid) {
        UpdateWrapper<PersonalDataEntity> updateWrapper =new UpdateWrapper<>();
        updateWrapper.lambda().set(PersonalDataEntity::getIsDeleted,1).eq(PersonalDataEntity::getWid,wid);
        personalDataMapper.update(null,updateWrapper);
        return new CommonResponseResult();
    }

    @Override
    public CommonResponseResult updateSorting(List<UpdateSortingDto> dto) {
        dto.forEach(e->{
            UpdateWrapper<PersonalDataEntity> updateWrapper =new UpdateWrapper<>();
            updateWrapper.lambda().set(PersonalDataEntity::getOrderNumber,e.getOrderNumber())
                    .eq(PersonalDataEntity::getWid,e.getWid());
            personalDataMapper.update(null,updateWrapper);
        });
        return new CommonResponseResult();
    }

    @Override
    public CommonResponseResult grant(List<PersonalDataGrantDto> dto) {
      return null;
    }

    @Override
    public CommonResponseResult<List<PluginInfo>> getPlugins() {
        Map<String, IAuth> supportedExtensionInstances = MinosExtensionLoader.getExtensionLoader(IAuth.class).getSupportedExtensionInstances();
        Iterator<IAuth> iterator = supportedExtensionInstances.values().iterator();
        List<PluginInfo> list =new ArrayList<>();
        while (iterator.hasNext()){
            String authId = iterator.next().getAuthId();
            list.add(new PluginInfo().setPluginId(authId).setPluginName(authId));
        }
        return new CommonResponseResult<List<PluginInfo>>().setData(list);
    }

    /**
     * 读取个人聚合数据存到redis
     */
    @Override
    public void loadPersonalDataToRedis() {
        LambdaQueryWrapper<PersonalDataEntity> queryWrapper = new LambdaQueryWrapper<PersonalDataEntity>()
                .eq(PersonalDataEntity::getIsDeleted,Global.DeleteStatus.NO.getId())
                .eq(PersonalDataEntity::getPersonalData,Global.DeleteStatus.NO.getId())
                .eq(PersonalDataEntity::getEnabled,Global.EnableStatus.ENABLE.getId());
        List<PersonalDataEntity> personalDataEntityList = personalDataMapper.selectList(queryWrapper);
        ApplicationContextUtil.get(RedisUtil.class).save(PERSONAL_KEY, personalDataEntityList);
        //存储标题多语言数据
        if(CollectionUtils.isNotEmpty(personalDataEntityList)){
            List<String> personWidList = personalDataEntityList.stream().map(PersonalDataEntity::getTitleLangKey).collect(Collectors.toList());
            LambdaQueryWrapper<InternationalizationEntity> internationalWrapper = new LambdaQueryWrapper<InternationalizationEntity>()
                    .in(InternationalizationEntity::getLangKey,personWidList);
            List<InternationalizationEntity> internationalizationEntityList = internationalizationMapper.selectList(internationalWrapper);
            ApplicationContextUtil.get(RedisUtil.class).save(INTERNATIONAL_KEY, internationalizationEntityList);
        }
    }

    /**
     * 数据库实体转vo
     * @param entity
     * @return com.wisedu.minos.casp.portal.model.personal.PersonalDataVo
     * @author jszhang
     * @date: 2021/7/15 10:03
     */
    public PersonalDataVo processEntityToVo(PersonalDataEntity entity) {
        PersonalDataVo personalDataVo = new PersonalDataVo();
        Integer sourceType = entity.getSourceType();
        personalDataVo.setWid(entity.getWid())
                .setTitle(getMultiLangByLangKey(entity.getTitleLangKey(),entity.getTitle()))
                .setMainInfo(getMultiLangByLangKey(entity.getMainInfoLangKey(),entity.getMainInfo()))
                .setSubInfo(getMultiLangByLangKey(entity.getSubInfoLangKey(),entity.getSubInfo()))
                .setCacheTimeout(entity.getCacheTimeout())
                .setIconUrl(entity.getIconUrl())
                .setSourceType(sourceType)
                .setOrderNumber(entity.getOrderNumber())
                .setEnabled(entity.getEnabled())
                .setIsPersonalData(entity.getPersonalData())
                .setIsHiddenPrivacy(entity.getIsHiddenPrivacy()==null?0:entity.getIsHiddenPrivacy())
                .setPersonalTitle(entity.getPersonalTitle());
        switch (entity.getSourceType()) {
            case 0:
                personalDataVo.setStaticDataSource(new PersonalDataConfigBase.StaticDataSource().setLinkUrl(entity.getLinkUrl()));
                break;
            case 1:
                personalDataVo.setJsonDataSource(new PersonalDataConfigBase.JSONDataSource().setSourceKey(entity.getSourceKey()).setHttpMethod(entity.getHttpMethod()).setLinkUrl(entity.getLinkUrl()).setUserKey(entity.getUserKey()));
                break;
            case 2:
                personalDataVo.setMinosDataSource(new PersonalDataConfigBase.MinosDataSource().setDataSql(entity.getDataSql()).setSourceKey(entity.getSourceKey()).setLinkUrl(entity.getLinkUrl()));
                break;
            case 3:
                personalDataVo.setMailDataSource(new PersonalDataConfigBase.MailDataSource().setSourceKey(entity.getSourceKey()));
                break;
            case 4:
                personalDataVo.setPluginDataSource(new PersonalDataConfigBase.PluginDataSource().setSourceKey(entity.getSourceKey()).setAuthConfig(entity.getAuthConfig()).setLinkUrl(entity.getLinkUrl()));
                break;
            default:
                break;
        }
        return personalDataVo;
    }

    /**
     * 获取多语言通过langkey
     * @param langKey
     * @return java.util.List<com.wisedu.minos.casp.portal.model.MultiLangData>
     * @author jszhang
     * @date: 2021/7/15 9:42
     */
    List<MultiLangData> getMultiLangByLangKey(String langKey,String defaultZhVal) {
        if(StringUtil.isBlank(langKey)){
            return Lists.newArrayList(new MultiLangData().setLangCountry(Global.DEFAULT_LANGUAGE).setLangValue(defaultZhVal));
        }
        return internationalizationService.list(new QueryWrapper<InternationalizationEntity>()
                .lambda().eq(InternationalizationEntity::getIsDeleted, 0)
                .eq(InternationalizationEntity::getLangKey, langKey)).stream().map(e ->
                new MultiLangData().setLangKey(langKey).setLangCountry(e.getLangCountry()).setLangValue(e.getLangValue())
        ).collect(Collectors.toList());

    }

    /***
     * 将dto转实体
     * @param personalDataEntity
     * @param: dto
     * @return void
     * @author jszhang
     * @date: 2021/7/14 17:50
     */
    private void processPersonalEntity(PersonalDataEntity personalDataEntity, PersonalDataDto dto) {
        switch (dto.getSourceType()) {
            case 0:
                personalDataEntity.setLinkUrl(dto.getStaticDataSource().getLinkUrl());
                break;
            case 1:
                personalDataEntity.setSourceKey(dto.getJsonDataSource().getSourceKey());
                personalDataEntity.setUserKey(dto.getJsonDataSource().getUserKey());
                personalDataEntity.setHttpMethod(dto.getJsonDataSource().getHttpMethod());
                personalDataEntity.setLinkUrl(dto.getJsonDataSource().getLinkUrl());
                break;
            case 2:
                personalDataEntity.setDataSql(dto.getMinosDataSource().getDataSql());
                personalDataEntity.setLinkUrl(dto.getMinosDataSource().getLinkUrl());
                personalDataEntity.setSourceKey(dto.getMinosDataSource().getSourceKey());
                break;
            case 3:
                personalDataEntity.setSourceKey(dto.getMailDataSource().getSourceKey());
                break;
            case 4:
                personalDataEntity.setSourceKey(dto.getPluginDataSource().getSourceKey());
                personalDataEntity.setAuthConfig(dto.getPluginDataSource().getAuthConfig());
                personalDataEntity.setLinkUrl(dto.getPluginDataSource().getLinkUrl());
                break;
            default:

        }
    }
    /***
     * 处理多语言（封装对象并入库）
     * @param langDataList
     * @return com.wisedu.minos.casp.portal.model.MultiLangData
     * @author jszhang
     * @date: 2021/7/15 10:02
     */
    private MultiLangData processMultiLangData(List<MultiLangData> langDataList) {
        String langKey = null;
        //移除langval为空的

        List<MultiLangData> removedEmptyLangList = langDataList.stream().filter(e -> StringUtils.isNotBlank(e.getLangValue())).collect(Collectors.toList());
        if (removedEmptyLangList.isEmpty()) {
            return new MultiLangData();
        }
        langKey = removedEmptyLangList.stream().filter(e -> StringUtils.isNotBlank(e.getLangKey())).findAny().orElse(new MultiLangData()).getLangKey();
        //说明是新增的
        if (langKey == null) {
            langKey = CommunalUtil.getWid();
            final String finalLangKey = langKey;
            addLangList(removedEmptyLangList, finalLangKey);
            return getCnLang(removedEmptyLangList, finalLangKey);
        } else {
            List<InternationalizationEntity> entities = internationalizationService.list(new QueryWrapper<InternationalizationEntity>().lambda()
                    .eq(InternationalizationEntity::getLangKey, langKey)
                    .eq(InternationalizationEntity::getIsDeleted, 0));
            if (entities.isEmpty()) {
                String finalLangKey = CommunalUtil.getWid();
                addLangList(removedEmptyLangList, finalLangKey);
                return getCnLang(removedEmptyLangList, finalLangKey);

            }
            entities.forEach(e -> {
                        List<MultiLangData> multiLangData = removedEmptyLangList.stream().filter(f -> e.getLangCountry().equals(f.getLangCountry())).collect(Collectors.toList());
                        if (multiLangData.isEmpty()) {
                            e.setIsDeleted(1);
                        } else {
                            e.setLangValue(multiLangData.get(0).getLangValue());
                        }
                    }
            );
            internationalizationService.updateBatchById(entities);
            List<MultiLangData> addLangList = removedEmptyLangList.stream().filter(e ->
                    entities.stream().filter(f -> f.getLangCountry().equals(e.getLangCountry())).collect(Collectors.toList()).isEmpty()
            ).collect(Collectors.toList());
            if (!addLangList.isEmpty()) {
                String finalLangKey = entities.get(0).getLangKey();
                addLangList(addLangList, finalLangKey);
            }
            return getCnLang(removedEmptyLangList, entities.get(0).getLangKey());

        }

    }
    /**
     * 保存多语言到数据库
     * @param langList
     * @param: langKey
     * @return void
     * @author jszhang
     * @date: 2021/7/15 10:01
     */
    private void addLangList(List<MultiLangData> langList, String langKey) {
        List<InternationalizationEntity> internationalizationEntities = langList.stream().map(e -> {
            InternationalizationEntity internationalizationEntity = new InternationalizationEntity();
            internationalizationEntity.setIsDeleted(0);
            internationalizationEntity.setLangCountry(e.getLangCountry());
            internationalizationEntity.setLangKey(langKey);
            internationalizationEntity.setLangValue(e.getLangValue());
            return internationalizationEntity;
        }).collect(Collectors.toList());
        internationalizationService.saveBatch(internationalizationEntities);
    }

    private MultiLangData getCnLang(List<MultiLangData> langList, String langKey) {
        return langList.stream().filter(e -> Global.DEFAULT_LANGUAGE.equals(e.getLangCountry())).findAny().orElse(new MultiLangData()).setLangKey(langKey);
    }

    public int getMaxSortNum(){
        IPage<PersonalDataEntity> personalDataEntityIPage = personalDataMapper.selectPage(new Page<PersonalDataEntity>().setSize(1).setCurrent(1),
                new QueryWrapper<PersonalDataEntity>().lambda()
                        .eq(PersonalDataEntity::getIsDeleted, 0)
                        .orderByDesc(PersonalDataEntity::getOrderNumber));
        if(personalDataEntityIPage.getRecords().isEmpty()){
            return 0;
        }else{
            return personalDataEntityIPage.getRecords().get(0).getOrderNumber()+1;
        }

    }

    private PersonalDataEntity getPersonalDataEntity(PersonalDataDto dto) {
        List<MultiLangData> title = dto.getTitle();
        MultiLangData titleLang = processMultiLangData(title);
        String zhTitle = titleLang.getLangValue();
        if (StringUtil.isBlank(zhTitle)) {
            throw new BusinessException("标题请设置中文");
        }
        Integer integer = personalDataMapper.selectCount(new QueryWrapper<PersonalDataEntity>().lambda()
                .eq(PersonalDataEntity::getTitle, zhTitle)
                .eq(PersonalDataEntity::getIsDeleted, 0)
                .ne(StringUtils.isNotBlank(dto.getWid()),PersonalDataEntity::getWid,dto.getWid()));
        if (integer>0){
            throw new BusinessException("标题名字已经存在");
        }
        String titleLangKey = titleLang.getLangKey();

        List<MultiLangData> mainInfo = dto.getMainInfo();
        MultiLangData mainInfoLang = processMultiLangData(mainInfo);
        String zhMainInfo = mainInfoLang.getLangValue();
        if (StringUtil.isBlank(zhMainInfo)) {
            throw new BusinessException("主要信息请设置中文");
        }
        String mainInfoLangKey = mainInfoLang.getLangKey();

        List<MultiLangData> subInfo = dto.getSubInfo();
        MultiLangData subInfoLang = processMultiLangData(subInfo);
        String subLangKey = subInfoLang.getLangKey();

        PersonalDataEntity personalDataEntity = new PersonalDataEntity();
        personalDataEntity.setTitle(zhTitle);
        personalDataEntity.setTitleLangKey(titleLangKey);

        personalDataEntity.setMainInfo(zhMainInfo);
        personalDataEntity.setMainInfoLangKey(mainInfoLangKey);

        personalDataEntity.setSubInfo(subInfoLang.getLangValue());
        personalDataEntity.setSubInfoLangKey(subLangKey);
        personalDataEntity.setEnabled(dto.getEnabled());
        personalDataEntity.setIconUrl(dto.getIconUrl());
        personalDataEntity.setSourceType(dto.getSourceType());
        personalDataEntity.setCacheTimeout(dto.getCacheTimeout());
        personalDataEntity.setPersonalData(dto.getIsPersonalData());
        personalDataEntity.setIsDeleted(0);
        personalDataEntity.setIsHiddenPrivacy(dto.getIsHiddenPrivacy());
        processPersonalEntity(personalDataEntity, dto);
        return personalDataEntity;
    }
}
