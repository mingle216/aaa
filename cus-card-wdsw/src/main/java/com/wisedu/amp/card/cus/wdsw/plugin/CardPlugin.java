package com.wisedu.amp.card.cus.wdsw.plugin;

import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.config.ApplicationContextUtil;

import java.util.HashMap;
import java.util.Map;

@MinosSPI
public class CardPlugin extends AbstractCard {
    @Override
    public String getCardId () {
        return "CUS_CARD_WDSW";
    }

    @Override
    public void destroy () {

    }

    @Override
    public Object execDispatcher (CardAjaxRequest request) {
        Object result = null;
        switch ( request.getMethod() ) {
            case "renderData":
                result = this.renderData(request);
                break;
            default:
                return null;
        }
        return result;
    }

    private Map<String,Object> renderData(CardAjaxRequest request){
        HashMap<String, Object> result = new HashMap<>();
        //查询配置信息
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        result.put("config",configInfo);
        return result;
    }
}
