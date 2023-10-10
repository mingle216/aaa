package com.wisedu.minos.casp.portal.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.HttpUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.OldOssEntity;
import com.wisedu.minos.casp.portal.dao.entity.PageCardConfigEntity;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.mapper.PageCardConfigMapper;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.configcomponent.AbstractComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.HomeService;
import com.wisedu.minos.casp.portal.service.impl.ShowProgrammeService;
import com.wisedu.minos.casp.portal.spi.MinosExtensionLoader;
import com.wisedu.minos.casp.portal.spi.itf.ICard;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CardUtilImpl implements CardUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(CardUtilImpl.class);
    private static final String CONFIG = "templateConfig";

    @Autowired
    PageCardConfigMapper pageCardConfigMapper;
    @Autowired
    HomeService homeService;
    @Autowired
    HttpServletRequest request;

    @Autowired
    ShowProgrammeService showProgrammeService;
    @Value("${minos.oss.server.addresses}")
    private String minosOssAddResses;
    @Value("${module.domain}")
    private String moudleDomain;
    @Autowired
    private RedisUtil redisUtil;
    /**
     * getCardConfig
     * <p/>
     * 获取卡片配置
     *
     * @param request
     * @param configClass
     * @return T
     * @throws
     * @date 2020-10-1 18:21
     */
    @Override
    public <T> T getCardConfig(CardAjaxRequest request, Class<T> configClass) {
        //先查询传参，如果传参没有则需要查询库，在没有则初始化默认
        String configInfo;
        if (null != request.getParam() && null != request.getParam().get(CONFIG)) {
            configInfo = request.getParam().get(CONFIG);
        } else {
            configInfo = getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        }
        if(org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)){
            configInfo= JSON.parse(configInfo).toString();
        }
        T config = JSONObject.parseObject(configInfo, configClass);
        if (null == config) {
            try {
                config = configClass.newInstance();
            } catch (Exception e) {
                LOGGER.error("卡片类型实例化失败:" + configClass.getName(), e);
            }
        }
        return config;
    }

    /**
     * 获取卡片配置(先从数据库获取，数据库没得时候，获取卡片默认配置,当数据库有值，没有默认配置中的一个时，会添加到数据库数据，并返回给前端)
     * <p/>
     *
     * @param cardWid
     * @return
     * @throws
     * @date 2020-10-1 18:21
     */
    @Override
    public String getCardConfigByCardWid(String cardWid,String cardId) {
        if( !StringUtils.isEmpty(cardWid) ) {
            PageCardConfigEntity cardConfigByCardWidFromDb = getCardConfigByCardWidFromDb(cardWid, "");
            if ( null != cardConfigByCardWidFromDb ) {
                if ( StringUtil.isEmpty(cardConfigByCardWidFromDb.getCardConfig()) ) {
                    //当此卡片配置为空时，取卡片默认配置
                    return getCardConfig(cardId);
                }
                String str = StringEscapeUtils.unescapeJava(filterHttpOrHttps(cardConfigByCardWidFromDb.getCardConfig()));
                Map<String, Object> cardConfigMap = getCardConfigMap(cardId);
                JSONObject jsonObject = JSONObject.parseObject(str.substring(1, str.length() - 1));
                if( MapUtils.isNotEmpty(cardConfigMap) ){
                    Set<String> keySets = cardConfigMap.keySet();
                    //进行比对
                    keySets.forEach(keySet->{
                        if(!jsonObject.containsKey(keySet)){
                            jsonObject.put(keySet,cardConfigMap.get(keySet));
                        }
                    });
                }
                return "\"" + StringEscapeUtils.escapeJava(JSON.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue)) + "\"";
            }
        }
        //当此卡片配置为空时，取卡片默认配置
        return getCardConfig(cardId);
    }
    /**
     * @Author jcx
     * @Description 使得配置中oss的http、https强制与门户保持一致
     * @Date 17:14 2022/1/19
     * @Param data:
     * @return String
     **/
    @Override
    public String filterHttpOrHttps(String data){
        if(StringUtil.isNotEmpty(data)){
            Object obj= redisUtil.get(Global.OLD_OSS_URL_DATA);
            if(null!=obj){
                List<OldOssEntity> oldOss = JSON.parseArray(String.valueOf(obj), OldOssEntity.class);
                for ( int i = 0 ; i <oldOss.size() ; i++ ) {
                    data=data.replaceAll(oldOss.get(i).getUrl(),minosOssAddResses);
                }
            }
        }
        return data;
    }

    @Override
    public String getConsoleCardConfigByCardWid(String cardWid,String cardId,int platformType) {
        PageCardConfigEntity cardConfigByCardWidFromDb = getConsoleCardConfigByCardWidFromDb(cardWid,platformType);
        if (null != cardConfigByCardWidFromDb) {
            if(StringUtil.isEmpty(cardConfigByCardWidFromDb.getCardConfig())){
                //当此卡片配置为空时，取卡片默认配置
                return getConsoleCardConfig(cardId,platformType);
            }
            return cardConfigByCardWidFromDb.getCardConfig();
        }
        //当此卡片配置为空时，取卡片默认配置
        return getConsoleCardConfig(cardId,platformType);
    }

    @Override
    public ComponentContainer getComContainerConfig(String cardWid, String cardId, String valueConfig, int platformType, String templateWid) {
        ICard iCard = getCard(cardId);
        Global.PlatformType localPlatformType = Global.PlatformType.getByType(platformType);
        String configJson = homeService.getConfigJson(cardId,localPlatformType.getType());
        return iCard.getConfig(new CardConfigReq(cardId, cardWid, configJson, new ComponentContainer(configJson, filterHttpOrHttps(valueConfig)),platformType,templateWid));
    }

    private String getCardConfig(String cardId){
        //当此卡片配置为空时，取卡片默认配置
        int platformType=CommonUtil.isMobileDevice(request)?Global.PlatformType.MOBILE.getType():Global.PlatformType.PC.getType();
        return filterHttpOrHttps(getCardConfigByPlatformType(cardId, platformType));
    }

    private Map<String, Object> getCardConfigMap(String cardId){
        //当此卡片配置为空时，取卡片默认配置
        int platformType=CommonUtil.isMobileDevice(request)?Global.PlatformType.MOBILE.getType():Global.PlatformType.PC.getType();
        return getDefaultConfig(cardId, platformType);
    }

    private String getCardConfigByPlatformType(String cardId, int platformType) {
        return "\"" + StringEscapeUtils.escapeJava(JSON.toJSONString(getDefaultConfig(cardId, platformType))) + "\"";
    }

    /**
     * 将默认配置文件转成map
     * @param cardId
     * @param platformType
     * @return
     */
    private Map<String, Object> getDefaultConfig(String cardId, int platformType){
        String configJson = homeService.getConfigJson(cardId, platformType);
        ComponentContainer componentContainer = new ComponentContainer(configJson, "");
        Map<String, Object> defaultConfig = new HashMap<>();
        List<AbstractComponent> components = componentContainer.getComponents();
        components.forEach(dataConfig -> {
            defaultConfig.put(String.valueOf(dataConfig.getKey()), dataConfig.getDefaultValue());
        });
        return defaultConfig;
    }

    private String getConsoleCardConfig(String cardId,int platformType){
        //当此卡片配置为空时，取卡片默认配置
        return filterHttpOrHttps(getCardConfigByPlatformType(cardId, platformType));
    }

    @Override
    public PageCardConfigEntity getCardConfigByCardWidFromDb(String cardWid,String platformType) {
        //卡片调用获取配置需判断终端类型
        int platformTypeInfo=CommonUtil.isMobileDevice(request)?Global.PlatformType.MOBILE.getType():Global.PlatformType.PC.getType();
        if(StringUtil.isNotEmpty(platformType)){
            platformTypeInfo=Global.PlatformType.getByType(Integer.valueOf(platformType)).getType();
        }
        return getPageCard(cardWid,platformTypeInfo);
    }

    private PageCardConfigEntity getPageCard(String cardWid,int platformType){
        QueryWrapper<PageCardConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PageCardConfigEntity::getCardWid,cardWid).eq(PageCardConfigEntity::getPlatformType,platformType)
                .eq(PageCardConfigEntity::getIsDeleted,Global.DeleteStatus.NO.getId());
        //从数据库获取卡片配置
        PageCardConfigEntity pageCardConfigEntity = pageCardConfigMapper.selectOne(wrapper);
        if(null!=pageCardConfigEntity&&StringUtil.isNotEmpty(pageCardConfigEntity.getCardConfig())){
            pageCardConfigEntity.setCardConfig(filterHttpOrHttps(pageCardConfigEntity.getCardConfig()));
        }
        return pageCardConfigEntity;
    }

    @Override
    public PageCardConfigEntity getConsoleCardConfigByCardWidFromDb(String cardWid,int platformType) {
        Global.PlatformType localPlatformType = Global.PlatformType.getByType(platformType);
        return getPageCard(cardWid,localPlatformType.getType());
    }

    @Override
    public void putConfigInfos(String cardWid, String configInfo) {

    }

    @Override
    public ICard getCard(String cardId) {
        Map<String, ICard> cardMap = getCardMap();
        ICard iCard = cardMap.get(cardId);
        if (null == iCard) {
            throw new BusinessException(String.format("系统异常，该卡片%s未安装", cardId));
        }
        return iCard;
    }

    @Override
    public Map<String, ICard> getCardMap() {
        return MinosExtensionLoader.getExtensionLoader(ICard.class).getSupportedExtensionInstances();
    }

    @Override
    public boolean getIsShowPCService(){
        int platformType=CommonUtil.isMobileDevice(request)?Global.PlatformType.MOBILE.getType():Global.PlatformType.PC.getType();
        ShowProgrammeEntity defaultProgramme = showProgrammeService.getDefaultProgramme(platformType,HttpUtil.getValueFromSessionAndCookie(Global.SITE_WID,request));
        Integer isShowPcService = defaultProgramme.getIsShowPcService();
        return 1 == isShowPcService;
    }
}
