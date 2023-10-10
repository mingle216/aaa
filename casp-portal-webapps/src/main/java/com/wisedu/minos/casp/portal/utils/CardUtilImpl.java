package com.wisedu.minos.casp.portal.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
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
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 获取卡片配置(先从数据库获取，数据库没得时候，获取卡片默认配置)
     * <p/>
     *
     * @param cardWid
     * @return
     * @throws
     * @date 2020-10-1 18:21
     */
    @Override
    public String getCardConfigByCardWid(String cardWid,String cardId) {
        PageCardConfigEntity cardConfigByCardWidFromDb = getCardConfigByCardWidFromDb(cardWid,"");
        if (null != cardConfigByCardWidFromDb) {
            if(StringUtil.isEmpty(cardConfigByCardWidFromDb.getCardConfig())){
                //当此卡片配置为空时，取卡片默认配置
                return getCardConfig(cardId);
            }
            return cardConfigByCardWidFromDb.getCardConfig();
        }
        //当此卡片配置为空时，取卡片默认配置
        return getCardConfig(cardId);
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
        return iCard.getConfig(new CardConfigReq(cardId, cardWid, configJson, new ComponentContainer(configJson, valueConfig),platformType,templateWid));
    }

    private String getCardConfig(String cardId){
        //当此卡片配置为空时，取卡片默认配置
        int platformType=CommonUtil.isMobileDevice(request)?Global.PlatformType.MOBILE.getType():Global.PlatformType.PC.getType();
        return getCardConfigByPlatformType(cardId, platformType);
    }

    private String getCardConfigByPlatformType(String cardId, int platformType) {
        String configJson = homeService.getConfigJson(cardId, platformType);
        ComponentContainer componentContainer = new ComponentContainer(configJson, "");
        Map<String, Object> defaultConfig = new HashMap<>();
        List<AbstractComponent> components = componentContainer.getComponents();
        components.forEach(dataConfig -> {
            defaultConfig.put(String.valueOf(dataConfig.getKey()), dataConfig.getDefaultValue());
        });
        return "\"" + StringEscapeUtils.escapeJava(JSON.toJSONString(defaultConfig)) + "\"";
    }

    private String getConsoleCardConfig(String cardId,int platformType){
        //当此卡片配置为空时，取卡片默认配置
        return getCardConfigByPlatformType(cardId, platformType);
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
        PageCardConfigEntity vo = new PageCardConfigEntity();
        vo.setCardWid(cardWid);
        vo.setPlatformType(platformType);
        vo.setIsDeleted(Global.DeleteStatus.NO.getId());
        wrapper.setEntity(vo);
        //从数据库获取卡片配置
        return pageCardConfigMapper.selectOne(wrapper);
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
        ShowProgrammeEntity defaultProgramme = showProgrammeService.getDefaultProgramme(platformType);
        Integer isShowPcService = defaultProgramme.getIsShowPcService();
        return 1 == isShowPcService;
    }
}
