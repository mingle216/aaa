package com.wisedu.minos.casp.portal.spi.itf;

import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;

import java.util.Map;

/**
 * @author kaisir
 */
@MinosSPI
public interface ICard {

    /**
     * 返回Card的唯一ID
     *
     * @return
     */
    String getCardId ();

    /**
     * The initialize
     **/
    void initialize (Map<String, Object> param);

    /***
     * @Author jcx
     * @Description 获取卡片配置
     * @Date 13:57 2021/1/15
     * @Param cardConfigReq:
     * @return List<Map<String,Object>>
     **/
    ComponentContainer getConfig(CardConfigReq cardConfigReq);


    /**
     * The destroy store templateConfig
     */
    void destroy ();

    Object execDispatcher (CardAjaxRequest request);
}
