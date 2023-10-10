package com.wisedu.minos.casp.portal.utils;

import com.wisedu.minos.casp.portal.dao.entity.PageCardConfigEntity;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.spi.itf.ICard;

import java.util.Map;

public interface CardUtil {


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
    <T> T getCardConfig(CardAjaxRequest request, Class<T> configClass);

    /**
     * 获取卡片配置(先从数据库获取，数据库没得时候，获取卡片默认配置)
     * <p/>
     *
     * @param cardWid
     * @return
     * @throws
     * @date 2020-10-1 18:21
     */
    String getCardConfigByCardWid(String cardWid,String cardId);

    ComponentContainer getComContainerConfig(String cardWid, String cardId, String valueConfig, int platformType, String templateWid);

    /**
     * 管控台获取配置信息
     * @param cardWid
     * @param cardId
     * @param platformType
     * @return
     */
    String getConsoleCardConfigByCardWid(String cardWid,String cardId,int platformType);

    /**
     * 获取卡片配置从数据库
     * <p/>
     *
     * @param cardWid
     * @return
     * @throws
     * @date 2021-03-03 9:12
     */
    PageCardConfigEntity getCardConfigByCardWidFromDb(String cardWid,String platformType);
    /**
     * 管控台获取卡片配置从数据库
     * <p/>
     *
     * @param cardWid
     * @return
     * @throws
     * @date 2021-03-03 9:12
     */
    PageCardConfigEntity getConsoleCardConfigByCardWidFromDb(String cardWid,int platformType);

    /**
     * putConfigInfos
     * <p/>
     * 设置卡片值
     *
     * @param cardWid
     * @param configInfo
     * @return void
     * @throws
     * @date 2020-10-1 19:56
     */
    void putConfigInfos(String cardWid, String configInfo);

    /**
     * getCard
     * <p/>
     * 获取卡片
     * @param cardId
     * @throws
     * @return com.wisedu.minos.casp.portal.spi.itf.ICard
     * @date 2020-10-3 13:03
     */
    ICard getCard(String cardId);

    /**
     * getCardMap
     * <p/>
     *  获取加载到的卡片map
     * @param
     * @throws
     * @return java.util.Map<java.lang.String,com.wisedu.minos.casp.portal.spi.itf.ITemplate>
     * @date 2020-10-3 13:07
     */
    Map<String, ICard> getCardMap();

    /**
     * 获取是否需要展示pc服务
     * @return
     */
    public boolean getIsShowPCService();


}
